package com.andres.mariposita3d.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import com.andres.mariposita3d.Service.EspecieMariposaService;

@Controller
public class ViewController {

    private final EspecieMariposaService butterflyService;

    public ViewController(EspecieMariposaService butterflyService) {
        this.butterflyService = butterflyService;
    }

    @GetMapping("/main")
    @PreAuthorize("hasRole('USER')")
    public String userMain(Model model){
        List<MariposaDetalleDTO> butterflyDetails = butterflyService.findAllWithUbicationDetails();
        model.addAttribute("butterflies", butterflyDetails);
        return "User/userMainPage";
    }

    @GetMapping("/admin/main")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminMain(Model model){
        List<MariposaDetalleDTO> butterflyDetails = butterflyService.findAllWithUbicationDetails();
        model.addAttribute("butterflies", butterflyDetails);

        return "Admin/adminMainPage";
    }

    @GetMapping("/admin/registroMariposa")
    @PreAuthorize("hasRole('ADMIN')")
    public String registroMariposa(){
        return "Admin/registroMariposa";
    }

    @GetMapping("/user/mapa")
    @PreAuthorize("hasRole('USER')")
    public String userMapa() {
        return "User/mapa";
    }

    @GetMapping("/admin/mapa")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminMapa() {
        return "Admin/mapa";
    }
}
