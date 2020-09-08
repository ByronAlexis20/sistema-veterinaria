package com.veterinaria.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the enfermedad database table.
 * 
 */
@Entity
@Table(name="enfermedad")
@NamedQueries({
	@NamedQuery(name="Enfermedad.busquedaPorPatron", query="SELECT e FROM Enfermedad e where lower(e.nombre) like lower(:patron)"),
	@NamedQuery(name="Enfermedad.buscarActivos", query="SELECT e FROM Enfermedad e where e.estado = 'A'")
})
public class Enfermedad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_enfermedad")
	private Integer idEnfermedad;

	private String descripcion;

	private String estado;

	private String nombre;

	//bi-directional many-to-one association to ConsultaDetalle
	@OneToMany(mappedBy="enfermedad")
	private List<ConsultaDetalle> consultaDetalles;

	public Enfermedad() {
	}

	public Integer getIdEnfermedad() {
		return this.idEnfermedad;
	}

	public void setIdEnfermedad(Integer idEnfermedad) {
		this.idEnfermedad = idEnfermedad;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<ConsultaDetalle> getConsultaDetalles() {
		return this.consultaDetalles;
	}

	public void setConsultaDetalles(List<ConsultaDetalle> consultaDetalles) {
		this.consultaDetalles = consultaDetalles;
	}

	public ConsultaDetalle addConsultaDetalle(ConsultaDetalle consultaDetalle) {
		getConsultaDetalles().add(consultaDetalle);
		consultaDetalle.setEnfermedad(this);

		return consultaDetalle;
	}

	public ConsultaDetalle removeConsultaDetalle(ConsultaDetalle consultaDetalle) {
		getConsultaDetalles().remove(consultaDetalle);
		consultaDetalle.setEnfermedad(null);

		return consultaDetalle;
	}

}