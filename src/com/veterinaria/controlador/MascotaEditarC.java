package com.veterinaria.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Mascota;
import com.veterinaria.modelo.MascotaDAO;
import com.veterinaria.modelo.Raza;
import com.veterinaria.modelo.RazaDAO;
import com.veterinaria.modelo.TipoMascota;
import com.veterinaria.modelo.TipoMascotaDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class MascotaEditarC {

	@FXML private ComboBox<Raza> cboRaza;
	@FXML private Button btnSalir;
	@FXML private TextField txtNombre;
	@FXML private TextField txtCodigo;
	@FXML private RadioButton chkHembra;
	@FXML private RadioButton chkMacho;
	@FXML private Button btnGrabar;
	@FXML private ComboBox<TipoMascota> cboTipoMascota;
	@FXML private DatePicker dtpFecha;
	TipoMascotaDAO tipoMascotaDAO = new TipoMascotaDAO();
	RazaDAO razaDAO = new RazaDAO();
	Mascota mascota;
	ControllerHelper helper = new ControllerHelper();
	MascotaDAO mascotaDAO = new MascotaDAO();
	public void initialize() {
		try {
			txtCodigo.setText("0");
			chkHembra.setSelected(false);
			chkMacho.setSelected(true);
			cargarComboTipoMascota();
			cboTipoMascota.getSelectionModel().select(-1);
			dtpFecha.setValue(convertToLocalDate(new Date()));
			if(Context.getInstance().getMascota() != null) {
				mascota = Context.getInstance().getMascota();
				cargarDatos();
				Context.getInstance().setMascota(null);
			}else {
				mascota = new Mascota();
				mascota.setIdMascota(null);
			}
		}catch(Exception ex) {

		}
	}
	private void cargarDatos() {
		txtCodigo.setText(String.valueOf(mascota.getIdMascota()));
		cboTipoMascota.getSelectionModel().select(mascota.getRaza().getTipoMascota());
		cargarRaza(mascota.getRaza().getTipoMascota().getIdTipoMascota());
		cboRaza.getSelectionModel().select(mascota.getRaza());
		dtpFecha.setValue(convertToLocalDate(mascota.getFechaNacimiento()));
		if(mascota.getSexo().equals("MACHO")) {
			chkMacho.setSelected(true);
			chkHembra.setSelected(false);
		}else {
			chkMacho.setSelected(false);
			chkHembra.setSelected(true);
		}
		txtNombre.setText(mascota.getNombre());
	}
	public void cargarRaza(Integer idTipoMascota) {
		try {
			cboRaza.getItems().clear();
			ObservableList<Raza> datos = FXCollections.observableArrayList();
			List<Raza> lista = razaDAO.getListaRazasPorMascota(idTipoMascota);
			datos.addAll(lista);
			cboRaza.setItems(datos);
			cboRaza.setPromptText("Seleccione raza");
		}catch(Exception ex) {

		}
	}
	private void cargarComboTipoMascota() {
		try {
			ObservableList<TipoMascota> datos = FXCollections.observableArrayList();
			List<TipoMascota> listaServicio = tipoMascotaDAO.getListaTipoMascotas();
			datos.addAll(listaServicio);
			cboTipoMascota.setItems(datos);
			cboTipoMascota.setPromptText("Seleccione tipo de mascota");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void seleccionarRaza() {
		try {
			cboRaza.getItems().clear();
			ObservableList<Raza> datos = FXCollections.observableArrayList();
			List<Raza> lista = razaDAO.getListaRazasPorMascota(cboTipoMascota.getSelectionModel().getSelectedItem().getIdTipoMascota());
			datos.addAll(lista);
			cboRaza.setItems(datos);
			cboRaza.setPromptText("Seleccione raza");
		}catch(Exception ex) {

		}
	}


	public void seleccionMacho() {
		try {
			chkMacho.setSelected(true);
			chkHembra.setSelected(false);
		}catch(Exception ex) {

		}
	}


	public void seleccionHembra() {
		try {
			chkMacho.setSelected(false);
			chkHembra.setSelected(true);
		}catch(Exception ex) {

		}
	}


	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				mascota.setEstado("A");
				mascota.setFechaNacimiento(convertToDate(dtpFecha.getValue()));
				mascota.setNombre(txtNombre.getText());
				mascota.setRaza(cboRaza.getSelectionModel().getSelectedItem());
				if(chkHembra.isSelected())
					mascota.setSexo("HEMBRA");
				else
					mascota.setSexo("MACHO");

				if(mascota.getIdMascota() != null) {//modifica
					mascotaDAO.getEntityManager().getTransaction().begin();
					mascotaDAO.getEntityManager().merge(mascota);
					mascotaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}else {//inserta
					mascotaDAO.getEntityManager().getTransaction().begin();
					mascotaDAO.getEntityManager().persist(mascota);
					mascotaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
		}
	}
	private boolean validarDatos() {
		try {
			boolean bandera = false;
			if(cboTipoMascota.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar tipo de mascota", Context.getInstance().getStage());
				return false;
			}
			if(cboRaza.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar raza de la mascota", Context.getInstance().getStage());
				return false;
			}
			if(dtpFecha.getValue() == null) {
				helper.mostrarAlertaAdvertencia("Debe registrar fecha de nacimiento", Context.getInstance().getStage());
				return false;
			}
			if(txtNombre.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar nombre de la mascota", Context.getInstance().getStage());
				return false;
			}			
			bandera = true;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}


	public void salir() {
		try {
			Context.getInstance().getStageModal().close();
		}catch(Exception ex) {

		}
	}
	public LocalDate convertToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
	}
	public Date convertToDate(LocalDate dateToConvert) {
		return java.sql.Date.valueOf(dateToConvert);
	}
}
