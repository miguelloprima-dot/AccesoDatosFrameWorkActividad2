package com.example.actividad2.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Averia")
public class Averia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY ) //Esto es para que id sea autonumerico
    private Long id;
    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoReparacion estado;

    @Column(nullable = false, updatable = false)
   private LocalDate fechaCreacion;

    @Column(nullable = false, updatable = false)
    private LocalDate fechaUltimaActualizacion;



    @ManyToOne
    @JoinColumn(name ="id_fk_vehiculo", nullable = false)

    @JsonBackReference
    private Vehiculo vehiculo;  //mismo nombre que en mappedBy
}
