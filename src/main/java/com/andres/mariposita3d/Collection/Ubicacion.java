package com.andres.mariposita3d.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("ubicaciones")
public class Ubicacion {

    @Id
    private String id;

    @Field("localidad")
    private String localidad;

    @Field("municipio")
    private String municipio;

    @Field("departamento")
    private String departamento;

    @Field("pais")
    private String pais;

    // Documento anidado legible { latitud, longitud }
    @Field("geolocalizacion")
    private GeoLatLon geolocalizacion;

    // GeoJSON para índices y consultas geoespaciales { type: "Point", coordinates: [lon, lat] }
    @Field("geolocalizacion_geojson")
    private GeoPoint geolocalizacionGeojson;

    @Field("ecosistema")
    private String ecosistema;

    public Ubicacion() {}

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public GeoLatLon getGeolocalizacion() {
        return geolocalizacion;
    }

    public void setGeolocalizacion(GeoLatLon geolocalizacion) {
        this.geolocalizacion = geolocalizacion;
    }

    public GeoPoint getGeolocalizacionGeojson() {
        return geolocalizacionGeojson;
    }

    public void setGeolocalizacionGeojson(GeoPoint geolocalizacionGeojson) {
        this.geolocalizacionGeojson = geolocalizacionGeojson;
    }

    public String getEcosistema() {
        return ecosistema;
    }

    public void setEcosistema(String ecosistema) {
        this.ecosistema = ecosistema;
    }

    // Clase anidada legible lat/lon
    public static class GeoLatLon {
        private double latitud;
        private double longitud;

        public GeoLatLon() {}

        public GeoLatLon(double latitud, double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

        public double getLatitud() {
            return latitud;
        }

        public void setLatitud(double latitud) {
            this.latitud = latitud;
        }

        public double getLongitud() {
            return longitud;
        }

        public void setLongitud(double longitud) {
            this.longitud = longitud;
        }
    }

    // Clase GeoJSON Point para indexado
    public static class GeoPoint {
        private String type = "Point";
        // coordinates: [lon, lat]
        private double[] coordinates;

        public GeoPoint() {}

        public GeoPoint(double lon, double lat) {
            this.coordinates = new double[] { lon, lat };
        }

        public String getType() {
            return type;
        }

        public double[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[] coordinates) {
            this.coordinates = coordinates;
        }
    }
}
