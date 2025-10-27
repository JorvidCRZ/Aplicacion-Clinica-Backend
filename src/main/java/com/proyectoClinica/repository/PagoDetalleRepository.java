package com.proyectoClinica.repository;

import com.proyectoClinica.model.PagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoDetalleRepository extends JpaRepository<PagoDetalle, Integer> {
}
