package com.proyectoClinica.repository;

import com.proyectoClinica.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {

    /*@Query(value = """
    SELECT
        TO_CHAR(p.created_at, 'YYYY-MM') AS mes,
        COUNT(p.id_paciente) AS nuevos_registros,
        COALESCE(AVG(c.total_citas), 0) AS citas_promedio,
        COALESCE(AVG(h.total_tratamientos), 0) AS frecuencia_tratamientos
    FROM paciente p
    LEFT JOIN (
        SELECT id_paciente, COUNT(*) AS total_citas
        FROM cita
        GROUP BY id_paciente
    ) c ON p.id_paciente = c.id_paciente
    LEFT JOIN (
        SELECT id_paciente, COUNT(*) AS total_tratamientos
        FROM historial_medico
        GROUP BY id_paciente
    ) h ON p.id_paciente = h.id_paciente
    WHERE EXTRACT(YEAR FROM p.created_at) = EXTRACT(YEAR FROM NOW())
    GROUP BY mes
    ORDER BY mes
""", nativeQuery = true)
    List<Map<String, Object>> estadisticasPacientes();*/

}
