package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.RolRequestDTO;
import com.proyectoClinica.dto.response.RolResponseDTO;
import com.proyectoClinica.model.Rol;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolResponseDTO toDTO (Rol rol);

    List<RolResponseDTO> toDTOList (List<Rol> roles);

    Rol toEntity (RolRequestDTO dto);
}
