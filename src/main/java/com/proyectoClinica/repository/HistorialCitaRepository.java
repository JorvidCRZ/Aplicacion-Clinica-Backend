package com.proyectoClinica.repository;

import com.proyectoClinica.model.HistorialCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "historial-cita")
public interface HistorialCitaRepository extends JpaRepository<HistorialCita, Integer> {
}
