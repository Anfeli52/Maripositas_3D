package com.andres.mariposita3d.Controller;

import java.util.Date;
import java.util.List;

import com.andres.mariposita3d.Collection.EspecieMariposa;
import com.andres.mariposita3d.Collection.Ubicacion;
import com.andres.mariposita3d.Collection.Usuario;
import com.andres.mariposita3d.DTO.EspecieMariposaFormDTO;
import com.andres.mariposita3d.Service.UbicacionService;

import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import com.andres.mariposita3d.Enum.TipoEspecie;
import com.andres.mariposita3d.Service.EspecieMariposaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ViewController {

    private final EspecieMariposaService butterflyService;
    private final UbicacionService ubicacionService;

    public ViewController(EspecieMariposaService butterflyService, UbicacionService ubicacionService) {
        this.butterflyService = butterflyService;
        this.ubicacionService = ubicacionService;
    }

    @GetMapping("/main")
    @PreAuthorize("hasRole('USER')")
    public String userMain(Model model){
        List<MariposaDetalleDTO> butterflyDetails = butterflyService.findAllWithUbicationDetails();
        model.addAttribute("butterflies", butterflyDetails);
        return "User/userMainPage";
    }

    @GetMapping("/admin/main")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminMain(Model model){
        List<MariposaDetalleDTO> butterflyDetails = butterflyService.findAllWithUbicationDetails();
        model.addAttribute("butterflies", butterflyDetails);

        return "Admin/adminMainPage";
    }

    @GetMapping("admin/registroMariposa")
    @PreAuthorize("hasRole('ADMIN')")
    public String registroMariposa(Model model) {
        // 1. Inicializa el DTO en lugar de la Entidad
        model.addAttribute("especieMariposaForm", new EspecieMariposaFormDTO());
        model.addAttribute("ubicacionesDisponibles", ubicacionService.all());

        return "Admin/registroMariposa";
    }

    @PostMapping("admin/nueva-especie")
    public String guardarNuevaEspecie(@ModelAttribute("especieMariposaForm") EspecieMariposaFormDTO formDTO, Authentication authentication) {

        ObjectId ubicacionFinalId;

        if (formDTO.isGuardarNuevaUbicacion() && formDTO.getNuevaUbicacion() != null) {
            Ubicacion nuevaUbicacion = formDTO.getNuevaUbicacion();
            double lat = nuevaUbicacion.getGeolocalizacion().getLatitud();
            double lon = nuevaUbicacion.getGeolocalizacion().getLongitud();
            nuevaUbicacion.setGeolocalizacion(new Ubicacion.GeoLatLon(lat, lon));
            nuevaUbicacion.setGeolocalizacionGeojson(new Ubicacion.GeoPoint(lon, lat));
            nuevaUbicacion.setFechaRegistro(new Date());
            Ubicacion ubicacionGuardada = ubicacionService.save(nuevaUbicacion);
            ubicacionFinalId = ubicacionGuardada.getId();

        } else if (formDTO.getUbicacionRecoleccionId() != null) {
            ubicacionFinalId = formDTO.getUbicacionRecoleccionId();
        } else {
            return "redirect:/admin/registroMariposa?error=ubicacion";
        }

        EspecieMariposa especieMariposa = mapDtoToEntity(formDTO);

        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        ObjectId registradoPorId = new ObjectId(usuarioActual.getId());

        especieMariposa.setRegistradoPorId(registradoPorId);
        especieMariposa.setFechaRegistro(new Date());
        especieMariposa.setUbicacionRecoleccionId(ubicacionFinalId);

        // 4. Guardar la Mariposa
        butterflyService.save(especieMariposa);

        return "redirect:/admin/main";
    }

    private EspecieMariposa mapDtoToEntity(EspecieMariposaFormDTO dto) {
        EspecieMariposa entity = new EspecieMariposa();

        entity.setNombreCientifico(dto.getNombreCientifico());
        entity.setNombreComun(dto.getNombreComun());
        entity.setFamilia(dto.getFamilia());
        entity.setTipoEspecie(TipoEspecie.valueOf(dto.getTipoEspecie().toLowerCase()));
        entity.setDescripcion(dto.getDescripcion());
        entity.setCaracteristicaMorfo(dto.getCaracteristicasMorfo());
        entity.setImagenDetallada(dto.getImagenesDetalladas());
        entity.setImagenes(dto.getImagenes());

        return entity;
    }
    

    @GetMapping("/user/mapa")
    @PreAuthorize("hasRole('USER')")
    public String userMapa() {
        return "User/mapa";
    }

    @GetMapping("/admin/mapa")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminMapa() {
        return "Admin/mapa";
    }
}
