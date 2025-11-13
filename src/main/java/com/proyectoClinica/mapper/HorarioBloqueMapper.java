package com.proyectoClinica.mapper;
import com.proyectoClinica.dto.request.HorarioBloqueRequestDTO;
import com.proyectoClinica.dto.response.HorarioBloqueResponseDTO;
import com.proyectoClinica.model.HorarioBloque;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HorarioBloqueMapper {

    @Mapping(target = "disponibilidadMedico.idDisponibilidad", source = "idDisponibilidad")
    @Mapping(target = "cita.idCita", source = "idCita")
    HorarioBloque toEntity(HorarioBloqueRequestDTO request);

    @Mapping(target = "idDisponibilidad", source = "disponibilidadMedico.idDisponibilidad")
    @Mapping(target = "idCita", source = "cita.idCita")
    HorarioBloqueResponseDTO toResponse(HorarioBloque entity);
}
