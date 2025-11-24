package com.proyectoClinica.repository;

import com.proyectoClinica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    boolean existsByPersonaIdPersona(Integer idPersona);
    Optional<Paciente> findByPersonaIdPersona(Integer idPersona);
    @Query(value = """
    SELECT 
        pa.id_paciente AS id,
        per.nombre1 || ' ' || COALESCE(per.nombre2, '') || ' ' || per.apellido_paterno || ' ' || per.apellido_materno AS nombreCompleto,
        u.correo AS email,
        per.telefono AS telefono,
        per.tipo_documento AS tipoDocumento,
        per.dni AS numeroDocumento,
        per.fecha_nac AS fechaNacimiento,
        per.genero AS genero
    FROM paciente pa
    JOIN persona per ON pa.id_persona = per.id_persona
    LEFT JOIN usuario u ON u.id_persona = per.id_persona
    ORDER BY pa.id_paciente
    """, nativeQuery = true)
    List<Map<String, Object>> listarPacientesDetalle();




    /*mETODO PARA LLAMAR PACIENTES EN TABLA PACIENTES DASHBOARD MEDICO*/
    @Query(value = "SELECT * FROM vista_pacientes_dashboard WHERE id_medico = :idMedico", nativeQuery = true)
    List<Map<String, Object>> listarPacientesDashboardPorMedico(@Param("idMedico") Integer idMedico);


    /*Calcular puntualidad del paciente*/
    @Query(value = """
    SELECT 
        ROUND(
            (COUNT(*) FILTER (WHERE c.estado = 'completada')::numeric /
             NULLIF(COUNT(*) FILTER (WHERE c.estado IN ('completada','atendida')),0)
            ) * 100, 2
        ) AS puntualidad
    FROM cita c
    JOIN detalle_cita dc ON c.id_detalle_cita = dc.id_detalle_cita
    JOIN medico_especialidad me ON dc.id_medico_especialidad = me.id_medico_especialidad
    WHERE me.id_medico = :idMedico
    """, nativeQuery = true)
    Double obtenerPuntualidadPorMedico(@Param("idMedico") Integer idMedico);

    /*Caluclar satisfaccion segun citas completadas*/
    @Query(value = """
    SELECT 
        ROUND(
            (COUNT(*) FILTER (WHERE c.estado = 'completada')::numeric /
             NULLIF(COUNT(*), 0)
            ) * 100, 2
        ) AS satisfaccion
    FROM cita c
    JOIN detalle_cita dc ON c.id_detalle_cita = dc.id_detalle_cita
    JOIN medico_especialidad me ON dc.id_medico_especialidad = me.id_medico_especialidad
    WHERE me.id_medico = :idMedico
    """, nativeQuery = true)
    Double obtenerSatisfaccionPorMedico(@Param("idMedico") Integer idMedico);

}
