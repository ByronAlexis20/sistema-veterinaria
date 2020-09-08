package com.veterinaria.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Veterinario;
import com.veterinaria.modelo.VeterinarioDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class VeterinarioEditarC {
	@FXML private Button btnSalir;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtProfesion;
	@FXML private TextField txtNombres;
	@FXML private TextField txtCedula;
	@FXML private Button btnGrabar;

	VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
	Veterinario seleccionado;
	ControllerHelper helper = new ControllerHelper();
	private int longitudCedula = 13;
	private int longitudTelefono = 15;
	Tooltip toolTip;

	public void initialize() {
		try {
			restricciones();

			toolTip = new Tooltip("Grabar usuario");
			btnGrabar.setStyle("-fx-graphic: url('/grabar.png');-fx-cursor: hand;");
			btnGrabar.setTooltip(toolTip);

			toolTip = new Tooltip("Salir");
			btnSalir.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
			btnSalir.setTooltip(toolTip);
			if(Context.getInstance().getVeterinario() != null) {
				seleccionado = Context.getInstance().getVeterinario();
				Context.getInstance().setVeterinario(null);
				cargarDatos();
			}else {
				seleccionado = new Veterinario();
				seleccionado.setIdVeterinario(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarDatos() {

		txtApellidos.setText(seleccionado.getApellidos());
		txtProfesion.setText(seleccionado.getProfesion());
		txtTelefono.setText(seleccionado.getTelefono());
		txtNombres.setText(seleccionado.getNombres());
		txtCedula.setText(seleccionado.getCedula());
	}
	private void restricciones() {
		//longitud de las cadenas de textos ingresadas
		txtCedula.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtCedula.getText().length() > longitudCedula) {
					String s = txtCedula.getText().substring(0, longitudCedula);
					txtCedula.setText(s);
				}
			}
		});
		txtTelefono.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtTelefono.getText().length() > longitudTelefono) {
					String s = txtTelefono.getText().substring(0, longitudTelefono);
					txtTelefono.setText(s);
				}
			}
		});
		//validar solo numeros
		txtCedula.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtCedula.setText(oldValue);
				}
			}
		});
		txtTelefono.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtTelefono.setText(oldValue);
				}
			}
		});
		//solo letras mayusculas
		txtApellidos.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String cadena = txtApellidos.getText().toUpperCase();
				txtApellidos.setText(cadena);
			}
		});
		txtProfesion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String cadena = txtProfesion.getText().toUpperCase();
				txtProfesion.setText(cadena);
			}
		});
		txtNombres.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String cadena = txtNombres.getText().toUpperCase();
				txtNombres.setText(cadena);
			}
		});
	}
	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}
			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				seleccionado.setApellidos(txtApellidos.getText());
				seleccionado.setCedula(txtCedula.getText());
				seleccionado.setProfesion(txtProfesion.getText());
				seleccionado.setEstado("A");
				seleccionado.setNombres(txtNombres.getText());
				seleccionado.setTelefono(txtTelefono.getText());
				
				if(seleccionado.getIdVeterinario() != null) {//modifica
					veterinarioDAO.getEntityManager().getTransaction().begin();
					veterinarioDAO.getEntityManager().merge(seleccionado);
					veterinarioDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}else {//inserta
					veterinarioDAO.getEntityManager().getTransaction().begin();
					veterinarioDAO.getEntityManager().persist(seleccionado);
					veterinarioDAO.getEntityManager().getTransaction().commit();
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
			if(txtCedula.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar cédula de identidad", Context.getInstance().getStage());
				return false;
			}
			if(seleccionado.getIdVeterinario() == null) {
				if(verificarCedula() == false) {
					helper.mostrarAlertaAdvertencia("Cédula ya existe en el registro", Context.getInstance().getStage());
					return false;
				}
			}
			if(txtNombres.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar nombres", Context.getInstance().getStage());
				return false;
			}
			if(txtApellidos.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar apellidos", Context.getInstance().getStage());
				return false;
			}			
			bandera = true;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}
	private boolean verificarCedula() {
		try {
			boolean bandera = false;
			List<Veterinario> lista = veterinarioDAO.getBuscarPorCedula(txtCedula.getText());
			if(lista.size() > 0) {
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
	public LocalDate convertToLocalDate(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	public Date convertToDate(LocalDate dateToConvert) {
	    return java.sql.Date.valueOf(dateToConvert);
	}
}
