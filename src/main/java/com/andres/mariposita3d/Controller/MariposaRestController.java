package com.andres.mariposita3d.Controller;

import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import com.andres.mariposita3d.Service.EspecieMariposaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/mariposas")
public class MariposaRestController {

    private final EspecieMariposaService especieMariposaService;

    public MariposaRestController(EspecieMariposaService especieMariposaService) {
        this.especieMariposaService = especieMariposaService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MariposaDetalleDTO getButterflyDetails(@PathVariable String id){

        MariposaDetalleDTO details = especieMariposaService.findDetailsById(id);
        if (details == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mariposa no encontrada.");
        }
        return details;
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateButterfly(@RequestBody MariposaDetalleDTO editedData) {
        try {
            especieMariposaService.updateMariposa(editedData);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor al actualizar.");
        }
    }

}