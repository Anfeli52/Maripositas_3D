package com.andres.mariposita3d.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

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

