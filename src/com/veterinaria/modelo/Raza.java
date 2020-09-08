package com.veterinaria.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the raza database table.
 * 
 */
@Entity
@Table(name="raza")
@NamedQueries({
	@NamedQuery(name="Raza.busquedaPorPatron", query="SELECT r FROM Raza r where lower(r.descripcion) like lower(:patron)"),
	@NamedQuery(name="Raza.buscarActivos", query="SELECT r FROM Raza r where r.estado = 'A'"),
	@NamedQuery(name="Raza.buscarPorTipo", query="SELECT r FROM Raza r where r.estado = 'A' and r.tipoMascota.idTipoMascota = :idTipoMascota")
})
public class Raza implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_raza")
	private Integer idRaza;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Mascota
	@OneToMany(mappedBy="raza")
	private List<Mascota> mascotas;

	//bi-directional many-to-one association to TipoMascota
	@ManyToOne
	@JoinColumn(name="id_tipo_mascota")
	private TipoMascota tipoMascota;

	public Raza() {
	}

	public Integer getIdRaza() {
		return this.idRaza;
	}

	public void setIdRaza(Integer idRaza) {
		this.idRaza = idRaza;
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

	public List<Mascota> getMascotas() {
		return this.mascotas;
	}

	public void setMascotas(List<Mascota> mascotas) {
		this.mascotas = mascotas;
	}

	public Mascota addMascota(Mascota mascota) {
		getMascotas().add(mascota);
		mascota.setRaza(this);

		return mascota;
	}

	public Mascota removeMascota(Mascota mascota) {
		getMascotas().remove(mascota);
		mascota.setRaza(null);

		return mascota;
	}

	public TipoMascota getTipoMascota() {
		return this.tipoMascota;
	}

	public void setTipoMascota(TipoMascota tipoMascota) {
		this.tipoMascota = tipoMascota;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.descripcion;
	}

}