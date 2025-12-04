package com.proyectoClinica.repository;

import com.proyectoClinica.dto.response.ReporteRendimientoDoctorDTO;
import com.proyectoClinica.model.Cita;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
	boolean existsByDisponibilidad_IdDisponibilidadAndFechaCitaAndHoraCita(Integer idDisponibilidad, java.time.LocalDate fechaCita, java.time.LocalTime horaCita);
	java.util.List<Cita> findByDisponibilidad_IdDisponibilidadAndFechaCita(Integer idDisponibilidad, java.time.LocalDate fechaCita);
    List<Cita> findByFechaCita(LocalDate fechaCita);

    /*numero de citas de medico , numero de citas del medico en este mes*/

    @Query("""
    SELECT COUNT(c)
    FROM Cita c
    WHERE c.detalleCita.medicoEspecialidad.medico.idMedico = :idMedico
""")
    long contarCitasPorMedico(@Param("idMedico") Integer idMedico);

    @Query("""
    SELECT COUNT(c)
    FROM Cita c
    WHERE c.detalleCita.medicoEspecialidad.medico.idMedico = :idMedico
      AND c.fechaCita BETWEEN :inicio AND :fin
""")
    long countByMedicoIdAndFechaBetween(@Param("idMedico") Long idMedico,
                                        @Param("inicio") LocalDate inicio,
                                        @Param("fin") LocalDate fin);

    /*Citas por Dashboard Medico*/
    /*@Query(value = "SELECT * FROM vista_citas_por_medico WHERE id_medico = :idMedico", nativeQuery = true)
    List<Map<String, Object>> listarCitasDashboardPorMedico(@Param("idMedico") Integer idMedico);*/


    @Query(value = """
    SELECT
                                          c.fecha_cita AS fecha,
                                          c.hora_cita AS hora,
                                          CONCAT(
                                              per.nombre1, ' ',
                                              COALESCE(per.nombre2, ''), ' ',
                                              per.apellido_paterno, ' ',
                                              per.apellido_materno
                                          ) AS paciente,
                                          per.dni AS documento,
                                          per.telefono AS telefono,
                                          c.motivo_consulta,
                                          c.estado,
                                          me.id_medico,
                                          se.nombre AS subespecialidad,
                                          e.nombre AS especialidad
                                      FROM cita c
                                      INNER JOIN paciente p
                                          ON c.id_paciente = p.id_paciente
                                      INNER JOIN persona per
                                          ON p.id_persona = per.id_persona
                                      INNER JOIN detalle_cita dc
                                          ON dc.id_detalle_cita = c.id_detalle_cita
                                      INNER JOIN medico_especialidad meesp
                                          ON meesp.id_medico_especialidad = dc.id_medico_especialidad
                                      INNER JOIN medico me
                                          ON me.id_medico = meesp.id_medico
                                      LEFT JOIN sub_especialidad se
                                          ON dc.id_subespecialidad = se.id_subespecialidad
                                      LEFT JOIN especialidad e
                                          ON meesp.id_especialidad = e.id_especialidad
                                      WHERE me.id_medico = :idMedico
                                      ORDER BY c.fecha_cita, c.hora_cita;
                                      
""", nativeQuery = true)
    List<Map<String, Object>> listarCitasDashboardPorMedico(@Param("idMedico") Integer idMedico);

    // MODIFICACIÓN
