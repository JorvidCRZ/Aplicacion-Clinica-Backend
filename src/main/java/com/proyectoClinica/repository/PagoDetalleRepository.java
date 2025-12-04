package com.proyectoClinica.repository;

import com.proyectoClinica.model.PagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoDetalleRepository extends JpaRepository<PagoDetalle, Integer> {

    @Query("SELECT pd FROM PagoDetalle pd " +
            "JOIN FETCH pd.pago p " +
            "JOIN FETCH pd.cita c " +
            "ORDER BY p.fechaPago DESC")
    List<PagoDetalle> findAllForReport();
}
