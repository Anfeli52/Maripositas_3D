package com.andres.mariposita3d.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.andres.mariposita3d.Collection.Usuario;
import com.andres.mariposita3d.IService.IUsuarioService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/usuario")
public class UsuarioController {

    @Autowired
    private IUsuarioService service;

    @GetMapping
    public List<Usuario> all() {
        return service.all();
    }

    @GetMapping("{id}")
    public Optional<Usuario> show(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Usuario save(@RequestBody Usuario usuario) {
        return service.save(usuario);
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Usuario update(@PathVariable String id, @RequestBody Usuario usuario) {
        Optional<Usuario> op = service.findById(id);

        if (op.isPresent()) {
            Usuario usuarioUpdate = op.get();
            usuarioUpdate.setNombre(usuario.getNombre());
            usuarioUpdate.setCorreo(usuario.getCorreo());
            usuarioUpdate.setRol(usuario.getRol());
            usuarioUpdate.setContrasena(usuario.getContrasena());
            usuarioUpdate.setFechaRegistro(usuario.getFechaRegistro());
            return service.save(usuarioUpdate);
        }

        return usuario;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
