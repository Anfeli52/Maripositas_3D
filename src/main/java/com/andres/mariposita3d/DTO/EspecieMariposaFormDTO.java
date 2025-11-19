package com.andres.mariposita3d.DTO;

import com.andres.mariposita3d.Collection.Ubicacion;
import com.andres.mariposita3d.Collection.Embedded.CaracteristicaMorfo;
import com.andres.mariposita3d.Collection.Embedded.ImagenDetallada;

import lombok.Data;

import java.util.List;

import org.bson.types.ObjectId;

@Data
public class EspecieMariposaFormDTO {

    private String nombreCientifico;
    private String nombreComun;
    private String familia;
    private String tipoEspecie;
    private String descripcion;

    private CaracteristicaMorfo caracteristicasMorfo;
    private ImagenDetallada imagenesDetalladas;

    private List<String> imagenes;
    private ObjectId ubicacionRecoleccionId;

    private Ubicacion nuevaUbicacion;
    private boolean guardarNuevaUbicacion;

}