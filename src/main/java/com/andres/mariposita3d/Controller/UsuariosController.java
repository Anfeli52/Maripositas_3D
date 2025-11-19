package com.andres.mariposita3d.Controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.andres.mariposita3d.Service.UsuarioService;

@CrossOrigin(origins = "*")
@Controller
public class UsuariosController {
    private final UsuarioService userService;
    public UsuariosController(UsuarioService userService) {this.userService = userService;}

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String usersTable(Model model){
        model.addAttribute("users", userService.all());
        return "Admin/usersManagement";
    }
}

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
class UsuariosRestController {
    private final UsuarioService userService;
    public UsuariosRestController(UsuarioService userService) {this.userService = userService;}

    @GetMapping
    public java.util.List<com.andres.mariposita3d.Collection.Usuario> all() {
        return userService.all();
    }

    @GetMapping("/{id}")
    public Optional<com.andres.mariposita3d.Collection.Usuario> show(@PathVariable String id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public com.andres.mariposita3d.Collection.Usuario update(@PathVariable String id, @RequestBody com.andres.mariposita3d.Collection.Usuario usuario) {
        Optional<com.andres.mariposita3d.Collection.Usuario> op = userService.findById(id);
        if (op.isPresent()) {
            com.andres.mariposita3d.Collection.Usuario usuarioUpdate = op.get();
            usuarioUpdate.setNombre(usuario.getNombre());
            usuarioUpdate.setCorreo(usuario.getCorreo());
            usuarioUpdate.setRol(usuario.getRol());
            usuarioUpdate.setContrasena(usuario.getContrasena());
            usuarioUpdate.setFechaRegistro(usuario.getFechaRegistro());
            return userService.save(usuarioUpdate);
        }
        return usuario;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }
}
