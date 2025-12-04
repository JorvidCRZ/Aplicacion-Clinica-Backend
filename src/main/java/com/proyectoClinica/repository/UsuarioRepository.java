package com.proyectoClinica.repository;

import com.proyectoClinica.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	java.util.Optional<Usuario> findByPersona_IdPersona(Integer idPersona);
    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findByRolNombre(String administrador);
}