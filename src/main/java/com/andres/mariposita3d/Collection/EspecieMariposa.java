package com.andres.mariposita3d.Collection;

import com.andres.mariposita3d.Collection.Embedded.CaracteristicaMorfo;
import com.andres.mariposita3d.Collection.Embedded.ImagenDetallada;
import com.andres.mariposita3d.Enum.TipoEspecie;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection="especieMariposa")
public class EspecieMariposa {

    @Id
    private String id;

    private String nombreCientifico;
    private String nombreComun;
    private String familia;
    private TipoEspecie tipoEspecie;
    private String descripcion;

    private List<String> imagenes;

    private ImagenDetallada imagenDetallada;
    private CaracteristicaMorfo caracteristicaMorfo;

    private String ubicacionRecoleccionId;
    private String registradoPorId;
    private Date fechaRegistro;


}
