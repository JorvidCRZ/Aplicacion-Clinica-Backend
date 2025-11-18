package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.DetalleCitaRequestDTO;
import com.proyectoClinica.dto.response.DetalleCitaResponseDTO;
import com.proyectoClinica.model.DetalleCita;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetalleCitaMapper {

    DetalleCitaResponseDTO toDTO (DetalleCita detalleCita);

    List<DetalleCitaResponseDTO> toDTOList (List<DetalleCita> detalleCitas);

    DetalleCita toEntity (DetalleCitaRequestDTO dto);
}
