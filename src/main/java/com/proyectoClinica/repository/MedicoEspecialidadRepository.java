package com.proyectoClinica.repository;

import com.proyectoClinica.model.MedicoEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "medico-especialidad")
public interface MedicoEspecialidadRepository extends JpaRepository<MedicoEspecialidad, Integer> {
}
