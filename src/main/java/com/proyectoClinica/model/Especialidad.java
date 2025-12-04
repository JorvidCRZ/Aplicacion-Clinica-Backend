package com.proyectoClinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "especialidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Integer idEspecialidad;

    @Column(name="nombre" , nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "url_img_icono")
	private String urlImgIcono;

    @Column(name = "url_img_port")
	private String urlImgPort;

    @Column(name="descripcion_portada")
    private String descripcionPortada;

    @PrePersist
    public void prePersist() {
        if (descripcion == null) descripcion = "";
        if (urlImgIcono == null) urlImgIcono = "";
        if (urlImgPort == null) urlImgPort = "";
        if (descripcionPortada == null) descripcionPortada = "";
    }
}
