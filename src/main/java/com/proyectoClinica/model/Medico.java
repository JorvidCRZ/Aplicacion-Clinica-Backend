package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Medico {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Integer idMedico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(nullable = false, unique = true)
    private String colegiatura;

    @Column(name = "experiencia_anios")
    private Integer experienciaAnios;

    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    private List<MedicoEspecialidad> especialidades;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    private List<DisponibilidadMedico> disponibilidades;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<HorarioDisponible> horariosDisponibles = new ArrayList<>();


}