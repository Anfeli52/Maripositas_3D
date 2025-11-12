package com.andres.mariposita3d.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andres.mariposita3d.Collection.Usuario;
import com.andres.mariposita3d.Repository.IUsuarioRepository;
import com.andres.mariposita3d.IService.IUsuarioService;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository repository;

    @Override
    public List<Usuario> all() {
        return repository.findAll();
    }

    @Override
    public Optional<Usuario> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    public void delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
}
