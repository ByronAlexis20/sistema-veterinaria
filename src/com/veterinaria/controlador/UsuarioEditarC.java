package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Perfil;
import com.veterinaria.modelo.PerfilDAO;
import com.veterinaria.modelo.Usuario;
import com.veterinaria.modelo.UsuarioDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;
import com.veterinaria.util.Encriptado;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class UsuarioEditarC {
	Tooltip toolTip;
	@FXML private Button btnSalir;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtTelefono;
	@FXML private ComboBox<Perfil> cboPerfil;
	@FXML private TextField txtNombres;
	@FXML private TextField txtCedula;
	@FXML private Button btnGrabar;
	@FXML private PasswordField txtClave;
	@FXML private TextField txtUsuario;
	private int longitudCedula = 13;
	private int longitudTelefono = 15;

	PerfilDAO perfilDAO = new PerfilDAO();
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	Usuario usuarioSeleccionado;
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			restricciones();
			llenarCombos();
			
			cboPerfil.setStyle("-fx-cursor: hand;");

			toolTip = new Tooltip("Grabar usuario");
			btnGrabar.setStyle("-fx-graphic: url('/grabar.png');-fx-cursor: hand;");
			btnGrabar.setTooltip(toolTip);

			toolTip = new Tooltip("Salir");
			btnSalir.setStyle("-fx-graphic: url('/salir.png');-fx-cursor: hand;");
			btnSalir.setTooltip(toolTip);
			if(Context.getInstance().getUsuarioSeleccionado() != null) {
				usuarioSeleccionado = Context.getInstance().getUsuarioSeleccionado();
				Context.getInstance().setUsuarioSeleccionado(null);
				cargarDatos();
			}else {
				usuarioSeleccionado = new Usuario();
				usuarioSeleccionado.setIdUsuario(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarDatos() {
		
		txtApellidos.setText(usuarioSeleccionado.getApellido());
		txtDireccion.setText(usuarioSeleccionado.getDireccion());
		txtTelefono.setText(usuarioSeleccionado.getTelefono());
		txtNombres.setText(usuarioSeleccionado.getNombre());
		txtCedula.setText(usuarioSeleccionado.getCedula());
		txtClave.setText(Encriptado.Desencriptar(usuarioSeleccionado.getClave()));
		txtUsuario.setText(Encriptado.Desencriptar(usuarioSeleccionado.getUsuario()));
		cboPerfil.getSelectionModel().select(usuarioSeleccionado.getPerfil());
	}
	
	private void llenarCombos() {
		try {
			cboPerfil.getItems().clear();
			ObservableList<Perfil> datos = FXCollections.observableArrayList();
			List<Perfil> lista = perfilDAO.getListaPerfilesActivos();
			datos.addAll(lista);
			cboPerfil.setItems(datos);
			cboPerfil.setPromptText("Seleccione cargo");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
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
		txtDireccion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String cadena = txtDireccion.getText().toUpperCase();
				txtDireccion.setText(cadena);
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
				usuarioSeleccionado.setApellido(txtApellidos.getText());
				usuarioSeleccionado.setCedula(txtCedula.getText());
				usuarioSeleccionado.setClave(Encriptado.Encriptar(txtClave.getText()));
				usuarioSeleccionado.setDireccion(txtDireccion.getText());
				usuarioSeleccionado.setEstado("A");
				usuarioSeleccionado.setNombre(txtNombres.getText());
				usuarioSeleccionado.setPerfil(cboPerfil.getSelectionModel().getSelectedItem());
				usuarioSeleccionado.setTelefono(txtTelefono.getText());
				usuarioSeleccionado.setUsuario(Encriptado.Encriptar(txtUsuario.getText()));
				
				if(usuarioSeleccionado.getIdUsuario() != null) {//modifica
					usuarioDAO.getEntityManager().getTransaction().begin();
					usuarioDAO.getEntityManager().merge(usuarioSeleccionado);
					usuarioDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					salir();
				}else {//inserta
					usuarioDAO.getEntityManager().getTransaction().begin();
					usuarioDAO.getEntityManager().persist(usuarioSeleccionado);
					usuarioDAO.getEntityManager().getTransaction().commit();
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
			if(usuarioSeleccionado.getIdUsuario() == null) {
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
			if(cboPerfil.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un cargo", Context.getInstance().getStage());
				return false;
			}
			if(txtUsuario.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar nombre de usuario", Context.getInstance().getStage());
				return false;
			}
			if(validarUsuario() == true) {
				helper.mostrarAlertaAdvertencia("El usuario ya existe!!", Context.getInstance().getStage());
				txtUsuario.requestFocus();
				return false;	
			}
			if(txtClave.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar contraseña para el usuario", Context.getInstance().getStage());
				return false;
			}
			bandera = true;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}
	boolean validarUsuario() {
		try {
			List<Usuario> listaUsuario;
			listaUsuario = usuarioDAO.getValidarUsuario(Encriptado.Encriptar(txtUsuario.getText()), usuarioSeleccionado.getIdUsuario());
			if(listaUsuario.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	private boolean verificarCedula() {
		try {
			boolean bandera = false;
			List<Usuario> listaUsuario = usuarioDAO.getBuscarPorCedula(txtCedula.getText());
			if(listaUsuario.size() > 0) {
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
