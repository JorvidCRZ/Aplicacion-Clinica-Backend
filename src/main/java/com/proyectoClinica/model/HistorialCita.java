package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_cita")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial_cita")
    private Integer idHistorialCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita")
    private Cita cita;

    @Column(length = 50)
    private String accion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column
    private String detalle;
}
