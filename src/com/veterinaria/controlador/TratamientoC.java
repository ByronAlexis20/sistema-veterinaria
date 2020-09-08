package com.veterinaria.controlador;

import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;

public class TratamientoC {
	Tooltip toolTip;
	@FXML private Button btnSalir;
	@FXML private Button btnAgregar;
	@FXML private TextArea txtTratamiento;
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		toolTip = new Tooltip("Agregar");
		btnAgregar.setStyle("-fx-graphic: url('/agregar.png');-fx-cursor: hand;");
		btnAgregar.setTooltip(toolTip);

		toolTip = new Tooltip("Salir");
		btnSalir.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
		btnSalir.setTooltip(toolTip);
	}
	public void agregar() {
		if(txtTratamiento.getText().equals("")) {
			helper.mostrarAlertaAdvertencia("Debe registrar un tratamiento", Context.getInstance().getStage());
			return;
		}
		Context.getInstance().setTratamiento(txtTratamiento.getText());
		salir();
	}
	
	public void salir() {
		Context.getInstance().getStageModal().close();
	}
}
