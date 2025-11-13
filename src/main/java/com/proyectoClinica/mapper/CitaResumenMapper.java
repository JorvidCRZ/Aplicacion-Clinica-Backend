package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.CitaResumenRequestDTO;
import com.proyectoClinica.dto.response.CitaResumenResponseDTO;
import com.proyectoClinica.model.CitaResumen;
import com.proyectoClinica.model.Cita;
import com.proyectoClinica.model.Medico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CitaResumenMapper {

    CitaResumenMapper INSTANCE = Mappers.getMapper(CitaResumenMapper.class);

    // DTO -> Entidad
    @Mapping(target = "cita", expression = "java(mapCita(dto.getIdCita()))")
    @Mapping(target = "medico", expression = "java(mapMedico(dto.getIdMedico()))")
    CitaResumen toEntity(CitaResumenRequestDTO dto);

    // Entidad -> DTO
    @Mapping(target = "idCita", source = "cita.idCita")
    @Mapping(target = "idMedico", source = "medico.idMedico")
    @Mapping(target = "nombreMedico", expression = "java(formatearNombreMedico(entity))")
    CitaResumenResponseDTO toDTO(CitaResumen entity);

    // Métodos auxiliares
    default Cita mapCita(Integer idCita) {
        if (idCita == null) return null;
        Cita c = new Cita();
        c.setIdCita(idCita);
        return c;
    }

    default Medico mapMedico(Integer idMedico) {
        if (idMedico == null) return null;
        Medico m = new Medico();
        m.setIdMedico(idMedico);
        return m;
    }

    default String formatearNombreMedico(CitaResumen entity) {
        if (entity == null || entity.getMedico() == null || entity.getMedico().getPersona() == null) {
            return null;
        }
        var p = entity.getMedico().getPersona();
        String nombreCompleto = (p.getNombre1() != null ? p.getNombre1() + " " : "")
                + (p.getApellidoPaterno() != null ? p.getApellidoPaterno() + " " : "")
                + (p.getApellidoMaterno() != null ? p.getApellidoMaterno() : "");
        return nombreCompleto.trim();
    }
}
