package com.andres.mariposita3d.RestController;

import com.andres.mariposita3d.Collection.Usuario;
import com.andres.mariposita3d.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosRestController {
    private final UsuarioService userService;

    public UsuariosRestController(UsuarioService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Optional<Usuario> show(@PathVariable String id) {
        return userService.findById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Usuario editedUser) {
        try {
            Optional<Usuario> existingUserOp = userService.findById(id);
            if (existingUserOp.isEmpty()) {
                throw new NoSuchElementException("Usuario no encontrado con ID: " + id);
            }

            Usuario existingUser = existingUserOp.get();

            if (editedUser.getNombre() != null) {
                existingUser.setNombre(editedUser.getNombre());
            }
            if (editedUser.getCorreo() != null) {
                existingUser.setCorreo(editedUser.getCorreo());
            }
            if (editedUser.getRol() != null) {
                existingUser.setRol(editedUser.getRol());
            }
            userService.save(existingUser);

            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            System.err.println("Error en la actualización de usuario: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor al actualizar.");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }
}
