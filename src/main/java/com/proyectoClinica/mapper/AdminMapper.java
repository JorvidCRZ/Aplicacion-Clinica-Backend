package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.AdminRequestDTO;
import com.proyectoClinica.dto.response.AdminResponseDTO;
import com.proyectoClinica.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")

public interface AdminMapper {

    @Mapping(target = "nombreCompleto", source = "admin", qualifiedByName = "concatNombreCompleto")
    @Mapping(target = "personaId", source = "persona.idPersona")
    @Mapping(target = "usuarioId", source = "usuario.idUsuario")
    @Mapping(target = "usuarioAgregoId", source = "usuarioAgrego.idUsuario")
    AdminResponseDTO toDTO(Admin admin);

    List<AdminResponseDTO> toDTOList(List<Admin> admins);

    @Mapping(target = "persona.idPersona", source = "idPersona")
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "usuarioAgrego.idUsuario", source = "idUsuarioAgrego")
    Admin toEntity(AdminRequestDTO dto);

    @Named("concatNombreCompleto")
    default String concatNombreCompleto(Admin admin) {
        if (admin == null || admin.getPersona() == null) return "";
        String nombre1 = admin.getPersona().getNombre1() != null ? admin.getPersona().getNombre1() : "";
        String nombre2 = admin.getPersona().getNombre2() != null ? " " + admin.getPersona().getNombre2() : "";
        String apellidos = (admin.getPersona().getApellidoPaterno() != null ? " " + admin.getPersona().getApellidoPaterno() : "")
                + (admin.getPersona().getApellidoMaterno() != null ? " " + admin.getPersona().getApellidoMaterno() : "");
        return (nombre1 + nombre2 + apellidos).trim();
    }

}
