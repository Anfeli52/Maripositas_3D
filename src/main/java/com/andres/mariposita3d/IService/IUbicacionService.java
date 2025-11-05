// IUbicacionService.java
package com.andres.mariposita3d.IService;

import java.util.List;
import java.util.Optional;
import com.andres.mariposita3d.Collection.Ubicacion;

public interface IUbicacionService {
    public List<Ubicacion> all();
    public Optional<Ubicacion> findById(String id);
    public Ubicacion save(Ubicacion ubicacion);
    public void delete(String id);
}
