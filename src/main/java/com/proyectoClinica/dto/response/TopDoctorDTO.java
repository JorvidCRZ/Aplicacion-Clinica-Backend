package com.proyectoClinica.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopDoctorDTO {

    private String nombreCompleto;
    private String especialidad;
    private long totalCitas;
}
