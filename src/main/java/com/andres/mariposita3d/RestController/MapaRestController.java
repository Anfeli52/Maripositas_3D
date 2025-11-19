package com.andres.mariposita3d.RestController;

import com.andres.mariposita3d.DTO.MariposaMapaDTO;
import com.andres.mariposita3d.Service.EspecieMariposaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mapa")
public class MapaRestController {

    private final EspecieMariposaService especieMariposaService;

    public MapaRestController(EspecieMariposaService especieMariposaService) {
        this.especieMariposaService = especieMariposaService;
    }

    @GetMapping("/mariposas")
    public List<MariposaMapaDTO> getMariposasForMap() {
        return especieMariposaService.findAllForMap();
    }
}