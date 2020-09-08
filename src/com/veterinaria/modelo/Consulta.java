package com.veterinaria.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the consulta database table.
 * 
 */
@Entity
@Table(name="consulta")
@NamedQueries({
	@NamedQuery(name="Consulta.buscarPatron", query="SELECT c FROM Consulta c "
			+ "WHERE (lower(c.cliente.nombre) like lower(:patron) or lower(c.cliente.apellido) like lower(:patron)) and c.estado = 'A'"),
	@NamedQuery(name="Consulta.numFactura", query="SELECT c FROM Consulta c WHERE c.numeroFactura = :patron and c.estado = 'A'"),
	@NamedQuery(name="Consulta.numSiguienteFactura", query="SELECT max(c.numeroFactura) FROM Consulta c WHERE c.estado = 'A'")
})
public class Consulta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_consulta")
	private Integer idConsulta;

	private String estado;

	@Column(name="examen_fisico")
	private String examenFisico;

	private String motivo;

	@Column(name="numero_factura")
	private Integer numeroFactura;

	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@Column(name="tiempo_tratamiento")
	private Integer tiempoTratamiento;

	@Column(name="valor_costo")
	private Double valorCosto;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	//bi-directional many-to-one association to ClienteMascota
	@ManyToOne
	@JoinColumn(name="id_cliente_mascota")
	private ClienteMascota clienteMascota;

	//bi-directional many-to-one association to Veterinario
	@ManyToOne
	@JoinColumn(name="id_veterinario")
	private Veterinario veterinario;

	//bi-directional many-to-one association to ConsultaDetalle
	@OneToMany(mappedBy="consulta",cascade = CascadeType.ALL)
	private List<ConsultaDetalle> consultaDetalles;

	public Consulta() {
	}

	public Integer getIdConsulta() {
		return this.idConsulta;
	}

	public void setIdConsulta(Integer idConsulta) {
		this.idConsulta = idConsulta;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getExamenFisico() {
		return this.examenFisico;
	}

	public void setExamenFisico(String examenFisico) {
		this.examenFisico = examenFisico;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Integer getNumeroFactura() {
		return this.numeroFactura;
	}

	public void setNumeroFactura(Integer numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public Integer getTiempoTratamiento() {
		return this.tiempoTratamiento;
	}

	public void setTiempoTratamiento(Integer tiempoTratamiento) {
		this.tiempoTratamiento = tiempoTratamiento;
	}

	public Double getValorCosto() {
		return this.valorCosto;
	}

	public void setValorCosto(Double valorCosto) {
		this.valorCosto = valorCosto;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ClienteMascota getClienteMascota() {
		return this.clienteMascota;
	}

	public void setClienteMascota(ClienteMascota clienteMascota) {
		this.clienteMascota = clienteMascota;
	}

	public Veterinario getVeterinario() {
		return this.veterinario;
	}

	public void setVeterinario(Veterinario veterinario) {
		this.veterinario = veterinario;
	}

	public List<ConsultaDetalle> getConsultaDetalles() {
		return this.consultaDetalles;
	}

	public void setConsultaDetalles(List<ConsultaDetalle> consultaDetalles) {
		this.consultaDetalles = consultaDetalles;
	}

	public ConsultaDetalle addConsultaDetalle(ConsultaDetalle consultaDetalle) {
		getConsultaDetalles().add(consultaDetalle);
		consultaDetalle.setConsulta(this);

		return consultaDetalle;
	}

	public ConsultaDetalle removeConsultaDetalle(ConsultaDetalle consultaDetalle) {
		getConsultaDetalles().remove(consultaDetalle);
		consultaDetalle.setConsulta(null);

		return consultaDetalle;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "Consulta [idConsulta=" + idConsulta + ", estado=" + estado + ", examenFisico=" + examenFisico
				+ ", motivo=" + motivo + ", numeroFactura=" + numeroFactura + ", fecha=" + fecha
				+ ", tiempoTratamiento=" + tiempoTratamiento + ", valorCosto=" + valorCosto + ", cliente=" + cliente
				+ ", clienteMascota=" + clienteMascota + ", veterinario=" + veterinario + ", consultaDetalles="
				+ consultaDetalles + "]";
	}
	
	

}