package com.proyectoClinica.repository;

import com.proyectoClinica.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    @Query(value = """
        SELECT 
            m.id_medico AS id,
            p.nombre1 || ' ' || COALESCE(p.nombre2, '') AS nombre,
            p.apellido_paterno AS apellidoPaterno,
            p.apellido_materno AS apellidoMaterno,
            u.correo AS email,
            STRING_AGG(e.nombre, ', ') AS especialidades,
            m.colegiatura AS colegiatura,
            p.telefono AS telefono,
            STRING_AGG(
                d.dia_semana || ' (' || TO_CHAR(d.hora_inicio, 'HH24:MI') || '-' || TO_CHAR(d.hora_fin, 'HH24:MI') || ')',
                ', '
            ) AS horario
        FROM medico m
        JOIN persona p ON m.id_persona = p.id_persona
        LEFT JOIN usuario u ON m.id_usuario = u.id_usuario
        LEFT JOIN medico_especialidad me ON m.id_medico = me.id_medico
        LEFT JOIN especialidad e ON me.id_especialidad = e.id_especialidad
        LEFT JOIN disponibilidad_medico d ON m.id_medico = d.id_medico
        GROUP BY m.id_medico, p.nombre1, p.nombre2, p.apellido_paterno, p.apellido_materno, u.correo, m.colegiatura, p.telefono
        ORDER BY p.apellido_paterno
        """, nativeQuery = true)
    List<Map<String, Object>> listarMedicosDetalle();
    Optional<Medico> findByUsuarioIdUsuario(Integer idUsuario);

    /*-------------------------------*/
    @Query(value = """
    SELECT 
        m.id_medico AS idMedico,
        p.id_persona AS idPersona,
        p.nombre1,
        p.nombre2,
        p.apellido_paterno AS apellidoPaterno,
        p.apellido_materno AS apellidoMaterno,
        p.dni,
        TO_CHAR(p.fecha_nac, 'YYYY-MM-DD') AS fechaNacimiento,
        p.genero,
        p.telefono,
        p.direccion,
        u.correo,
        m.colegiatura,
        e.nombre AS especialidad
    FROM medico m
    INNER JOIN persona p ON p.id_persona = m.id_persona
    LEFT JOIN usuario u ON u.id_usuario = m.id_usuario
    LEFT JOIN medico_especialidad me ON me.id_medico = m.id_medico
    LEFT JOIN especialidad e ON e.id_especialidad = me.id_especialidad
    WHERE m.id_medico = :idMedico
    """, nativeQuery = true)
    List<Map<String,Object>> listarPerfilDashboardPorMedico(@Param("idMedico") Integer idMedico);
/*-------------------------------*/
    @Modifying
    @Query(value = """
    UPDATE medico_especialidad 
    SET id_especialidad = (
        SELECT id_especialidad 
        FROM especialidad 
        WHERE nombre = :nombreEspecialidad
    )
    WHERE id_medico = :idMedico
    """, nativeQuery = true)
    void actualizarEspecialidad(
            @Param("idMedico") Integer idMedico,
            @Param("nombreEspecialidad") String nombreEspecialidad);


}
