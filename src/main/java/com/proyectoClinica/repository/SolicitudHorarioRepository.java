package com.proyectoClinica.repository;

import com.proyectoClinica.model.Medico;
import com.proyectoClinica.model.SolicitudHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SolicitudHorarioRepository extends JpaRepository<SolicitudHorario, Long> {

    List<SolicitudHorario> findByEstado(String estado);


    List<SolicitudHorario> findByMedicoAndFecha(Medico medico, LocalDate fecha);

    List<SolicitudHorario> findByMedicoOrderByFechaSolicitudDesc(Medico medico);
}