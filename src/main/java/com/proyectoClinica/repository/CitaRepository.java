package com.proyectoClinica.repository;

import com.proyectoClinica.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "cita")
public interface CitaRepository extends JpaRepository<Cita, Integer> {
	boolean existsByDisponibilidad_IdDisponibilidadAndFechaCitaAndHoraCita(Integer idDisponibilidad, java.time.LocalDate fechaCita, java.time.LocalTime horaCita);

	java.util.List<Cita> findByDisponibilidad_IdDisponibilidadAndFechaCita(Integer idDisponibilidad, java.time.LocalDate fechaCita);
}
