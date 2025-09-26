package com.proyectoClinica.repository;

import com.proyectoClinica.model.DisponibilidadMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "disponibilidad-medico")
public interface DisponibilidadMedicoRepository extends JpaRepository<DisponibilidadMedico, Integer> {
}
