package com.proyectoClinica.repository;
import com.proyectoClinica.model.CitaResumen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CitaResumenRepository extends JpaRepository<CitaResumen, Integer>{
}
