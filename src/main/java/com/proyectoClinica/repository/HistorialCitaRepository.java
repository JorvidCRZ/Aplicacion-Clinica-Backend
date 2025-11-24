package com.proyectoClinica.repository;

import com.proyectoClinica.model.HistorialCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HistorialCitaRepository extends JpaRepository<HistorialCita, Integer> {

    @Query(value = """
    SELECT\s
                                                                                      hc.id_historial_cita,
                                                                                      hc.detalle,
                                                                                      hc.fecha AS fecha_historial,
                                                                                      per_paciente.nombre1 || ' ' || COALESCE(per_paciente.nombre2, '') || ' ' || per_paciente.apellido_paterno || ' ' || per_paciente.apellido_materno AS nombre_paciente
                                                                                  FROM historial_cita hc
                                                                                  JOIN cita c ON hc.id_cita = c.id_cita
                                                                                  JOIN detalle_cita dc ON c.id_detalle_cita = dc.id_detalle_cita
                                                                                  JOIN medico_especialidad me ON dc.id_medico_especialidad = me.id_medico_especialidad
                                                                                  JOIN medico m ON me.id_medico = m.id_medico
                                                                                  JOIN paciente p ON c.id_paciente = p.id_paciente
                                                                                  JOIN persona per_paciente ON p.id_persona = per_paciente.id_persona
                                                                                  WHERE m.id_medico = 1  -- <--- reemplaza 1 por el id del médico deseado
                                                                                  ORDER BY hc.fecha DESC;
                                                                                  
                                                                         
                                      
""", nativeQuery = true)
    List<Map<String, Object>> listarHistorialCitasDashboardPorMedico(@Param("idMedico") Integer idMedico);

}
