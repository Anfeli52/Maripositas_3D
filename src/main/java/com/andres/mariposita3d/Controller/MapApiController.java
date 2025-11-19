package com.andres.mariposita3d.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.andres.mariposita3d.Collection.Ubicacion;
import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import com.andres.mariposita3d.Service.EspecieMariposaService;
import com.andres.mariposita3d.Repository.UbicacionRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mapa")
public class MapApiController {

    private final EspecieMariposaService butterflyService;
    private final UbicacionRepository ubicacionRepository;

    public MapApiController(EspecieMariposaService butterflyService, UbicacionRepository ubicacionRepository) {
        this.butterflyService = butterflyService;
        this.ubicacionRepository = ubicacionRepository;
    }

    @GetMapping("/mariposas")
    public List<Map<String, Object>> getMariposasLocations() {
        List<MariposaDetalleDTO> list = butterflyService.findAllWithUbicationDetails();
        List<Map<String, Object>> out = new ArrayList<>();
        
        System.out.println("=== INICIANDO BÚSQUEDA DE MARIPOSAS ===");
        System.out.println("Total de mariposas en BD: " + list.size());
        
        for (MariposaDetalleDTO d : list) {
            String ubicId = d.getUbicacionRecoleccionId();
            System.out.println("\nMariposa: " + d.getNombreComun() + " | UbicacionId: " + ubicId);
            
            if (ubicId != null && !ubicId.isEmpty()) {
                Optional<Ubicacion> op = ubicacionRepository.findById(ubicId);
                
                if (op.isPresent()) {
                    Ubicacion u = op.get();
                    Double lat = null, lon = null;
                    
                    System.out.println("  Ubicación encontrada");
                    
                    // Intentar primero con geolocalizacion_geojson
                    try {
                        if (u.getGeolocalizacionGeojson() != null && 
                            u.getGeolocalizacionGeojson().getCoordinates() != null &&
                            u.getGeolocalizacionGeojson().getCoordinates().length >= 2) {
                            
                            double[] coords = u.getGeolocalizacionGeojson().getCoordinates();
                            lon = coords[0];
                            lat = coords[1];
                            System.out.println("    ✓ Coordenadas de GeoJSON: lat=" + lat + ", lon=" + lon);
                        }
                    } catch (Exception e) {
                        System.out.println("    ✗ Error leyendo GeoJSON: " + e.getMessage());
                    }
                    
                    // Si no obtuvo coordenadas, intentar con geolocalización
                    if ((lat == null || lon == null)) {
                        try {
                            if (u.getGeolocalizacion() != null) {
                                lat = u.getGeolocalizacion().getLatitud();
                                lon = u.getGeolocalizacion().getLongitud();
                                System.out.println("    ✓ Coordenadas de Geolocalización: lat=" + lat + ", lon=" + lon);
                            }
                        } catch (Exception e) {
                            System.out.println("    ✗ Error leyendo Geolocalización: " + e.getMessage());
                        }
                    }
                    
                    // Solo añadir si tiene coordenadas válidas
                    if (lat != null && lon != null && lat != 0.0 && lon != 0.0) {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", d.getId());
                        m.put("nombreComun", d.getNombreComun());
                        m.put("nombreCientifico", d.getNombreCientifico());
                        m.put("familia", d.getFamilia());
                        m.put("descripcion", d.getDescripcion());
                        m.put("departamento", d.getDepartamento());
                        m.put("municipio", d.getMunicipio());
                        m.put("localidad", d.getLocalidad());
                        m.put("lat", lat);
                        m.put("lon", lon);
                        out.add(m);
                        System.out.println("  ✓ Mariposa añadida al mapa");
                    } else {
                        System.out.println("  ✗ Coordenadas no válidas o nulas");
                    }
                } else {
                    System.out.println("  ✗ Ubicación NO encontrada en BD con id: " + ubicId);
                }
            } else {
                System.out.println("  ✗ Sin ubicacionRecoleccionId");
            }
        }
        
        System.out.println("\n=== RESULTADO FINAL ===");
        System.out.println("Total de mariposas con ubicación: " + out.size());
        System.out.println("Datos enviados al cliente: " + out);
        return out;
    }
}
