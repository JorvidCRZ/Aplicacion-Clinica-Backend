package com.proyectoClinica.repository;

import com.proyectoClinica.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "reporte")
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
}
