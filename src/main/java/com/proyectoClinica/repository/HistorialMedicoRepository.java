package com.proyectoClinica.repository;

import com.proyectoClinica.model.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "historial-medico")
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer> {
}
