package com.veterinaria.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the mascota database table.
 * 
 */
@Entity
@Table(name="mascota")
@NamedQueries({
	@NamedQuery(name="Mascota.busquedaPorPatron", query="SELECT m FROM Mascota m where lower(m.nombre) like lower(:patron)"),
	@NamedQuery(name="Mascota.buscarActivos", query="SELECT m FROM Mascota m where m.estado = 'A'")

})
public class Mascota implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_mascota")
	private Integer idMascota;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;

	private String nombre;

	private String sexo;

	//bi-directional many-to-one association to ClienteMascota
	@OneToMany(mappedBy="mascota")
	private List<ClienteMascota> clienteMascotas;

	//bi-directional many-to-one association to Raza
	@ManyToOne
	@JoinColumn(name="id_raza")
	private Raza raza;

	public Mascota() {
	}

	public Integer getIdMascota() {
		return this.idMascota;
	}

	public void setIdMascota(Integer idMascota) {
		this.idMascota = idMascota;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSexo() {
		return this.sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public List<ClienteMascota> getClienteMascotas() {
		return this.clienteMascotas;
	}

	public void setClienteMascotas(List<ClienteMascota> clienteMascotas) {
		this.clienteMascotas = clienteMascotas;
	}

	public ClienteMascota addClienteMascota(ClienteMascota clienteMascota) {
		getClienteMascotas().add(clienteMascota);
		clienteMascota.setMascota(this);

		return clienteMascota;
	}

	public ClienteMascota removeClienteMascota(ClienteMascota clienteMascota) {
		getClienteMascotas().remove(clienteMascota);
		clienteMascota.setMascota(null);

		return clienteMascota;
	}

	public Raza getRaza() {
		return this.raza;
	}

	public void setRaza(Raza raza) {
		this.raza = raza;
	}

	@Override
	public String toString() {
		return "" + nombre + "";
	}
	
	
}