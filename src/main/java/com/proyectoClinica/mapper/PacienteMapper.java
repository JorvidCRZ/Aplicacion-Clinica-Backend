package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.response.PacienteResponseDTO;
import com.proyectoClinica.model.Paciente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    PacienteResponseDTO toDTO (Paciente paciente);

    List<PacienteResponseDTO> toDTOList (List<Paciente> pacientes);

    Paciente toEntity (PacienteRequestDTO dto);
}
