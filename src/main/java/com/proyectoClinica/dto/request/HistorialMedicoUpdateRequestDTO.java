package com.proyectoClinica.dto.request;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialMedicoUpdateRequestDTO {

    private String diagnostico;
    private String observaciones;
    private String receta;
}
