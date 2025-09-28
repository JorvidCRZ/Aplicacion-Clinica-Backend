package com.proyectoClinica.repository;

import com.proyectoClinica.model.SubEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "sub-especialidad")
public interface SubEspecialidadRepository extends JpaRepository<SubEspecialidad, Integer> {
}
