package com.proyectoClinica.repository;

import com.proyectoClinica.model.DetalleCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "detalle-cita")
public interface DetalleCitaRepository extends JpaRepository<DetalleCita, Integer> {

}
