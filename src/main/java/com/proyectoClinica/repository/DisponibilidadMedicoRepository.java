package com.proyectoClinica.repository;

import com.proyectoClinica.model.DisponibilidadMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadMedicoRepository extends JpaRepository<DisponibilidadMedico, Integer> {

    // Queries using the nested Medico property. Medico entity uses field 'idMedico',
    // so we must traverse to medico.idMedico in derived query method names.
    List<DisponibilidadMedico> findByMedico_IdMedico(Integer idMedico);

    List<DisponibilidadMedico> findByMedico_IdMedicoAndDiaSemana(Integer idMedico, String diaSemana);

    // todos vigentes
    List<DisponibilidadMedico> findByMedico_IdMedicoAndVigenciaTrue(Integer idMedico);

    // vigentes de un día
    List<DisponibilidadMedico> findByMedico_IdMedicoAndDiaSemanaAndVigenciaTrue(Integer idMedico, String diaSemana);



}
