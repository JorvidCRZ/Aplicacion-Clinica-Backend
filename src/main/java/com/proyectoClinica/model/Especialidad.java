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

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column
    private String descripcion;

    @Column(name = "url_img_icono")
	private String urlImgIcono;

    @Column(name = "url_img_port")
	private String urlImgPort;

    @Column(name="descripcion_portada")
    private String descripcionPortada;

    public Integer getIdEspecialidad() {
		return idEspecialidad;
	}

	public void setIdEspecialidad(Integer idEspecialidad) {
		this.idEspecialidad = idEspecialidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUrlImgIcono() {
		return urlImgIcono;
	}

	public void setUrlImgIcono(String urlImgIcono) {
		this.urlImgIcono = urlImgIcono;
	}

	public String getUrlImgPort() {
		return urlImgPort;
	}

	public void setUrlImgPort(String urlImgPort) {
		this.urlImgPort = urlImgPort;
	}

	public String getDescripcionPortada() {
		return descripcionPortada;
	}

	public void setDescripcionPortada(String descripcionPortada) {
		this.descripcionPortada = descripcionPortada;
	}
}
