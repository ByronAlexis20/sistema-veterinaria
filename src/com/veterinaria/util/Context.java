package com.veterinaria.util;


import com.veterinaria.modelo.Cliente;
import com.veterinaria.modelo.ClienteMascota;
import com.veterinaria.modelo.Consulta;
import com.veterinaria.modelo.Enfermedad;
import com.veterinaria.modelo.Mascota;
import com.veterinaria.modelo.Perfil;
import com.veterinaria.modelo.Raza;
import com.veterinaria.modelo.TipoMascota;
import com.veterinaria.modelo.Usuario;
import com.veterinaria.modelo.Veterinario;

import javafx.stage.Stage;


public class Context {
	private final static Context instance = new Context();


	private Stage stage;
	private Stage stageModal;
	private Stage stageModalSecundario;
	private Stage stagePrincipal;
	
	private Usuario usuario;
	private Usuario usuarioSeleccionado;
	private Perfil perfil;
	private Cliente cliente;
	private Enfermedad enfermedad;
	private Raza raza;
	private TipoMascota tipoMascota;
	private Veterinario veterinario;
	private ClienteMascota clienteMascota;
	private String tratamiento;
	private Consulta consulta;
	private Mascota mascota;
	
	public static Context getInstance() {
		return instance;
	}
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public Stage getStageModal() {
		return stageModal;
	}
	public void setStageModal(Stage stageModal) {
		this.stageModal = stageModal;
	}
	public Stage getStagePrincipal() {
		return stagePrincipal;
	}
	public void setStagePrincipal(Stage stagePrincipal) {
		this.stagePrincipal = stagePrincipal;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Stage getStageModalSecundario() {
		return stageModalSecundario;
	}
	public void setStageModalSecundario(Stage stageModalSecundario) {
		this.stageModalSecundario = stageModalSecundario;
	}
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}
	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Enfermedad getEnfermedad() {
		return enfermedad;
	}
	public void setEnfermedad(Enfermedad enfermedad) {
		this.enfermedad = enfermedad;
	}
	public Raza getRaza() {
		return raza;
	}
	public void setRaza(Raza raza) {
		this.raza = raza;
	}
	public TipoMascota getTipoMascota() {
		return tipoMascota;
	}
	public void setTipoMascota(TipoMascota tipoMascota) {
		this.tipoMascota = tipoMascota;
	}
	public Veterinario getVeterinario() {
		return veterinario;
	}
	public void setVeterinario(Veterinario veterinario) {
		this.veterinario = veterinario;
	}
	public ClienteMascota getClienteMascota() {
		return clienteMascota;
	}
	public void setClienteMascota(ClienteMascota clienteMascota) {
		this.clienteMascota = clienteMascota;
	}
	public String getTratamiento() {
		return tratamiento;
	}
	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}
	public Consulta getConsulta() {
		return consulta;
	}
	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}
	public Mascota getMascota() {
		return mascota;
	}
	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}
	
}