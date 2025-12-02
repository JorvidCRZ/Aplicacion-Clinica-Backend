package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.PersonaRequestDTO;
import com.proyectoClinica.dto.response.PersonaResponseDTO;
import com.proyectoClinica.model.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    PersonaResponseDTO toDTO(Persona persona);

    List<PersonaResponseDTO> toDTOList(List<Persona> personas);

    Persona toEntity(PersonaRequestDTO dto);

    void updateFromDTO(PersonaRequestDTO dto, @MappingTarget Persona entity);


}
