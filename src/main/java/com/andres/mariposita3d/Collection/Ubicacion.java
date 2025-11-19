package com.andres.mariposita3d.Collection;

import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
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

    @Field("geolocalizacion")
    private GeoLatLon geolocalizacion;

    @Field("geolocalizacion_geojson")
    private GeoPoint geolocalizacionGeojson;

    @Field("ecosistema")
    private String ecosistema;

    public Ubicacion() {}

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

    public static class GeoPoint {
        private String type = "Point";
        @Setter
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

    }
}
