package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.UsuarioRequestDTO;
import com.proyectoClinica.dto.response.UsuarioResponseDTO;
import com.proyectoClinica.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PersonaMapper.class, RolMapper.class})
public interface UsuarioMapper {

    UsuarioResponseDTO toDTO (Usuario usuario);

    List<UsuarioResponseDTO> toDTOList (List<Usuario> usuarios);

    @Mapping(source = "persona", target = "persona")
    @Mapping(source = "idRol", target = "rol.idRol")
    @Mapping(target = "idUsuario", ignore = true)
    Usuario toEntity (UsuarioRequestDTO dto);
}
