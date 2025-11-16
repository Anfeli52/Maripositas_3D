package com.andres.mariposita3d.Repository;

import com.andres.mariposita3d.DTO.MariposaDetalleDTO;
import java.util.List;

public interface EspecieMariposaRepositoryCustom {
    List<MariposaDetalleDTO> findAllWithUbicationDetails();
}
