package com.proyectoClinica.repository;

import com.proyectoClinica.model.HistorialAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "historial-acceso")
public interface HistorialAccesoRepository extends JpaRepository<HistorialAcceso, Integer> {
}

