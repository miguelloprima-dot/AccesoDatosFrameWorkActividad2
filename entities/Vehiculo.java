package com.example.actividad2.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="vehiculos")
public class Vehiculo {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY ) //Esto es para que id sea autonumerico
    private Long id;
     private String matricula;
     private String modelo;
     private LocalDate fechaMatricula;
/*Al usar CascadeType.PERSIST y no poner cascada de borrado (REMOVE o ALL),
*  al intentar borrar un vehículo no se eliminan automáticamente sus averías,
            * y la base de datos impide borrar el vehículo si sigue teniendo averías asociadas
* hasta que se gestionen o eliminen antes.
 */
    @JsonManagedReference
     @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.PERSIST)
      private List<Averia> averiados = new ArrayList<>();
}
