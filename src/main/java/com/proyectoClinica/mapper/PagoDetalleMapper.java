package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.PagoDetalleRequestDTO;
import com.proyectoClinica.dto.response.PagoDetalleResponseDTO;
import com.proyectoClinica.model.PagoDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PagoDetalleMapper {

    PagoDetalleResponseDTO toDTO (PagoDetalle pagoDetalle);

    List<PagoDetalleResponseDTO> toDTOList (List<PagoDetalle> pagoDetalles);
    @Mapping(target = "idPagoDetalle", ignore = true)
    @Mapping(source = "idCita", target = "cita.idCita")
    @Mapping(source = "idPago", target = "pago.idPago")
    PagoDetalle toEntity (PagoDetalleRequestDTO dto);
}
