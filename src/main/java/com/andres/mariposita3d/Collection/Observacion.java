package com.andres.mariposita3d.Collection;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("observaciones")
public class Observacion {

    @Id
    private String id;

    @Field("especie_id")
    private String especieId;

    @Field("usuario_id")
    private String usuarioId;

    @Field("comentario")
    private String comentario;

    @Field("fecha")
    private Date fecha;

    public Observacion() {}

    public Observacion(String especieId, String usuarioId, String comentario, Date fecha) {
        this.especieId = especieId;
        this.usuarioId = usuarioId;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEspecieId() {
        return especieId;
    }

    public void setEspecieId(String especieId) {
        this.especieId = especieId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
