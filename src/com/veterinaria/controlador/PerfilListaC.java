package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Perfil;
import com.veterinaria.modelo.PerfilDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class PerfilListaC {
	Tooltip toolTip;
	@FXML private TableView<Perfil> tvDatos;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;

	PerfilDAO perfilDAO = new PerfilDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Editar perfil");
			btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnEditar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Eliminar perfil");
			btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
			btnEliminar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Nuevo perfil");
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
						buscarPerfil();
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
			List<Perfil> listaPerfiles;
			ObservableList<Perfil> datos = FXCollections.observableArrayList();
			listaPerfiles = perfilDAO.getListaPerfiles(busqueda);
			datos.setAll(listaPerfiles);
			
			//llenar los datos en la tabla
			TableColumn<Perfil, String> cedulaColum = new TableColumn<>("Código");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(90);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Perfil,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Perfil, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdPerfil()));
				}
			});
			
			TableColumn<Perfil, String> nombresColum = new TableColumn<>("Perfil");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Perfil,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Perfil, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getPerfil());
				}
			});
			
			TableColumn<Perfil, String> cargoColum = new TableColumn<>("Estado");
			cargoColum.setMinWidth(10);
			cargoColum.setPrefWidth(100);
			cargoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Perfil,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Perfil, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstado());
				}
			});
			tvDatos.getColumns().addAll(cedulaColum, nombresColum,cargoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarPerfil() {
		try {
			List<Perfil> listaPerfil;
			ObservableList<Perfil> datos = FXCollections.observableArrayList();
			listaPerfil = perfilDAO.getListaPerfiles(txtBuscar.getText().toString());
			datos.setAll(listaPerfil);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevoPerfil() {
		try {
			Context.getInstance().setPerfil(null);
			helper.abrirPantallaModal("/seguridad/FrmPerfilEditar.fxml","Registro de perfiles", Context.getInstance().getStage());
			llenarDatos("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void editarPerfil() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setPerfil(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/seguridad/FrmPerfilEditar.fxml","Registro de perfiles", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un perfil a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void eliminarPerfil() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja al perfil.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					Perfil perfilSeleccionado = new Perfil();
					perfilSeleccionado = tvDatos.getSelectionModel().getSelectedItem();
					perfilSeleccionado.setEstado("I");
					perfilDAO.getEntityManager().getTransaction().begin();
					perfilDAO.getEntityManager().merge(perfilSeleccionado);
					perfilDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Perfil Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Perfil a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
