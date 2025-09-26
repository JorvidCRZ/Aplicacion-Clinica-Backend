package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false,  length = 50)
    private String metodo;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column
    private LocalDate fechaPago;
}
