package com.proyectoClinica.repository;

import com.proyectoClinica.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "especialidad")
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
}
