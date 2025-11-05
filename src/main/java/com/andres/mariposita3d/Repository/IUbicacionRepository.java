package com.andres.mariposita3d.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.andres.mariposita3d.Collection.Ubicacion;

public interface IUbicacionRepository extends MongoRepository<Ubicacion, String> {

}
