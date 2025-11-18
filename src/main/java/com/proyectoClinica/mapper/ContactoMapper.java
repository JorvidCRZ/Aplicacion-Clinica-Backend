package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.ContactoRequestDTO;
import com.proyectoClinica.dto.response.ContactoResponseDTO;
import com.proyectoClinica.model.Contacto;
import org.mapstruct.Mapper;

import java.util.List;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactoMapper {

    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    ContactoResponseDTO toDTO(Contacto contacto);

    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    List<ContactoResponseDTO> toDTOList(List<Contacto> contactos);

    // When creating from DTO we don't map idContacto/fecha/usuario directly here; service will attach usuario if provided
    @Mapping(target = "idContacto", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Contacto toEntity(ContactoRequestDTO dto);
}
