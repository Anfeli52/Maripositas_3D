package com.andres.mariposita3d.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.andres.mariposita3d.Collection.Ubicacion;
import com.andres.mariposita3d.IService.IUbicacionService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/ubicaciones")
public class UbicacionesController {

    @Autowired
    private IUbicacionService service;

    @GetMapping
    public List<Ubicacion> all() {
        return service.all();
    }

    @GetMapping("{id}")
    public Optional<Ubicacion> show(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Ubicacion save(@RequestBody Ubicacion ubicacion) {
        // Si la petición trae solo geolocalizacion {latitud,longitud} y no geojson,
        // puedes construir el campo geojson antes de guardar (ejemplo simple):
        if (ubicacion.getGeolocalizacionGeojson() == null && ubicacion.getGeolocalizacion() != null) {
            double lat = ubicacion.getGeolocalizacion().getLatitud();
            double lon = ubicacion.getGeolocalizacion().getLongitud();
            ubicacion.setGeolocalizacionGeojson(new Ubicacion.GeoPoint(lon, lat));
        }
        return service.save(ubicacion);
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Ubicacion update(@PathVariable String id, @RequestBody Ubicacion ubicacion) {
        Optional<Ubicacion> op = service.findById(id);
        if (op.isPresent()) {
            Ubicacion u = op.get();
            u.setLocalidad(ubicacion.getLocalidad());
            u.setMunicipio(ubicacion.getMunicipio());
            u.setDepartamento(ubicacion.getDepartamento());
            u.setPais(ubicacion.getPais());
            u.setEcosistema(ubicacion.getEcosistema());
            u.setGeolocalizacion(ubicacion.getGeolocalizacion());
            // actualizar o generar geojson a partir de lat/lon
            if (ubicacion.getGeolocalizacionGeojson() != null) {
                u.setGeolocalizacionGeojson(ubicacion.getGeolocalizacionGeojson());
            } else if (ubicacion.getGeolocalizacion() != null) {
                double lat = ubicacion.getGeolocalizacion().getLatitud();
                double lon = ubicacion.getGeolocalizacion().getLongitud();
                u.setGeolocalizacionGeojson(new Ubicacion.GeoPoint(lon, lat));
            }
            return service.save(u);
        }
        return ubicacion;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
