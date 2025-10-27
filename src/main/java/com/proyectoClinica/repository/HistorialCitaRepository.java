package com.proyectoClinica.repository;

import com.proyectoClinica.model.HistorialCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialCitaRepository extends JpaRepository<HistorialCita, Integer> {
}
