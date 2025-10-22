package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.DisponibilidadMedicoRequestDTO;
import com.proyectoClinica.dto.response.DisponibilidadMedicoResponseDTO;
import com.proyectoClinica.model.DisponibilidadMedico;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DisponibilidadMedicoMapper {

    DisponibilidadMedicoResponseDTO toDTO (DisponibilidadMedico disponibilidadMedico);

    List<DisponibilidadMedicoResponseDTO> toDTOList (List<DisponibilidadMedico> disponibilidadMedicos);

    DisponibilidadMedico toEntity (DisponibilidadMedicoRequestDTO dto);
}
