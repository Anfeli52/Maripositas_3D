package com.andres.mariposita3d.Controller;

import com.andres.mariposita3d.Collection.Observacion;
import com.andres.mariposita3d.Collection.Usuario;
import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import com.andres.mariposita3d.Service.EspecieMariposaService;
import com.andres.mariposita3d.Service.ObservacionService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ViewController {

    private final EspecieMariposaService butterflyService;
    private final ObservacionService observacionService;

    public ViewController(EspecieMariposaService butterflyService, ObservacionService observacionService) {
        this.butterflyService = butterflyService;
        this.observacionService = observacionService;
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
