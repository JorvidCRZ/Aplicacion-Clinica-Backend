package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.response.PacienteResponseDTO;
import com.proyectoClinica.model.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    PacienteResponseDTO toDTO (Paciente paciente);

    List<PacienteResponseDTO> toDTOList (List<Paciente> pacientes);
    @Mapping(target = "idPaciente", ignore = true)
    @Mapping(source = "idPersona", target = "persona.idPersona")
    @Mapping(source = "usuarioAgrego", target = "usuarioAgrego.idUsuario")
    Paciente toEntity (PacienteRequestDTO dto);
}
