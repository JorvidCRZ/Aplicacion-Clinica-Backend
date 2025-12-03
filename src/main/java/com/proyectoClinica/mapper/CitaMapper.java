package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.CitaRequestDTO;
import com.proyectoClinica.dto.response.CitaResponseDTO;
import com.proyectoClinica.dto.response.CrearCitaResponoseDTO;
import com.proyectoClinica.model.Cita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CitaMapper {

    CitaResponseDTO toDTO (Cita cita);

    List<CitaResponseDTO> toDTOList (List<Cita> citas);

    Cita toEntity (CitaRequestDTO dto);

    @Mapping(source = "cita.paciente.persona.nombre1", target = "pacienteNombre")
    @Mapping(source = "cita.detalleCita.medicoEspecialidad.medico.persona.nombre1", target = "medicoNombre")
    @Mapping(source = "cita.detalleCita.medicoEspecialidad.especialidad.nombre", target = "especialidad")
    @Mapping(source = "cita.detalleCita.subEspecialidad.nombre", target = "subEspecialidad")
    @Mapping(source = "cita.fechaCita", target = "fecha")
    @Mapping(source = "cita.horaCita", target = "hora")
    @Mapping(source = "cita.detalleCita.precioTotal", target = "precio")
    @Mapping(source = "cita.estado", target = "estado")
    CrearCitaResponoseDTO crearDTO(Cita cita);

    List<CrearCitaResponoseDTO> toCrearDTOList(List<Cita> citas);

}
