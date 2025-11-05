package com.andres.mariposita3d.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andres.mariposita3d.Collection.Ubicacion;
import com.andres.mariposita3d.Repository.IUbicacionRepository;
import com.andres.mariposita3d.IService.IUbicacionService;

@Service
public class UbicacionService implements IUbicacionService {

    @Autowired
    private IUbicacionRepository repository;

    @Override
    public List<Ubicacion> all() {
        return repository.findAll();
    }

    @Override
    public Optional<Ubicacion> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Ubicacion save(Ubicacion ubicacion) {
        // Si no viene el campo GeoJSON, y sí viene lat/lon, construimos el GeoPoint
        if ((ubicacion.getGeolocalizacionGeojson() == null
                || ubicacion.getGeolocalizacionGeojson().getCoordinates() == null)
                && ubicacion.getGeolocalizacion() != null) {

            Double lat = ubicacion.getGeolocalizacion().getLatitud();
            Double lon = ubicacion.getGeolocalizacion().getLongitud();

            if (lat != null && lon != null) {
                ubicacion.setGeolocalizacionGeojson(new Ubicacion.GeoPoint(lon, lat));
            }
        }

        return repository.save(ubicacion);
    }

    @Override
    public void delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
}
