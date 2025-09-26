package com.proyectoClinica.repository;

import com.proyectoClinica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "paciente")
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
}
