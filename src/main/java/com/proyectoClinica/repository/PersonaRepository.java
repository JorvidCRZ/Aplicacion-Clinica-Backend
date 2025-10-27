package com.proyectoClinica.repository;

import com.proyectoClinica.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "persona")
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
}
