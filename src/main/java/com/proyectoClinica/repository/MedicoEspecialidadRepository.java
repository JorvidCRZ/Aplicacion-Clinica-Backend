package com.proyectoClinica.repository;

import com.proyectoClinica.model.MedicoEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoEspecialidadRepository extends JpaRepository<MedicoEspecialidad, Integer> {
    List<MedicoEspecialidad> findByMedico_IdMedico(Integer idMedico);


    @Query("""
    SELECT me.idMedicoEspecialidad
    FROM MedicoEspecialidad me
    JOIN me.medico m
    JOIN m.persona p
    JOIN me.especialidad e
    WHERE CONCAT(p.nombre1, ' ', p.apellidoPaterno) = :medicoNombre
      AND e.nombre = :especialidad
""")
    Optional<Integer> findIdByNombreMedicoYEspecialidad(@Param("medicoNombre") String medicoNombre,
                                                        @Param("especialidad") String especialidad);
}
