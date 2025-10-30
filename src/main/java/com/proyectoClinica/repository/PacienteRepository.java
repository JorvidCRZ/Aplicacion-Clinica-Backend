package com.proyectoClinica.repository;

import com.proyectoClinica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    boolean existsByPersonaIdPersona(Integer idPersona);
    Optional<Paciente> findByPersonaIdPersona(Integer idPersona);
}
