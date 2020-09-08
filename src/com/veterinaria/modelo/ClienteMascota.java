package com.veterinaria.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the cliente_mascota database table.
 * 
 */
@Entity
@Table(name="cliente_mascota")
@NamedQueries({
	@NamedQuery(name="ClienteMascota.buscarPorCliente", query="SELECT c FROM ClienteMascota c where lower(c.mascota.nombre) like lower(:patron) and c.cliente.idCliente = :idCliente and c.estado = 'A'"),
	@NamedQuery(name="ClienteMascota.buscarSinDueno", query="SELECT c FROM ClienteMascota c where lower(c.mascota.nombre) like lower(:patron) and c.estado = 'A' and c.cliente = null"),
	@NamedQuery(name="ClienteMascota.buscarActivos", query="SELECT c FROM ClienteMascota c where c.estado = 'A'"),
	@NamedQuery(name="ClienteMascota.buscarPorDuenio", query="SELECT c FROM ClienteMascota c where c.estado = 'A' and c.mascota.idMascota = :idMascota")
	
})
public class ClienteMascota implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_cliente_mascota")
	private Integer idClienteMascota;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	//bi-directional many-to-one association to Mascota
	@ManyToOne
	@JoinColumn(name="id_mascota")
	private Mascota mascota;

	//bi-directional many-to-one association to Consulta
	@OneToMany(mappedBy="clienteMascota")
	private List<Consulta> consultas;

	public ClienteMascota() {
	}

	public Integer getIdClienteMascota() {
		return this.idClienteMascota;
	}

	public void setIdClienteMascota(Integer idClienteMascota) {
		this.idClienteMascota = idClienteMascota;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Mascota getMascota() {
		return this.mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	public List<Consulta> getConsultas() {
		return this.consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}

	public Consulta addConsulta(Consulta consulta) {
		getConsultas().add(consulta);
		consulta.setClienteMascota(this);

		return consulta;
	}

	public Consulta removeConsulta(Consulta consulta) {
		getConsultas().remove(consulta);
		consulta.setClienteMascota(null);

		return consulta;
	}

}