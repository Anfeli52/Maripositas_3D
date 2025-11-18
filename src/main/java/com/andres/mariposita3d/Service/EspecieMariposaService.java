package com.andres.mariposita3d.Service;

import com.andres.mariposita3d.Collection.EspecieMariposa;
import com.andres.mariposita3d.Collection.Ubicacion;
import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import com.andres.mariposita3d.Repository.EspecieMariposaRepository;
import com.andres.mariposita3d.Repository.UbicacionRepository;
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

    public EspecieMariposaService(MongoTemplate mongoTemplate, UbicacionRepository ubicacionRepository, EspecieMariposaRepository especieMariposaRepository) {
        this.mongoTemplate = mongoTemplate;
        this.ubicacionRepository = ubicacionRepository;
        this.especieMariposaRepository = especieMariposaRepository;
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

        updateEspecieMariposaData(data);
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

        especieMariposaRepository.save(especie);
    }

    private void updateUbicacionData(MariposaDetalleDTO data) {
        Ubicacion ubicacion = ubicacionRepository.findById(
                data.getUbicacionRecoleccionId()).orElseThrow(() -> new NoSuchElementException("Ubicación asociada no encontrada con ID: " + data.getUbicacionRecoleccionId())
        );

        ubicacion.setDepartamento(data.getDepartamento());
        ubicacion.setMunicipio(data.getMunicipio());
        ubicacion.setLocalidad(data.getLocalidad());

        ubicacionRepository.save(ubicacion);
    }

}
