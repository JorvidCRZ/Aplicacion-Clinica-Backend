package com.proyectoClinica.repository;

import com.proyectoClinica.model.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer> {
    List<HistorialMedico> findByPacienteIdPaciente(Integer idPaciente);

    List<HistorialMedico> findByMedicoIdMedico(Integer idMedico);

    List<HistorialMedico> findByPacienteIdPacienteAndMedicoIdMedico(Integer idPaciente, Integer idMedico);
}
