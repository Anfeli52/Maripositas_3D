package com.andres.mariposita3d.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MariposaMapaDTO {

    private String nombreCientifico;
    private String nombreComun;
    private String familia;
    private String localidad;
    private String municipio;
    private String departamento;

    private Double lat;
    private Double lon;
}