// 1) RESUMEN POR ESTADO
    @Query(value = """
        SELECT
            c.estado AS estado,
            COUNT(*) AS cantidad
        FROM cita c
        WHERE c.fecha_cita BETWEEN :inicio AND :fin
        GROUP BY c.estado
        ORDER BY cantidad DESC
        """, nativeQuery = true)
    List<Map<String, Object>> contarCitasPorEstadoEntreFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin);


    // 2) TOP DOCTORES
    @Query(value = """
        SELECT
          CONCAT(pm.nombre1, ' ', COALESCE(pm.nombre2, ''), ' ', pm.apellido_paterno, ' ', pm.apellido_materno) AS nombre,
          esp.nombre AS especialidad,
          COUNT(*) AS total
        FROM cita c
        INNER JOIN detalle_cita dc ON c.id_detalle_cita = dc.id_detalle_cita
        INNER JOIN medico_especialidad meesp ON meesp.id_medico_especialidad = dc.id_medico_especialidad
        INNER JOIN medico me ON me.id_medico = meesp.id_medico
        INNER JOIN persona pm ON me.id_persona = pm.id_persona
        LEFT JOIN especialidad esp ON meesp.id_especialidad = esp.id_especialidad
        WHERE c.fecha_cita BETWEEN :inicio AND :fin
        GROUP BY me.id_medico,
                 pm.nombre1,
                 pm.nombre2,
                 pm.apellido_paterno,
                 pm.apellido_materno,
                 esp.nombre
        ORDER BY total DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Map<String, Object>> topDoctoresEntreFechas(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin,
            @Param("limit") int limit);

    // DESDE AQUÍ ES PARA LO QUE ESTAMOS TRABAJANDO CON RENDIMIENTO DE DOCTORES

    @Query("""
SELECT new com.proyectoClinica.dto.response.ReporteRendimientoDoctorDTO(
    CONCAT(p.nombre1, ' ', COALESCE(p.nombre2, ''), ' ',
           p.apellidoPaterno, ' ', p.apellidoMaterno),
    e.nombre,
    COUNT(c.idCita),
    SUM(CASE WHEN c.estado = 'Completada' THEN 1 ELSE 0 END),
    SUM(CASE WHEN c.estado = 'Cancelada' THEN 1 ELSE 0 END),
    SUM(CASE WHEN c.estado = 'Pendiente' THEN 1 ELSE 0 END),
    SUM(CASE WHEN c.estado = 'Completada' THEN dc.precioTotal ELSE 0 END)
)
FROM Cita c
JOIN c.detalleCita dc
JOIN dc.medicoEspecialidad meesp
JOIN meesp.medico m
JOIN m.persona p
JOIN meesp.especialidad e
GROUP BY p.nombre1, p.nombre2, p.apellidoPaterno, p.apellidoMaterno, e.nombre
ORDER BY COUNT(c.idCita) DESC
""")
    List<ReporteRendimientoDoctorDTO> reporteRendimientoDoctores();

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.detalleCita.medicoEspecialidad.medico.idMedico = :idMedico")
    long contarTotalCitas(@Param("idMedico") Integer idMedico);

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.estado = 'Completada' AND c.detalleCita.medicoEspecialidad.medico.idMedico = :idMedico")
    long contarCompletadas(@Param("idMedico") Integer idMedico);

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.estado = 'Cancelada' AND c.detalleCita.medicoEspecialidad.medico.idMedico = :idMedico")
    long contarCanceladas(@Param("idMedico") Integer idMedico);

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.estado = 'Pendiente' AND c.detalleCita.medicoEspecialidad.medico.idMedico = :idMedico")
    long contarPendientes(@Param("idMedico") Integer idMedico);

    @Query("SELECT SUM(dc.precioTotal) FROM Cita c JOIN c.detalleCita dc WHERE c.detalleCita.medicoEspecialidad.medico.idMedico = :idMedico AND c.estado = 'Completada'")
    BigDecimal calcularIngresos(@Param("idMedico") Integer idMedico);

    //------------------
    @Query("SELECT c FROM Cita c " +
            "JOIN FETCH c.paciente pac " +
            "JOIN FETCH pac.persona ppac " +
            "JOIN FETCH c.disponibilidad disp " +
            "JOIN FETCH disp.medico med " +
            "JOIN FETCH med.persona pmed " +
            "LEFT JOIN FETCH med.especialidades me " + // <--- ¿Estás usando LEFT JOIN aquí?
            "LEFT JOIN FETCH me.especialidad esp " +  // <--- ¿Y aquí?
            "ORDER BY c.fechaCita DESC, c.horaCita DESC")
    List<Cita> findAllForReport();

    /*--------------------------------------------------*/

        @Modifying
        @Transactional
        @Query("""
        UPDATE Cita c
        SET c.estado = 'no-show'
        WHERE (c.fechaCita < CURRENT_DATE
              OR (c.fechaCita = CURRENT_DATE AND c.horaCita < CURRENT_TIME))
        AND c.estado NOT IN ('COMPLETADA', 'CANCELADA', 'no-show')
    """)
        void updateCitasNoShow();

    @Modifying
    @Transactional
    @Query("""
    UPDATE Cita c
    SET c.estado = 'programada'
    WHERE (c.fechaCita > CURRENT_DATE
           OR (c.fechaCita = CURRENT_DATE AND c.horaCita > CURRENT_TIME))
      AND c.estado = 'no-show'
""")
    void revertirNoShowSiCitaSeMovio();

    

    @Query(value = """
    SELECT 
        ROUND(SUM(cr.duracion_minutos) / 60.0, 2) AS horas_totales,
        ROUND(AVG(cr.duracion_minutos), 0) AS promedio_minutos
    FROM cita_resumen cr
    INNER JOIN cita c ON c.id_cita = cr.id_cita
    INNER JOIN detalle_cita dc ON dc.id_detalle_cita = c.id_detalle_cita
    INNER JOIN medico_especialidad me ON me.id_medico_especialidad = dc.id_medico_especialidad
    WHERE me.id_medico = :idMedico
      AND c.estado = 'completada'
""", nativeQuery = true)
    Map<String, Object> obtenerHorasPromedioPorMedico(@Param("idMedico") Integer idMedico);

    Optional<Cita> findById(Integer id);


    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cita c " +
            "WHERE c.paciente.persona.idPersona = :idPaciente " +
            "AND c.fechaCita = :fecha " +
            "AND c.horaCita BETWEEN :horaInicio AND :horaFin")
    boolean existeCitaCercana(@Param("idPaciente") Integer idPaciente,
                              @Param("fecha") LocalDate fecha,
                              @Param("horaInicio") LocalTime horaInicio,
                              @Param("horaFin") LocalTime horaFin);


}


