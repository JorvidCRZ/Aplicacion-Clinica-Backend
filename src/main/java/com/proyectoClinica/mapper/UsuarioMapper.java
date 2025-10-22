package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.UsuarioRequestDTO;
import com.proyectoClinica.dto.response.UsuarioResponseDTO;
import com.proyectoClinica.model.Usuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toDTO (Usuario usuario);

    List<UsuarioResponseDTO> toDTOList (List<Usuario> usuarios);

    Usuario toEntity (UsuarioRequestDTO dto);
}
