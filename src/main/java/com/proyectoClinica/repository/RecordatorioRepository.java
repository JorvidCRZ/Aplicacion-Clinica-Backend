package com.proyectoClinica.repository;

import com.proyectoClinica.model.Recordatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Integer> {
	java.util.List<Recordatorio> findByCita_Paciente_Persona_IdPersonaAndDestinatarioCorreoIsNull(Integer idPersona);
}
