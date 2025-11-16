package com.andres.mariposita3d.Controller;

import com.andres.mariposita3d.Service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final UsuarioService userService;

    public ViewController(UsuarioService userService) {this.userService = userService;}

    @GetMapping("/main")
    @PreAuthorize("hasRole('USER')")
    public String userMain(){
        return "User/userMainPage";
    }

    @GetMapping("/admin/main")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminMain(){
        return "Admin/adminMainPage";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String usersTable(Model model){
        model.addAttribute("users", userService.all());
        return "Admin/usersManagement";
    }
}
