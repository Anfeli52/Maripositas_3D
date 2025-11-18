package com.andres.mariposita3d.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andres.mariposita3d.Collection.Observacion;
import com.andres.mariposita3d.Repository.ObservacionRepository;
import com.andres.mariposita3d.IService.IObservacionService;

@Service
public class ObservacionService implements IObservacionService {

    @Autowired
    private ObservacionRepository repository;

    @Override
    public List<Observacion> all() {
        return repository.findAll();
    }

    @Override
    public Optional<Observacion> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Observacion save(Observacion observacion) {
        if (observacion.getFecha() == null) {
            observacion.setFecha(new Date());
        }
        return repository.save(observacion);
    }

    @Override
    public void delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    @Override
    public List<Observacion> findByUsuarioId(String usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Observacion> findByEspecieId(String especieId) {
        return repository.findByEspecieId(especieId);
    }
}
