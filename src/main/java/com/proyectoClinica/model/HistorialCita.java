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
    @Column(name = "id_hotorial_cita")
    private Integer idHistorialCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @Column(nullable = false, length = 50)
    private String accion;

    @Column
    private LocalDateTime fecha;

    @Column
    private String detalle;

}
