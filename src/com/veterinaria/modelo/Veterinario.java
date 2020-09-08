package com.veterinaria.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the veterinario database table.
 * 
 */
@Entity
@Table(name="veterinario")
@NamedQueries({
	@NamedQuery(name="Veterinario.findAll", query="SELECT c FROM Veterinario c"),
	@NamedQuery(name="Veterinario.buscarPatron", query="SELECT c FROM Veterinario c "
			+ "WHERE (lower(c.nombres) like lower(:patron) or lower(c.apellidos) like lower(:patron)) and c.estado = 'A'"),
	@NamedQuery(name="Veterinario.buscarPorCedula", query="SELECT c FROM Veterinario c WHERE c.cedula = (:cedula) and c.estado = 'A'")
})
public class Veterinario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_veterinario")
	private Integer idVeterinario;

	private String apellidos;

	private String cedula;

	private String estado;

	private String nombres;

	private String profesion;

	private String telefono;

	//bi-directional many-to-one association to Consulta
	@OneToMany(mappedBy="veterinario")
	private List<Consulta> consultas;

	public Veterinario() {
	}

	public Integer getIdVeterinario() {
		return this.idVeterinario;
	}

	public void setIdVeterinario(Integer idVeterinario) {
		this.idVeterinario = idVeterinario;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getProfesion() {
		return this.profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<Consulta> getConsultas() {
		return this.consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}

	public Consulta addConsulta(Consulta consulta) {
		getConsultas().add(consulta);
		consulta.setVeterinario(this);

		return consulta;
	}

	public Consulta removeConsulta(Consulta consulta) {
		getConsultas().remove(consulta);
		consulta.setVeterinario(null);

		return consulta;
	}

	@Override
	public String toString() {
		return this.nombres + " " + this.apellidos;
	}

	
}