package com.proyectoClinica.repository;

import com.proyectoClinica.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "rol")
public interface RolRepository extends JpaRepository<Rol, Integer> {
}
