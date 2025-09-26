package com.proyectoClinica.repository;

import com.proyectoClinica.model.AuditoriaAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "auditoria-accion")
public interface AuditoriaAccionRepository extends JpaRepository<AuditoriaAccion, Integer> {
}
