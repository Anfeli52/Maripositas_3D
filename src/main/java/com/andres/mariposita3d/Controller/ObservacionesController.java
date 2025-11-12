package com.andres.mariposita3d.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.andres.mariposita3d.Collection.Observacion;
import com.andres.mariposita3d.IService.IObservacionService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/observaciones")
public class ObservacionesController {

    @Autowired
    private IObservacionService service;

    @GetMapping
    public List<Observacion> all() {
        return service.all();
    }

    @GetMapping("{id}")
    public Optional<Observacion> show(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Observacion save(@RequestBody Observacion observacion) {
        return service.save(observacion);
    }

    @PutMapping("{id}")
    public Observacion update(@PathVariable String id, @RequestBody Observacion observacion) {
        Optional<Observacion> op = service.findById(id);
        if (op.isPresent()) {
            Observacion o = op.get();
            o.setEspecieId(observacion.getEspecieId());
            o.setUsuarioId(observacion.getUsuarioId());
            o.setComentario(observacion.getComentario());
            o.setFecha(observacion.getFecha());
            return service.save(o);
        }
        // Si no existe, puedes decidir crear o devolver el objeto recibido; aquí devolvemos el recibido tras guardarlo
        observacion.setId(id);
        return service.save(observacion);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Observacion> byUsuario(@PathVariable String usuarioId) {
        return service.findByUsuarioId(usuarioId);
    }

    @GetMapping("/especie/{especieId}")
    public List<Observacion> byEspecie(@PathVariable String especieId) {
        return service.findByEspecieId(especieId);
    }
}
