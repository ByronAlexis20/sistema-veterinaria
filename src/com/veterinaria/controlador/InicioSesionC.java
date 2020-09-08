package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Usuario;
import com.veterinaria.modelo.UsuarioDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;
import com.veterinaria.util.Encriptado;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InicioSesionC {
	Tooltip toolTip;
	@FXML private ImageView pbFoto;
	@FXML private Button btnAceptar;
	@FXML private Button btnCancelar;
	@FXML private TextField txtUsuario;
	@FXML private PasswordField txtContrasenia;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		try {
			toolTip = new Tooltip("Aceptar");
			btnAceptar.setStyle("-fx-graphic: url('/aceptar.png');-fx-cursor: hand;");
			btnAceptar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Cancelar");
			btnCancelar.setStyle("-fx-graphic: url('/cancelar.png');-fx-cursor: hand;");
			btnCancelar.setTooltip(toolTip);
			
			Image image = new Image("inicio.png");
			pbFoto.setImage(image);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void aceptar() {
		try {
			List<Usuario> usuario;
    		usuario = usuarioDAO.getUsuario(Encriptado.Encriptar(txtUsuario.getText()),Encriptado.Encriptar(txtContrasenia.getText()));
    		if(usuario.size() == 1){
    			Context.getInstance().setUsuario(usuario.get(0));
    			helper.abrirPantallaPrincipal();
    		}else {
    			helper.mostrarAlertaError("Usuario o clave incorrecto", Context.getInstance().getStage());
    		}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void cancelar() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Salir?",Context.getInstance().getStagePrincipal());
			if(result.get() == ButtonType.OK)
				System.exit(0);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}	
}
