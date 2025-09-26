package com.proyectoClinica.repository;

import com.proyectoClinica.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "usuario")
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}