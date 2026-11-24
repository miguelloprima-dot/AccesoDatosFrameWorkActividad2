package com.example.actividad2.repositorios;

import com.example.actividad2.entities.Averia;
import com.example.actividad2.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

}
