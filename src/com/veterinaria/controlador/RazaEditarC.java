package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;


public class RazaEditarC {
	Tooltip toolTip;
	@FXML private Button btnSalir;
	@FXML private TextField txtRaza;
	@FXML private TextField txtCodigo;
	@FXML private Button btnGrabar;
	@FXML private ComboBox<TipoMascota> cboTipoMascota;

	TipoMascotaDAO tipoMascotaDAO = new TipoMascotaDAO();
	RazaDAO razaDAO = new RazaDAO();
	Raza razaSeleccionado;
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		llenarCombos();
		toolTip = new Tooltip("Grabar usuario");
		btnGrabar.setStyle("-fx-graphic: url('/grabar.png');-fx-cursor: hand;");
		btnGrabar.setTooltip(toolTip);

		toolTip = new Tooltip("Salir");
		btnSalir.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
		btnSalir.setTooltip(toolTip);
		
		toolTip = new Tooltip("Seleccionar tipo de mascota");
		cboTipoMascota.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
		cboTipoMascota.setTooltip(toolTip);
		
		txtCodigo.setText("0");
		if(Context.getInstance().getRaza() != null) {
			razaSeleccionado = Context.getInstance().getRaza();
			Context.getInstance().setRaza(null);
			cargarDatos();
		}else {
			razaSeleccionado = new Raza();
			razaSeleccionado.setIdRaza(null);
		}
	}
	private void cargarDatos() {
		txtCodigo.setText(String.valueOf(razaSeleccionado.getIdRaza()));
		txtRaza.setText(String.valueOf(razaSeleccionado.getDescripcion()));
		cboTipoMascota.getSelectionModel().select(razaSeleccionado.getTipoMascota());
		
	}
	private void llenarCombos() {
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
	
	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}
			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				razaSeleccionado.setDescripcion(txtRaza.getText());
				razaSeleccionado.setEstado("A");
				razaSeleccionado.setTipoMascota(cboTipoMascota.getSelectionModel().getSelectedItem());
				
				if(razaSeleccionado.getIdRaza() != null) {//modifica
					razaDAO.getEntityManager().getTransaction().begin();
					razaDAO.getEntityManager().merge(razaSeleccionado);
					razaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}else {//inserta
					razaDAO.getEntityManager().getTransaction().begin();
					razaDAO.getEntityManager().persist(razaSeleccionado);
					razaDAO.getEntityManager().getTransaction().commit();
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
			if(txtRaza.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar la raza", Context.getInstance().getStage());
				return false;
			}
			if(cboTipoMascota.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar tipo de mascota", Context.getInstance().getStage());
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
			System.out.println(ex.getMessage());
		}
	}
}
