package com.example.actividad2.controladores;

import com.example.actividad2.entities.Averia;
import com.example.actividad2.entities.EstadoReparacion;
import com.example.actividad2.entities.Vehiculo;
import com.example.actividad2.repositorios.AveriaRepository;
import com.example.actividad2.repositorios.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")


public class AveriaController {
    @Autowired
    AveriaRepository repositorioAveria;
    @Autowired
    VehiculoRepository repositorioVehiculos;

    //GET - Buscar averias todas
    @GetMapping("/averias")
    public ResponseEntity<List<Averia>> findAllAveria(){
        return ResponseEntity.ok(repositorioAveria.findAll());//200 ok + lista de vehiculos
    }
    //GET - Buscar averia por ID
    @GetMapping("/averias/{id}")
    public ResponseEntity<Averia>findAveria(@PathVariable Long id){
        return repositorioAveria.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


   //Borrar averia por id
    @DeleteMapping("/averias/{id}")
    public ResponseEntity<Object> deleteVehiculo(@PathVariable Long id) {
      Optional<Averia> averiaOptional=repositorioAveria.findById(id);
        if(!repositorioAveria.existsById(id)){   //Si no existe
            return ResponseEntity.notFound().build(); // 404
        }

//Comprobamos si el vehiculos tiene alguna averia PENDIENTE o EN_REPARACION
        Averia averia = averiaOptional.get();
       Vehiculo vehiculo=averia.getVehiculo();
        if (vehiculo != null && vehiculo.getAveriados()!=null) {
           vehiculo.getAveriados().remove(averia);

        }
        return ResponseEntity.noContent().build();
    }


    // POST /api/
    @PostMapping("/averias")
    public ResponseEntity<Averia> crearAveria(@RequestBody Averia averia) {
        if (averia.getVehiculo() == null || averia.getVehiculo().getId() == null) {
            return ResponseEntity.badRequest().build();  //400 petición inválidad
        }
         //obtenemos el vehiculo con la id
        Optional<Vehiculo> vehiculoOpt = repositorioVehiculos.findById(averia.getVehiculo().getId());
        if (!vehiculoOpt.isPresent()) {
            return ResponseEntity.notFound().build();  // 404 recurso no encontrado
        }

        Vehiculo vehiculo = vehiculoOpt.get();
        averia.setVehiculo(vehiculo);
        if (averia.getEstado() == null) {
            averia.setEstado(EstadoReparacion.PENDIENTE);
        }
        //actualización de la fecha* * *  * * * * * * * * * * * *
        LocalDate fechaActual = LocalDate.now();
        averia.setFechaCreacion(fechaActual);
        averia.setFechaUltimaActualizacion(fechaActual);
        Averia guardada = repositorioAveria.save(averia);
        vehiculo.getAveriados().add(guardada);

        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);//201 avería creada
    }

    // PUT /api/averias/{id} -> 200, 404
    @PutMapping("/averias/{id}")
    public ResponseEntity<Averia> updateAveria(@PathVariable Long id, @RequestBody Averia averiaNueva) {
        Optional<Averia> averiaOpt = repositorioAveria.findById(id);
        //Comprobamos que existe la averia
        if (!averiaOpt.isPresent()) {
            return ResponseEntity.notFound().build(); //404 bo encontrado
        }


        if (averiaNueva.getEstado() != null) {
            averiaOpt.get().setEstado(averiaNueva.getEstado());
        }
        //Actualizamos la fecha
        //***************************************************
        LocalDate fechaActual = LocalDate.now();

        averiaOpt.get().setFechaUltimaActualizacion(fechaActual);
         //si es null el estado no midificado
        return ResponseEntity.ok(repositorioAveria.save(averiaOpt.get())); //200
    }
}
