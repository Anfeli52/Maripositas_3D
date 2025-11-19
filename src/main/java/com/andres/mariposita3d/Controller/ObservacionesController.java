package com.andres.mariposita3d.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.andres.mariposita3d.Collection.Observacion;
import com.andres.mariposita3d.Collection.Usuario;
import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import com.andres.mariposita3d.DTO.ObservacionDTO;
import com.andres.mariposita3d.Service.EspecieMariposaService;
import com.andres.mariposita3d.Service.ObservacionService;
import com.andres.mariposita3d.Service.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Nuevo import
import org.bson.types.ObjectId;

@Controller
public class ObservacionesController {

    private final ObservacionService observacionService;
    private final UsuarioService usuarioService;
    private final EspecieMariposaService butterflyService;

    public ObservacionesController(ObservacionService observacionService, UsuarioService usuarioService, EspecieMariposaService butterflyService) {
        this.observacionService = observacionService;
        this.usuarioService = usuarioService;
        this.butterflyService = butterflyService;
    }

     @GetMapping("/admin/observaciones")
    @PreAuthorize("hasRole('ADMIN')")
    public String observacionesManagement(Model model) {
        List<Observacion> observaciones = observacionService.all();
        
        List<ObservacionDTO> observacionDTOs = observaciones.stream()
            .map(obs -> {
                ObservacionDTO dto = new ObservacionDTO();
                dto.setId(obs.getId());
                dto.setComentario(obs.getComentario());
                dto.setFecha(obs.getFecha());
                
                // Obtener nombre del usuario
                Optional<Usuario> usuario = usuarioService.findById(obs.getUsuarioId().toString());
                dto.setUsuarioNombre(usuario.map(Usuario::getNombre).orElse("Usuario Desconocido"));
                
                // Obtener nombre de la especie
                MariposaDetalleDTO especie = butterflyService.findDetailsById(obs.getEspecieId().toString());
                dto.setEspecieNombre(especie != null ? especie.getNombreComun() : "Especie Desconocida");
                
                return dto;
            })
            .collect(Collectors.toList());
        
        model.addAttribute("observaciones", observacionDTOs);
        return "Admin/observacionesManagement";
    }

    @PostMapping("/admin/observacion/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public String rechazarObservacion(@PathVariable String id) {
        observacionService.delete(id);
        return "redirect:/admin/observaciones?rechazo=exitoso";
    }

    @PostMapping("/admin/observacion/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public String aprobarObservacion(@PathVariable String id) {
        return "redirect:/admin/observaciones?aprobacion=exitosa";
    }


    @GetMapping("/observacion/nueva/{especieId}")
    @PreAuthorize("hasRole('USER')")
    public String showObservationForm(@PathVariable String especieId,
                                      @RequestParam(name = "nombre", required = false) String nombre,
                                      Model model) {
        model.addAttribute("especieId", especieId);
        model.addAttribute("especieNombre", nombre);
        return "User/nuevaObservacion";
    }

    @PostMapping("/observacion/nueva")
    @PreAuthorize("hasRole('USER')")
    public String handleObservation(@RequestParam("especieId") ObjectId especieId,
                                    @RequestParam("comentario") String comentario,
                                    @AuthenticationPrincipal Usuario usuario) {
        // Convertir el id del usuario a ObjectId antes de crear la observación
        ObjectId usuarioObjectId = new ObjectId(usuario.getId());
        Observacion observacion = new Observacion(especieId, usuarioObjectId, comentario, null);
        observacionService.save(observacion);
        return "redirect:/main?observacion=creada";
    }

}