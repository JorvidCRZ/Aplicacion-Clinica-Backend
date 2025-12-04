package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorariosMedicoResponseDTO {
    private Integer idMedico;
    private List<HorarioPorFechaResponseDTO> dias;
}
