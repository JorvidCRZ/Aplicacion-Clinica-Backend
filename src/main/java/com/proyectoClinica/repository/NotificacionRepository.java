package com.proyectoClinica.repository;

import com.proyectoClinica.model.Notificacion;
import com.proyectoClinica.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository  extends JpaRepository<Notificacion, Long>{
    List<Notificacion> findByUsuarioDestinoAndActivaTrueOrderByFechaCreacionDesc(Usuario usuario);

    List<Notificacion> findByUsuarioDestinoAndLeidaFalseAndActivaTrueOrderByFechaCreacionDesc(Usuario usuario);

    List<Notificacion> findByTipoAndActivaTrueOrderByFechaCreacionDesc(String tipo);

    Long countByUsuarioDestinoAndLeidaFalseAndActivaTrue(Usuario usuario);
    Optional<Notificacion> findById(Long id);
}
