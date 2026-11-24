package com.example.actividad2.repositorios;

import com.example.actividad2.entities.Averia;
import com.example.actividad2.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AveriaRepository extends JpaRepository<Averia, Long> {
    List<Averia> findByVehiculo_Id(Long id);
}
