package com.veterinaria.controlador;

import java.util.Optional;

import com.veterinaria.modelo.Enfermedad;
import com.veterinaria.modelo.EnfermedadDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class EnfermedadesEditarC {
	Tooltip toolTip;
	@FXML private Button btnSalir;
    @FXML private TextField txtEnfermedad;
    @FXML private TextField txtCodigo;
    @FXML private TextArea txtDescripcion;
    @FXML private Button btnGrabar;
    EnfermedadDAO enfermedadDAO = new EnfermedadDAO();
	Enfermedad enfermedadSeleccionado;
	ControllerHelper helper = new ControllerHelper();
    
	public void initialize() {
		try {

			toolTip = new Tooltip("Grabar usuario");
			btnGrabar.setStyle("-fx-graphic: url('/grabar.png');-fx-cursor: hand;");
			btnGrabar.setTooltip(toolTip);

			toolTip = new Tooltip("Salir");
			btnSalir.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
			btnSalir.setTooltip(toolTip);
			
			txtCodigo.setText("0");
			txtEnfermedad.requestFocus();
			if(Context.getInstance().getEnfermedad() != null) {
				enfermedadSeleccionado = Context.getInstance().getEnfermedad();
				Context.getInstance().setEnfermedad(null);
				cargarDatos();
			}else {
				enfermedadSeleccionado = new Enfermedad();
				enfermedadSeleccionado.setIdEnfermedad(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	
	private void cargarDatos() {
		txtCodigo.setText(String.valueOf(enfermedadSeleccionado.getIdEnfermedad()));
		txtEnfermedad.setText(String.valueOf(enfermedadSeleccionado.getNombre()));
		txtDescripcion.setText(String.valueOf(enfermedadSeleccionado.getDescripcion()));
	}
	
    public void grabar() {
    	try {
			if(validarDatos() == false) {
				return;
			}
			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				enfermedadSeleccionado.setDescripcion(txtDescripcion.getText());
				enfermedadSeleccionado.setNombre(txtEnfermedad.getText());
				enfermedadSeleccionado.setEstado("A");
				
				if(enfermedadSeleccionado.getIdEnfermedad() != null) {//modifica
					enfermedadDAO.getEntityManager().getTransaction().begin();
					enfermedadDAO.getEntityManager().merge(enfermedadSeleccionado);
					enfermedadDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}else {//inserta
					enfermedadDAO.getEntityManager().getTransaction().begin();
					enfermedadDAO.getEntityManager().persist(enfermedadSeleccionado);
					enfermedadDAO.getEntityManager().getTransaction().commit();
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
			if(txtEnfermedad.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar la enfermedad", Context.getInstance().getStage());
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
