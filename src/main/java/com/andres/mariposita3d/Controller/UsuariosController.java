package com.andres.mariposita3d.Controller;

import com.andres.mariposita3d.Collection.Usuario;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    public String usersTable(Model model, Authentication authentication){
        Usuario currentUser = (Usuario) authentication.getPrincipal();

        String currentUserId = currentUser.getId().toString();

        model.addAttribute("users", userService.allExcluding(currentUserId));
        return "Admin/usersManagement";
    }
}

