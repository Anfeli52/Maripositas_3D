package com.andres.mariposita3d.DTO;

import java.util.Date;
import lombok.Data;

@Data
public class ObservacionDTO {
    private String id;
    private String usuarioNombre;
    private String especieNombre;
    private String comentario;
    private Date fecha;
}
