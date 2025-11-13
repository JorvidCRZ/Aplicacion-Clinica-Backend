package com.proyectoClinica.repository;

import com.proyectoClinica.model.HorarioBloque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioBloqueRepository extends JpaRepository<HorarioBloque, Integer> {
}
