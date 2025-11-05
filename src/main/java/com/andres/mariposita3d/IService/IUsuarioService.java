// IUsuarioService.java
package com.andres.mariposita3d.IService;

import java.util.List;
import java.util.Optional;

import com.andres.mariposita3d.Collection.Usuario;

public interface IUsuarioService {

    public List<Usuario> all();

    public Optional<Usuario> findById(String id);

    public Usuario save(Usuario usuario);

    public void delete(String id);
}
