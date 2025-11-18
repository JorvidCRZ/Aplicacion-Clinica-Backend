package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.request.UsuarioEditRequestDTO;
import com.proyectoClinica.dto.response.PacienteResponseDTO;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.model.Persona;
import com.proyectoClinica.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    // ----------- DTO → Entidad -----------
    @Mapping(target = "idPaciente", ignore = true)
    @Mapping(target = "persona", expression = "java(mapIdToPersona(dto.getIdPersona()))")
    @Mapping(target = "usuarioAgrego", expression = "java(mapUsuarioAgrego(dto.getUsuarioAgrego()))")
    Paciente toEntity(PacienteRequestDTO dto);

    // ----------- Entidad → DTO -----------
    @Mapping(source = "persona.usuario.correo", target = "email")
    PacienteResponseDTO toDTO(Paciente paciente);

    List<PacienteResponseDTO> toDTOList(List<Paciente> pacientes);

    // ----------- Actualización parcial -----------
    void updateEntityFromDto(PacienteRequestDTO dto, @MappingTarget Paciente entity);

    // ----------- Métodos auxiliares -----------
    default Persona mapIdToPersona(Integer idPersona) {
        if (idPersona == null) return null;
        Persona persona = new Persona();
        persona.setIdPersona(idPersona);
        return persona;
    }

    default Usuario mapUsuarioAgrego(UsuarioEditRequestDTO dto) {
        if (dto == null) return null;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        usuario.setCorreo(dto.getCorreo());
        return usuario;
    }
}
