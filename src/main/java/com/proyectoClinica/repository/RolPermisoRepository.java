package com.proyectoClinica.repository;

import com.proyectoClinica.model.RolPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, Integer> {
}
