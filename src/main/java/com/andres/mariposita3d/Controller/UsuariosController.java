package com.andres.mariposita3d.Controller;

import com.andres.mariposita3d.Service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
@CrossOrigin(origins = "*")
@Controller
//@RequestMapping("api/usuario")
public class UsuariosController {

//    @Autowired
//    private IUsuarioService service;

    private final UsuarioService userService;
    public UsuariosController(UsuarioService userService) {this.userService = userService;}

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String usersTable(Model model){
        model.addAttribute("users", userService.all());
        return "Admin/usersManagement";
    }

//    @GetMapping
//    public List<Usuario> all() {
//        return service.all();
//    }
//
//    @GetMapping("{id}")
//    public Optional<Usuario> show(@PathVariable String id) {
//        return service.findById(id);
//    }
//
//    @PostMapping
//    @ResponseStatus(code = HttpStatus.CREATED)
//    public Usuario save(@RequestBody Usuario usuario) {
//        return service.save(usuario);
//    }
//
//    @PutMapping("{id}")
//    @ResponseStatus(code = HttpStatus.CREATED)
//    public Usuario update(@PathVariable String id, @RequestBody Usuario usuario) {
//        Optional<Usuario> op = service.findById(id);
//
//        if (op.isPresent()) {
//            Usuario usuarioUpdate = op.get();
//            usuarioUpdate.setNombre(usuario.getNombre());
//            usuarioUpdate.setCorreo(usuario.getCorreo());
//            usuarioUpdate.setRol(usuario.getRol());
//            usuarioUpdate.setContrasena(usuario.getContrasena());
//            usuarioUpdate.setFechaRegistro(usuario.getFechaRegistro());
//            return service.save(usuarioUpdate);
//        }
//
//        return usuario;
//    }
//
//    @DeleteMapping("{id}")
//    @ResponseStatus(code = HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable String id) {
//        service.delete(id);
//    }
}
