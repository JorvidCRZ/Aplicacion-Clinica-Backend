package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_cita")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_cita")
    private Integer idDetalleCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico_especialidad", nullable = false)
    private MedicoEspecialidad medicoEspecialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subespecialidad")
    private SubEspecialidad subEspecialidad;

    @Column(name = "precio_consulta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioConsulta;

    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @Column(name = "diagnostico", columnDefinition = "text")
    private String diagnostico;

    @PrePersist
    public void prePersistDefaults() {
        if (diagnostico == null) diagnostico = "";
        if (precioTotal == null) precioTotal = BigDecimal.ZERO;
    }
}
