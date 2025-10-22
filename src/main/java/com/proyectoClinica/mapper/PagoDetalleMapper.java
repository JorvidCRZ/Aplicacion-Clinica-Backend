package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.PagoDetalleRequestDTO;
import com.proyectoClinica.dto.response.PagoDetalleResponseDTO;
import com.proyectoClinica.model.PagoDetalle;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PagoDetalleMapper {

    PagoDetalleResponseDTO toDTO (PagoDetalle pagoDetalle);

    List<PagoDetalleResponseDTO> toDTOList (List<PagoDetalle> pagoDetalles);

    PagoDetalle toEntity (PagoDetalleRequestDTO dto);
}
