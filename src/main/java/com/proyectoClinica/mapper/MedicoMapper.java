package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.MedicoResponseDTO;
import com.proyectoClinica.model.Medico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicoMapper {

    // Mapea correo del usuario
    @Mapping(source = "usuario.correo", target = "email")

    // Mapea especialidad tomando la primera
    @Mapping(target = "especialidad",
            expression = "java( medico.getEspecialidades() != null && !medico.getEspecialidades().isEmpty() ? " +
                    "medico.getEspecialidades().get(0).getEspecialidad().getNombre() : null )")

    // Mapea horario tomando la primera disponibilidad
    @Mapping(target = "horario",
            expression = "java( medico.getDisponibilidades() != null && !medico.getDisponibilidades().isEmpty() ? " +
                    "medico.getDisponibilidades().get(0).getDiaSemana() + \" \" + " +
                    "medico.getDisponibilidades().get(0).getHoraInicio() + \" - \" + " +
                    "medico.getDisponibilidades().get(0).getHoraFin() : null )")
    MedicoResponseDTO toDTO(Medico medico);

    List<MedicoResponseDTO> toDTOList(List<Medico> medicos);

    Medico toEntity(MedicoRequestDTO dto);
}
