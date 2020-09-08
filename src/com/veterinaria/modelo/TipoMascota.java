package com.veterinaria.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_mascota database table.
 * 
 */
@Entity
@Table(name="tipo_mascota")
@NamedQueries({
	@NamedQuery(name="TipoMascota.busquedaPorPatron", query="SELECT t FROM TipoMascota t where lower(t.descripcion) like (:patron)"),
	@NamedQuery(name="TipoMascota.buscarActivos", query="SELECT t FROM TipoMascota t where t.estado = 'A'"),
	@NamedQuery(name="TipoMascota.porTipo", query="SELECT t FROM TipoMascota t where t.idTipoMascota=:patron")
})
public class TipoMascota implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_mascota")
	private Integer idTipoMascota;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Raza
	@OneToMany(mappedBy="tipoMascota")
	private List<Raza> razas;

	public TipoMascota() {
	}

	public Integer getIdTipoMascota() {
		return this.idTipoMascota;
	}

	public void setIdTipoMascota(Integer idTipoMascota) {
		this.idTipoMascota = idTipoMascota;
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

	public List<Raza> getRazas() {
		return this.razas;
	}

	public void setRazas(List<Raza> razas) {
		this.razas = razas;
	}

	public Raza addRaza(Raza raza) {
		getRazas().add(raza);
		raza.setTipoMascota(this);

		return raza;
	}

	public Raza removeRaza(Raza raza) {
		getRazas().remove(raza);
		raza.setTipoMascota(null);

		return raza;
	}

	@Override
	public String toString() {
		return this.descripcion;
	}

}