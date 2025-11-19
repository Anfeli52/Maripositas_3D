package com.andres.mariposita3d.Service;

import com.andres.mariposita3d.Collection.EspecieMariposa;
import com.andres.mariposita3d.Collection.Ubicacion;
import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import com.andres.mariposita3d.DTO.MariposaMapaDTO;
import com.andres.mariposita3d.Repository.EspecieMariposaRepository;
import com.andres.mariposita3d.Repository.ObservacionRepository;
import com.andres.mariposita3d.Repository.UbicacionRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EspecieMariposaService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    private EspecieMariposaRepository especieMariposaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private ObservacionRepository observacionRepository;

    public EspecieMariposaService(MongoTemplate mongoTemplate, UbicacionRepository ubicacionRepository, EspecieMariposaRepository especieMariposaRepository, ObservacionRepository observacionRepository) {
        this.mongoTemplate = mongoTemplate;
        this.ubicacionRepository = ubicacionRepository;
        this.especieMariposaRepository = especieMariposaRepository;
        this.observacionRepository = observacionRepository;
    }

    public List<MariposaDetalleDTO> findAllWithUbicationDetails() {

        LookupOperation lookup = Aggregation.lookup(
                "ubicaciones",
                "ubicacionRecoleccionId",
                "_id",
                "detallesUbicacion"
        );

        UnwindOperation unwind = Aggregation.unwind("detallesUbicacion");
        ProjectionOperation projection = new ProjectionOperation()
                .and("id").as("id")
                .and("nombreCientifico").as("nombreCientifico")
                .and("nombreComun").as("nombreComun")
                .and("familia").as("familia")
                .and("descripcion").as("descripcion")
//                .and("imagenes").as("imagenes")
                .and("detallesUbicacion.departamento").as("departamento")
                .and("detallesUbicacion.municipio").as("municipio")
                .and("detallesUbicacion.localidad").as("localidad");

        Aggregation aggregation = Aggregation.newAggregation(lookup, unwind, projection);
        AggregationResults<MariposaDetalleDTO> results = mongoTemplate.aggregate(
                aggregation,
                "especieMariposa",
                MariposaDetalleDTO.class
        );

        return results.getMappedResults();
    }

    public MariposaDetalleDTO findDetailsById(String id){
        MatchOperation match = Aggregation.match(Criteria.where("_id").is(id));

        LookupOperation lookup = Aggregation.lookup(
                "ubicaciones",
                "ubicacionRecoleccionId",
                "_id",
                "detallesUbicacion"
        );
        UnwindOperation unwind = Aggregation.unwind("detallesUbicacion", true);

        ProjectionOperation projection = Aggregation.project()
                .and("id").as("id")
                .and("ubicacionRecoleccionId").as("ubicacionRecoleccionId")
                .and("nombreCientifico").as("nombreCientifico")
                .and("nombreComun").as("nombreComun")
                .and("familia").as("familia")
                .and("descripcion").as("descripcion")

                .and("detallesUbicacion.departamento").as("departamento")
                .and("detallesUbicacion.municipio").as("municipio")
                .and("detallesUbicacion.localidad").as("localidad");

        Aggregation aggregation = Aggregation.newAggregation(match, lookup, unwind, projection);
        AggregationResults<MariposaDetalleDTO> results = mongoTemplate.aggregate(
                aggregation,
                "especieMariposa",
                MariposaDetalleDTO.class
        );

        return results.getUniqueMappedResult();
    }

    public void updateMariposa(MariposaDetalleDTO data) {
        if(data.getId() == null || data.getUbicacionRecoleccionId() == null){
            throw new IllegalArgumentException("IDs de Especie o Ubicación faltantes.");
        }

        System.out.println("Estoy aquí con esta información: "+data.toString());

        updateEspecieMariposaData(data);
        System.out.println("Pasé el primer método :D");
        updateUbicacionData(data);
    }

    private void updateEspecieMariposaData(MariposaDetalleDTO data) {
        EspecieMariposa especie = especieMariposaRepository.findById(
                data.getId()).orElseThrow(() -> new NoSuchElementException("Especie Mariposa no encontrada con ID: " + data.getId())
        );

        especie.setNombreCientifico(data.getNombreCientifico());
        especie.setNombreComun(data.getNombreComun());
        especie.setFamilia(data.getFamilia());
        especie.setDescripcion(data.getDescripcion());

        System.out.println("Soy yo nuevamente, esto funciona aquí, por ahora, información: "+data.toString());

        try {
            especieMariposaRepository.save(especie);
        } catch (Exception e) {
            System.err.println("!!! ERROR FATAL AL GUARDAR LA ESPECIE MARIPOSA !!!");
            e.printStackTrace();
            throw e;
        }

    }

    private void updateUbicacionData(MariposaDetalleDTO data) {

        System.out.println("Funciono?");

        Ubicacion ubicacion = ubicacionRepository.findById(
                data.getUbicacionRecoleccionId()).orElseThrow(() -> new NoSuchElementException("Ubicación asociada no encontrada con ID: " + data.getUbicacionRecoleccionId())
        );

        ubicacion.setDepartamento(data.getDepartamento());
        ubicacion.setMunicipio(data.getMunicipio());
        ubicacion.setLocalidad(data.getLocalidad());

        System.out.println("Soy yo nueva nuevamente, esto funciona otra vez hasta aquí, por ahora, creo, información: "+data.toString());

        ubicacionRepository.save(ubicacion);
    }

    public void deleteEspecieConObservaciones(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID requerido.");
        }
        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("ID inválido.");
        }
        especieMariposaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Especie no encontrada."));
        observacionRepository.deleteByEspecieId(id);
        especieMariposaRepository.deleteById(id);
    }

    public void deleteEspecieById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID requerido.");
        }
        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("ID inválido.");
        }
        
        especieMariposaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Especie no encontrada."));
        long count = observacionRepository.countByEspecieId(id);
        if (count > 0) {
            throw new IllegalStateException(
                "No se puede eliminar: existen " + count + " observaciones asociadas."
            );
        }
        
        especieMariposaRepository.deleteById(id);
    }

    public List<MariposaMapaDTO> findAllForMap() {
        LookupOperation lookup = Aggregation.lookup(
                "ubicaciones",
                "ubicacionRecoleccionId",
                "_id",
                "detallesUbicacion"
        );

        UnwindOperation unwind = Aggregation.unwind("detallesUbicacion");
        ProjectionOperation projection = Aggregation.project()
                .and("nombreCientifico").as("nombreCientifico")
                .and("nombreComun").as("nombreComun")
                .and("familia").as("familia")
                .and("detallesUbicacion.localidad").as("localidad")
                .and("detallesUbicacion.municipio").as("municipio")
                .and("detallesUbicacion.departamento").as("departamento")

                .and("detallesUbicacion.geolocalizacion.latitud").as("lat")
                .and("detallesUbicacion.geolocalizacion.longitud").as("lon");

        Aggregation aggregation = Aggregation.newAggregation(lookup, unwind, projection);

        AggregationResults<MariposaMapaDTO> results = mongoTemplate.aggregate(
                aggregation,
                "especieMariposa",
                MariposaMapaDTO.class
        );

        return results.getMappedResults();
    }

}
