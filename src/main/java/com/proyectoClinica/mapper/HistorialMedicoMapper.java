package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.HistorialMedicoRequestDTO;
import com.proyectoClinica.dto.response.HistorialMedicoResponseDTO;
import com.proyectoClinica.model.HistorialMedico;
import com.proyectoClinica.model.Medico;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistorialMedicoMapper {

    HistorialMedicoResponseDTO toDTO (HistorialMedico historialMedico);

    List<HistorialMedicoResponseDTO> toDTOList (List<HistorialMedico> historialMedicos);

    HistorialMedico toEntity (HistorialMedicoRequestDTO dto);
}
