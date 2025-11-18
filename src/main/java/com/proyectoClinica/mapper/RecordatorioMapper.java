package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.RecordatorioRequestDTO;
import com.proyectoClinica.dto.response.RecordatorioResponseDTO;
import com.proyectoClinica.model.Recordatorio;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecordatorioMapper {

    RecordatorioResponseDTO toDTO (Recordatorio recordatorio);

    List<RecordatorioResponseDTO> toDTOList (List<Recordatorio> recordatorios);

    Recordatorio toEntity (RecordatorioRequestDTO dto);
}
