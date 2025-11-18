package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.response.MedicoEspecialidadResponseDTO;
import com.proyectoClinica.model.MedicoEspecialidad;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicoEspecialidadMapper {

    MedicoEspecialidadResponseDTO toDTO (MedicoEspecialidad medicoEspecialidad);

    List<MedicoEspecialidadResponseDTO> toDTOList (List<MedicoEspecialidad> medicoEspecialidades);

    MedicoEspecialidad toEntity (MedicoEspecialidad dto);
}
