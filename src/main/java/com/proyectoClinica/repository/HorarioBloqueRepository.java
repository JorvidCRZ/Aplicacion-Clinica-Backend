package com.proyectoClinica.repository;

import com.proyectoClinica.model.HorarioBloque;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HorarioBloqueRepository extends JpaRepository<HorarioBloque, Integer> {

    @Query("""
    SELECT hb
    FROM HorarioBloque hb
    JOIN hb.disponibilidadMedico d
    WHERE d.medico.idMedico = :idMedico
      AND hb.disponible = TRUE
    ORDER BY hb.fecha, hb.horaInicio
    """)
    List<HorarioBloque> findHorariosDisponiblesPorMedico(@Param("idMedico") Integer idMedico);

//    List<HorarioBloque> findByDisponibilidadMedico_IdMedicoEspecialidadAndFechaAndDisponibleTrueOrderByHoraInicio(
//            Integer idMedicoEspecialidad,
//            LocalDate fecha
//    );

    @Query(value = """
   SELECT\s
                                                       MIN(dm.id_disponibilidad) AS id,
                                                       CONCAT(p.nombre1, ' ', p.apellido_paterno, ' ', p.apellido_materno) AS medico,
                                                       e.nombre AS especialidad,
                                                       STRING_AGG(DISTINCT dm.dia_semana, ', ' ORDER BY dm.dia_semana) AS dias,
                                                       TO_CHAR(MIN(dm.hora_inicio), 'HH24:MI:SS') AS horaInicio,
                                                       TO_CHAR(MAX(dm.hora_fin), 'HH24:MI:SS') AS horaFin,
                                                       MIN(dm.duracion_minutos) AS duracion,
                                                       SUM(
                                                           FLOOR(
                                                               EXTRACT(EPOCH FROM (dm.hora_fin - dm.hora_inicio)) / 60
                                                               / dm.duracion_minutos
                                                           )
                                                       )::INTEGER AS bloques,
                                                       CASE\s
                                                           WHEN SUM(CASE WHEN hb.disponible = TRUE THEN 1 ELSE 0 END) > 0 THEN 'Disponible'
                                                           ELSE 'No disponible'
                                                       END AS estado
                                                   FROM disponibilidad_medico dm
                                                   JOIN medico m ON dm.id_medico = m.id_medico
                                                   JOIN persona p ON m.id_persona = p.id_persona
                                                   LEFT JOIN medico_especialidad me ON me.id_medico = m.id_medico
                                                   LEFT JOIN especialidad e ON e.id_especialidad = me.id_especialidad
                                                   LEFT JOIN horario_bloque hb ON hb.id_disponibilidad = dm.id_disponibilidad
                                                   GROUP BY p.nombre1, p.apellido_paterno, p.apellido_materno, e.nombre
                                                   ORDER BY medico;
""", nativeQuery = true)
    List<Object[]> listarDisponibilidadesAgrupadas();



    @Query("""
    SELECT hb
    FROM HorarioBloque hb
    JOIN hb.disponibilidadMedico d
    WHERE d.medico.idMedico = :idMedico
    ORDER BY hb.fecha, hb.horaInicio
""")
    List<HorarioBloque> findByMedicoIdOrderByFechaHora(@Param("idMedico") Integer idMedico);


    @Modifying
    @Transactional
    @Query("""
    UPDATE HorarioBloque hb
    SET hb.disponible = :estado
    WHERE hb.disponibilidadMedico.medico.idMedico = :idMedico
""")
    int actualizarDisponibilidadPorMedico(
            @Param("idMedico") Integer idMedico,
            @Param("estado") boolean estado);



}
