package com.proyectoClinica.repository;

import com.proyectoClinica.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "medico")
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
}
