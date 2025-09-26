package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "cita")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disponibilidad", nullable = false)
    private DisponibilidadMedico disponibilidadMedico;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @Column(name = "hora_cita", nullable = false)
    private LocalTime horaCita;

    @Column
    private String estado;

    @Column(name = "motivo_consulta")
    private String motivoConsulta;

    @Column
    private BigDecimal precioFinal;

}
