package com.proyectoClinica.repository;

import com.proyectoClinica.model.HorarioDisponible;
import com.proyectoClinica.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Long>{
    List<HorarioDisponible> findByMedicoAndFechaAndDisponibleTrue(
            @Param("medico") Medico medico,
            @Param("fecha") LocalDate fecha);

    List<HorarioDisponible> findByMedicoAndFecha(
            @Param("medico") Medico medico,
            @Param("fecha") LocalDate fecha);

    @Query("SELECT h FROM HorarioDisponible h WHERE h.medico = :medico AND h.fecha BETWEEN :startDate AND :endDate AND h.disponible = true")
    List<HorarioDisponible> findByMedicoAndFechaBetweenAndDisponibleTrue(
            @Param("medico") Medico medico,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<HorarioDisponible> findByMedico(Medico medico);

    @Query("SELECT h FROM HorarioDisponible h WHERE h.medico.idMedico = :medicoId AND h.fecha = :fecha AND h.disponible = true")
    List<HorarioDisponible> findByMedicoIdAndFechaAndDisponibleTrue(
            @Param("medicoId") Integer medicoId,
            @Param("fecha") LocalDate fecha);
}

