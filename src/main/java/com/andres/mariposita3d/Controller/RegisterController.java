package com.andres.mariposita3d.Controller;

import com.andres.mariposita3d.Collection.Usuario;
import com.andres.mariposita3d.Service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class RegisterController {

    private final UsuarioService usuarioService;

    public RegisterController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam("nombre") String nombre,
                                 @RequestParam("correo") String correo,
                                 @RequestParam("contrasena") String contrasena,
                                 Model model) {
        try {
            Usuario nuevo = new Usuario(null, nombre, correo, "USER", contrasena, new Date());
            usuarioService.save(nuevo);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo registrar el usuario.");
            return "register";
        }
    }
}
