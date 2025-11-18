package com.andres.mariposita3d.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.andres.mariposita3d.Collection.Observacion;
import com.andres.mariposita3d.Collection.Usuario;
import com.andres.mariposita3d.Service.ObservacionService;

// Nuevo import
import org.bson.types.ObjectId;

@Controller
public class ObservacionesController {

    private final ObservacionService ObservacionService;

    public ObservacionesController(ObservacionService ObservacionService) {
        this.ObservacionService = ObservacionService;
    }

    @GetMapping("/observacion/nueva/{especieId}")
    @PreAuthorize("hasRole('USER')")
    public String showObservationForm(@PathVariable String especieId,
                                      @RequestParam(name = "nombre", required = false) String nombre,
                                      Model model) {
        model.addAttribute("especieId", especieId);
        model.addAttribute("especieNombre", nombre);
        return "User/nuevaObservacion";
    }

    @PostMapping("/observacion/nueva")
    @PreAuthorize("hasRole('USER')")
    public String handleObservation(@RequestParam("especieId") ObjectId especieId,
                                    @RequestParam("comentario") String comentario,
                                    @AuthenticationPrincipal Usuario usuario) {
        // Convertir el id del usuario a ObjectId antes de crear la observación
        ObjectId usuarioObjectId = new ObjectId(usuario.getId());
        Observacion observacion = new Observacion(especieId, usuarioObjectId, comentario, null);
        ObservacionService.save(observacion);
        return "redirect:/main?observacion=creada";
    }

}