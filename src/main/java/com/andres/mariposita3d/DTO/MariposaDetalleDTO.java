package com.andres.mariposita3d.DTO;

import lombok.Data;

import java.util.List;

@Data
public class MariposaDetalleDTO {

    private String id;
    private String nombreCientifico;
    private String nombreComun;
    private String familia;
    private String descripcion;
    private List<String> imagenes;

    // Campos de Ubicacion (Obtenidos del $lookup)
    // El $lookup devuelve un array, pero si sabemos que solo hay una ubicación,
    // podemos tomar el primer elemento. Es más fácil si definimos los campos directamente:
    private String departamento;
    private String municipio;
    private String localidad;

}
