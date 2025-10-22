package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.MedicoResponseDTO;
import com.proyectoClinica.model.Medico;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicoMapper {

    MedicoResponseDTO toDTO (Medico medico);

    List<MedicoResponseDTO> toDTOList (List<Medico> medicos);

    Medico toEntity (MedicoRequestDTO dto);
}
