package com.veterinaria.controlador;

import java.util.Optional;

import com.veterinaria.modelo.Perfil;
import com.veterinaria.modelo.PerfilDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;


public class PerfilEditarC {
	Tooltip toolTip;
	@FXML private TextField txtCodigo;
	@FXML private TextField txtPerfil;
	@FXML private TextField txtDescripcion;
	@FXML private Button btnGrabar;
	@FXML private Button btnSalir;
	Perfil perfilSeleccionado;
	ControllerHelper helper = new ControllerHelper();
	PerfilDAO perfilDAO = new PerfilDAO();
	
	public void initialize() {
		try {
			txtCodigo.setEditable(false);
			toolTip = new Tooltip("Grabar perfil");
			btnGrabar.setStyle("-fx-graphic: url('/grabar.png');-fx-cursor: hand;");
			btnGrabar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Salir");
			btnSalir.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
			btnSalir.setTooltip(toolTip);
			
			toolTip = new Tooltip("Agregar cargos");
			
			limpiar();
			if(Context.getInstance().getPerfil() != null) {
				perfilSeleccionado = Context.getInstance().getPerfil();
				Context.getInstance().setPerfil(null);
				cargarDatosPerfil();
			}else {
				perfilSeleccionado = new Perfil();
				perfilSeleccionado.setIdPerfil(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void grabar() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				perfilSeleccionado.setDescripcion(txtDescripcion.getText());
				perfilSeleccionado.setPerfil(txtPerfil.getText());
				perfilSeleccionado.setEstado("A");
				
				if(perfilSeleccionado.getIdPerfil() != null) {//modifica
					perfilDAO.getEntityManager().getTransaction().begin();
					perfilDAO.getEntityManager().merge(perfilSeleccionado);
					perfilDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}else {//inserta
					perfilDAO.getEntityManager().getTransaction().begin();
					perfilDAO.getEntityManager().persist(perfilSeleccionado);
					perfilDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
		}
	}
	private void cargarDatosPerfil() {
		try {
			txtCodigo.setText(String.valueOf(perfilSeleccionado.getIdPerfil()));
			txtPerfil.setText(perfilSeleccionado.getPerfil());
			txtDescripcion.setText(perfilSeleccionado.getDescripcion());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void limpiar() {
		txtCodigo.setText("0");
		txtPerfil.setText("");
		txtDescripcion.setText("");
	}
	public void salir() {
		try {
			Context.getInstance().getStageModal().close();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
