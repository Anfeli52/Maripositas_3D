package com.andres.mariposita3d.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.andres.mariposita3d.Collection.Usuario;

public interface IUsuarioRepository extends MongoRepository<Usuario, String> {

}
