package com.proyectoClinica.repository;

import com.proyectoClinica.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
}
