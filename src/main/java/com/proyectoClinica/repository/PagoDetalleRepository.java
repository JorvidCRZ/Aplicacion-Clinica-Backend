package com.proyectoClinica.repository;

import com.proyectoClinica.model.PagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "pago-detalle")
public interface PagoDetalleRepository extends JpaRepository<PagoDetalle, Integer> {
}
