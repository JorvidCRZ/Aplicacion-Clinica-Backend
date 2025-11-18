package com.proyectoClinica.repository;

import com.proyectoClinica.model.SubEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubEspecialidadRepository extends JpaRepository<SubEspecialidad, Integer> {

    List<SubEspecialidad> findByEspecialidad_IdEspecialidad(Integer idEspecialidad);
}
