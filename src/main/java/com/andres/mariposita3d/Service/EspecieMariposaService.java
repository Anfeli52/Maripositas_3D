package com.andres.mariposita3d.Service;

import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecieMariposaService {

    private final MongoTemplate mongoTemplate;

    public EspecieMariposaService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
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
                .and("imagenes").as("imagenes")
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

}
