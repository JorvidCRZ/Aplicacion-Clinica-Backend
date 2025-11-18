package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.request.PagoRequestDTO;
import com.proyectoClinica.dto.response.PagoResponseDTO;
import com.proyectoClinica.model.Pago;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PagoMapper {

    PagoResponseDTO toDTO (Pago pago);

    List<PagoResponseDTO> toDTOList (List<Pago> pagos);

    Pago toEntity (PagoRequestDTO dto);
}
