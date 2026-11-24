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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")


public class VehiculosController {

    @Autowired
    VehiculoRepository repositorioVehiculos;
    @Autowired
    AveriaRepository repositorioAverias;
    //GET - Listar todos los vehiculos
    @GetMapping("/vehiculos")
    public ResponseEntity<List<Vehiculo>> findAllVehiculos(){
         return ResponseEntity.ok(repositorioVehiculos.findAll()); //200 ok + lista de vehiculos
    }

    //GET - Buscar vehiculo por ID
    @GetMapping("/vehiculos/{id}")
    public ResponseEntity<Vehiculo>findVehiculo(@PathVariable Long id){
        return repositorioVehiculos.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //Borrar un vehiculo
    @DeleteMapping("/vehiculos/{id}")
    public ResponseEntity<Object> deleteVehiculo(@PathVariable Long id) {
        if(!repositorioVehiculos.existsById(id)){   //Si no existe
            return ResponseEntity.notFound().build(); // 404
        }
        //Obtener todas las averias del vehiculo
        List<Averia> averias = repositorioAverias.findByVehiculo_Id(id);

        //Comprobamos si el vehiculos tiene alguna averia PENDIENTE o EN_REPARACION
        boolean tieneAveriasSinTerminar =false;
        for (Averia averia : averias){
            if(averia.getEstado()== EstadoReparacion.PENDIENTE || averia.getEstado() == EstadoReparacion.EN_REPARACION){
                tieneAveriasSinTerminar = true;
                break;
            }
        }
        //***********************************************
        if(tieneAveriasSinTerminar){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();//409
        }

       //Borrar averias (si las hay) t luego el vehiculo
       if(!averias.isEmpty()){
           repositorioAverias.deleteAll(averias);
       }

        repositorioVehiculos.deleteById(id);

        return ResponseEntity.notFound().build();
    }
    //POST - Crear un vehiculo
    @PostMapping("/vehiculos")
    public ResponseEntity<Vehiculo> createVehiculo(@RequestBody Vehiculo vehiculo) {
        Vehiculo guardado = repositorioVehiculos.save(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado); //201 POST CREADO
    }

    //PUT - Actualizar un vehiculo
    @PutMapping("/vehiculos/{id}")
    public ResponseEntity<Vehiculo> update(@RequestBody Vehiculo vehiculoNuevo, @PathVariable Long id) {
        Optional<Vehiculo> vehiculo = repositorioVehiculos.findById(id);
        if (vehiculo.isPresent()) {
            Vehiculo existente = vehiculo.get();
            existente.setMatricula(vehiculoNuevo.getMatricula());
            existente.setModelo(vehiculoNuevo.getModelo());
            existente.setAveriados(vehiculoNuevo.getAveriados());
            Vehiculo actualizado = repositorioVehiculos.save(existente);
            return  ResponseEntity.ok(actualizado); //200 Ok + vehiculo aztualizado
        } else {
            return ResponseEntity.notFound().build(); //404 Not Found si no existe
        }
    }
}
