package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Usuario;
import com.veterinaria.modelo.UsuarioDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;
import com.veterinaria.util.Encriptado;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class UsuarioListaC {
	Tooltip toolTip;
	@FXML private TableView<Usuario> tvDatos;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;

	UsuarioDAO usuarioDAO = new UsuarioDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Editar usuario");
			btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnEditar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Eliminar usuario");
			btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
			btnEliminar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Nuevo usuario");
			btnNuevo.setStyle("-fx-graphic: url('/nuevo.png');-fx-cursor: hand;");
			btnNuevo.setTooltip(toolTip);
			
			llenarDatos("");
			txtBuscar.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtBuscar.getText().toUpperCase();
					txtBuscar.setText(cadena);
				}
			});
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarUsuario();
					} 
				} 
			}); 
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void llenarDatos(String busqueda) {
		try {
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<Usuario> listaUsuarios;
			ObservableList<Usuario> datos = FXCollections.observableArrayList();
			listaUsuarios = usuarioDAO.getListaUsuarios(busqueda);
			datos.setAll(listaUsuarios);
			
			//llenar los datos en la tabla
			TableColumn<Usuario, String> cedulaColum = new TableColumn<>("Código");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(90);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Usuario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdUsuario()));
				}
			});
			
			TableColumn<Usuario, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Usuario, String> param) {
					String nombres = param.getValue().getNombre() + " " + param.getValue().getApellido();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			TableColumn<Usuario, String> usuarioColum = new TableColumn<>("Usuario");
			usuarioColum.setMinWidth(10);
			usuarioColum.setPrefWidth(100);
			usuarioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Usuario, String> param) {
					return new SimpleObjectProperty<String>(Encriptado.Desencriptar(param.getValue().getUsuario()));
				}
			});
			
			TableColumn<Usuario, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Usuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Usuario, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstado());
				}
			});
			tvDatos.getColumns().addAll(cedulaColum, nombresColum,usuarioColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarUsuario() {
		try {
			List<Usuario> listaUsuario;
			ObservableList<Usuario> datos = FXCollections.observableArrayList();
			listaUsuario = usuarioDAO.getListaUsuarios(txtBuscar.getText().toString());
			datos.setAll(listaUsuario);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevoUsuario() {
		try {
			Context.getInstance().setUsuarioSeleccionado(null);
			helper.abrirPantallaModal("/seguridad/FrmUsuarioEditar.fxml","Registro de usuarios", Context.getInstance().getStage());
			llenarDatos("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void editarUsuario() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setUsuarioSeleccionado(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/seguridad/FrmUsuarioEditar.fxml","Registro de usuarios", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Usuario a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void eliminarUsuario() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja al usuario.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					Usuario usuarioSeleccionado = new Usuario();
					usuarioSeleccionado = tvDatos.getSelectionModel().getSelectedItem();
					usuarioSeleccionado.setEstado("I");
					usuarioDAO.getEntityManager().getTransaction().begin();
					usuarioDAO.getEntityManager().merge(usuarioSeleccionado);
					usuarioDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Usuario Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Usuario a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
