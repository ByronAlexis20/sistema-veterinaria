package com.veterinaria.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the consulta_detalle database table.
 * 
 */
@Entity
@Table(name="consulta_detalle")
@NamedQuery(name="ConsultaDetalle.findAll", query="SELECT c FROM ConsultaDetalle c")
public class ConsultaDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_detalle")
	private Integer idDetalle;

	private String estado;

	//bi-directional many-to-one association to Consulta
	@ManyToOne
	@JoinColumn(name="id_consulta")
	private Consulta consulta;

	//bi-directional many-to-one association to Enfermedad
	@ManyToOne
	@JoinColumn(name="id_enfermedad")
	private Enfermedad enfermedad;

	public ConsultaDetalle() {
	}

	public Integer getIdDetalle() {
		return this.idDetalle;
	}

	public void setIdDetalle(Integer idDetalle) {
		this.idDetalle = idDetalle;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Consulta getConsulta() {
		return this.consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public Enfermedad getEnfermedad() {
		return this.enfermedad;
	}

	public void setEnfermedad(Enfermedad enfermedad) {
		this.enfermedad = enfermedad;
	}

}