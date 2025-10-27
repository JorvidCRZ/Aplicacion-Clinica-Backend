package com.proyectoClinica.repository;

import com.proyectoClinica.model.HistorialAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialAccesoRepository extends JpaRepository<HistorialAcceso, Integer> {
}

