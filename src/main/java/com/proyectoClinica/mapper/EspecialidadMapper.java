package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.EspecialidadRequestDTO;
import com.proyectoClinica.dto.response.EspecialidadResponseDTO;
import com.proyectoClinica.model.Especialidad;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EspecialidadMapper {

    EspecialidadResponseDTO toDTO(Especialidad especialidad);

    List<EspecialidadResponseDTO> toDTOList(List<Especialidad> especialidades);

    Especialidad toEntity(EspecialidadRequestDTO dto);
}
