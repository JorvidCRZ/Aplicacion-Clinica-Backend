package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.DisponibilidadMedicoRequestDTO;
import com.proyectoClinica.dto.response.DisponibilidadMedicoResponseDTO;
import com.proyectoClinica.model.DisponibilidadMedico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MedicoMapper.class})
public interface DisponibilidadMedicoMapper {

    // Entity -> ResponseDTO
    DisponibilidadMedicoResponseDTO toDTO(DisponibilidadMedico disponibilidadMedico);

    List<DisponibilidadMedicoResponseDTO> toDTOList(List<DisponibilidadMedico> disponibilidadMedicos);

    // RequestDTO -> Entity
    @Mapping(target = "idDisponibilidad", ignore = true) // autogenerado
    @Mapping(target = "duracionMinutos", ignore = true)  // no viene en el dto
    @Mapping(target = "medico.idMedico", source = "idMedico") // clave: mapea idMedico a medico.idMedico
    DisponibilidadMedico toEntity(DisponibilidadMedicoRequestDTO dto);
}
