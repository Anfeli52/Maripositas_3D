package com.andres.mariposita3d.IService;

import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import com.andres.mariposita3d.Collection.Observacion;

public interface IObservacionService {
    List<Observacion> all();
    Optional<Observacion> findById(String id);
    Observacion save(Observacion observacion);
    void delete(String id);
    List<Observacion> findByUsuarioId(String usuarioId);
    List<Observacion> findByEspecieId(String especieId);
}

