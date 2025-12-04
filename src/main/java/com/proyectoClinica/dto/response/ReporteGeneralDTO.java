package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteGeneralDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoCitaDTO estadoCitas;
    private List<TopDoctorDTO> topDoctores;
    private long totalCitas;
}
