package com.proyectoClinica.repository;

import com.proyectoClinica.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar usuario por ID de persona (para editar/actualizar)
    Optional<Usuario> findByPersona_IdPersona(Integer idPersona);

    // Buscar usuario por correo (para validar duplicados)
    Optional<Usuario> findByCorreo(String correo);
}
