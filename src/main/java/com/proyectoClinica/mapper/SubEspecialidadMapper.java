package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.SubEspecialidadRequestDTO;
import com.proyectoClinica.dto.response.SubEspecialidadResponseDTO;
import com.proyectoClinica.model.SubEspecialidad;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { EspecialidadMapper.class })
public interface SubEspecialidadMapper {

    SubEspecialidadResponseDTO toDTO(SubEspecialidad subEspecialidad);

    List<SubEspecialidadResponseDTO> toDTOList(List<SubEspecialidad> subEspecialidades);

    SubEspecialidad toEntity(SubEspecialidadRequestDTO dto);
}
