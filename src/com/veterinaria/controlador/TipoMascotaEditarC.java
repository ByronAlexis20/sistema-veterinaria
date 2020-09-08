package com.veterinaria.controlador;

import java.util.Optional;

import com.veterinaria.modelo.TipoMascota;
import com.veterinaria.modelo.TipoMascotaDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class TipoMascotaEditarC {
	Tooltip toolTip;
	@FXML private Button btnSalir;
	@FXML private TextArea txtTipoMascota;
	@FXML private TextField txtCodigo;
	@FXML private Button btnGrabar;

	TipoMascotaDAO tipoMascotaDAO = new TipoMascotaDAO();
	TipoMascota seleccionado;
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		toolTip = new Tooltip("Grabar tipo de mascota");
		btnGrabar.setStyle("-fx-graphic: url('/grabar.png');-fx-cursor: hand;");
		btnGrabar.setTooltip(toolTip);

		toolTip = new Tooltip("Salir");
		btnSalir.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
		btnSalir.setTooltip(toolTip);
		
		txtCodigo.setText("0");
		if(Context.getInstance().getTipoMascota() != null) {
			seleccionado = Context.getInstance().getTipoMascota();
			Context.getInstance().setTipoMascota(null);
			cargarDatos();
		}else {
			seleccionado = new TipoMascota();
			seleccionado.setIdTipoMascota(null);
		}
	}
	private void cargarDatos() {
		txtCodigo.setText(String.valueOf(seleccionado.getIdTipoMascota()));
		txtTipoMascota.setText(seleccionado.getDescripcion());
	}
	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}
			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				seleccionado.setDescripcion(txtTipoMascota.getText());
				seleccionado.setEstado("A");
				
				if(seleccionado.getIdTipoMascota() != null) {//modifica
					tipoMascotaDAO.getEntityManager().getTransaction().begin();
					tipoMascotaDAO.getEntityManager().merge(seleccionado);
					tipoMascotaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}else {//inserta
					tipoMascotaDAO.getEntityManager().getTransaction().begin();
					tipoMascotaDAO.getEntityManager().persist(seleccionado);
					tipoMascotaDAO.getEntityManager().getTransaction().commit();
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
			if(txtTipoMascota.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar la raza", Context.getInstance().getStage());
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
