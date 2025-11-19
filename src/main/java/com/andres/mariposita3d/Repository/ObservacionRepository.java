package com.andres.mariposita3d.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;
import com.andres.mariposita3d.Collection.Observacion;

public interface ObservacionRepository extends MongoRepository<Observacion, String> {
    List<Observacion> findByUsuarioId(ObjectId usuarioId);
    List<Observacion> findByEspecieId(String especieId);
}

