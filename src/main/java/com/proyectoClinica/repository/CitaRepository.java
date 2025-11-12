package com.proyectoClinica.repository;

import com.proyectoClinica.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


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


}


