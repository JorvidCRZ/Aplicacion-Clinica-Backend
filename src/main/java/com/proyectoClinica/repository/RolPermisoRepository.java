package com.proyectoClinica.repository;

import com.proyectoClinica.model.RolPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "rol-permiso")
public interface RolPermisoRepository extends JpaRepository<RolPermiso, Integer> {
}
