package com.proyectoClinica.repository;

import com.proyectoClinica.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


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
        me.id_medico
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
    WHERE me.id_medico = :idMedico
    ORDER BY c.fecha_cita, c.hora_cita
""", nativeQuery = true)
    List<Map<String, Object>> listarCitasDashboardPorMedico(@Param("idMedico") Integer idMedico);


}


