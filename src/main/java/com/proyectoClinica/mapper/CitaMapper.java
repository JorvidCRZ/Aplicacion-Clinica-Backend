package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.CitaRequestDTO;
import com.proyectoClinica.dto.response.CitaResponseDTO;
import com.proyectoClinica.model.Cita;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CitaMapper {

    CitaResponseDTO toDTO (Cita cita);

    List<CitaResponseDTO> toDTOList (List<Cita> citas);

    Cita toEntity (CitaRequestDTO dto);
}
