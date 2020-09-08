package com.veterinaria.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="cliente")
@NamedQueries({
	@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c"),
	@NamedQuery(name="Cliente.buscarPatron", query="SELECT c FROM Cliente c "
			+ "WHERE (lower(c.nombre) like lower(:patron) or lower(c.apellido) like lower(:patron)) and c.estado = 'A'"),
	@NamedQuery(name="Cliente.buscarPorCedula", query="SELECT c FROM Cliente c WHERE c.cedula = (:cedula) and c.estado = 'A'")
})
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_cliente")
	private Integer idCliente;

	private String apellido;

	private String cedula;

	private String direccion;

	private String email;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_modifica")
	private Date fechaModifica;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;

	private String nombre;

	private String telefono;

	@Column(name="usuario_crea")
	private String usuarioCrea;

	@Column(name="usuario_modifica")
	private String usuarioModifica;

	//bi-directional many-to-one association to ClienteMascota
	@OneToMany(mappedBy="cliente")
	private List<ClienteMascota> clienteMascotas;

	//bi-directional many-to-one association to Consulta
	@OneToMany(mappedBy="cliente")
	private List<Consulta> consultas;

	public Cliente() {
	}

	public Integer getIdCliente() {
		return this.idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
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

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public List<ClienteMascota> getClienteMascotas() {
		return this.clienteMascotas;
	}

	public void setClienteMascotas(List<ClienteMascota> clienteMascotas) {
		this.clienteMascotas = clienteMascotas;
	}

	public ClienteMascota addClienteMascota(ClienteMascota clienteMascota) {
		getClienteMascotas().add(clienteMascota);
		clienteMascota.setCliente(this);

		return clienteMascota;
	}

	public ClienteMascota removeClienteMascota(ClienteMascota clienteMascota) {
		getClienteMascotas().remove(clienteMascota);
		clienteMascota.setCliente(null);

		return clienteMascota;
	}

	public List<Consulta> getConsultas() {
		return this.consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}

	public Consulta addConsulta(Consulta consulta) {
		getConsultas().add(consulta);
		consulta.setCliente(this);

		return consulta;
	}

	public Consulta removeConsulta(Consulta consulta) {
		getConsultas().remove(consulta);
		consulta.setCliente(null);

		return consulta;
	}

}