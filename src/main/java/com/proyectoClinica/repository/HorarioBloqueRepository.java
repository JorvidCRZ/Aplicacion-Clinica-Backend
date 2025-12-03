package com.proyectoClinica.repository;

import com.proyectoClinica.model.HorarioBloque;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
