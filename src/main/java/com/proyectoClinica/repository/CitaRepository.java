package com.proyectoClinica.repository;

import com.proyectoClinica.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "cita")
public interface CitaRepository extends JpaRepository<Cita, Integer> {
}
