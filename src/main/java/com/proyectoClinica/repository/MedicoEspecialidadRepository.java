package com.proyectoClinica.repository;

import com.proyectoClinica.model.MedicoEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface MedicoEspecialidadRepository extends JpaRepository<MedicoEspecialidad, Integer> {
    List<MedicoEspecialidad> findByMedico_IdMedico(Integer idMedico);
}
