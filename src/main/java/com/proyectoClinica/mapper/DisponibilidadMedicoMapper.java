package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.DisponibilidadMedicoRequestDTO;
import com.proyectoClinica.dto.response.DisponibilidadMedicoResponseDTO;
import com.proyectoClinica.model.DisponibilidadMedico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MedicoMapper.class})
public interface DisponibilidadMedicoMapper {

    DisponibilidadMedicoResponseDTO toDTO (DisponibilidadMedico disponibilidadMedico);

    List<DisponibilidadMedicoResponseDTO> toDTOList (List<DisponibilidadMedico> disponibilidadMedicos);

    @Mapping(target = "medico", ignore = true)
    @Mapping(target = "duracionMinutos", ignore = true)
    @Mapping(target = "idDisponibilidad", ignore = true)
    DisponibilidadMedico toEntity (DisponibilidadMedicoRequestDTO dto);
}
