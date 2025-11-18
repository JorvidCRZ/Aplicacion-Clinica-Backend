package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.ReporteRequestDTO;
import com.proyectoClinica.dto.response.ReporteResponseDTO;
import com.proyectoClinica.model.Reporte;
import com.proyectoClinica.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

    @Mapping(target = "admin", source = "admin")
    ReporteResponseDTO toDTO(Reporte reporte);

    @Mapping(target = "admin", source = "admin")
    List<ReporteResponseDTO> toDTOList(List<Reporte> reportes);

    @Mapping(target = "admin", source = "admin")
    Reporte toEntity(ReporteRequestDTO dto);

    default Usuario map(Integer adminId) {
        if (adminId == null) return null;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(adminId);
        return usuario;
    }

    default Integer map(Usuario usuario) {
        return (usuario == null) ? null : usuario.getIdUsuario();
    }
}
