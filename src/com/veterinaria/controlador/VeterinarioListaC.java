package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Veterinario;
import com.veterinaria.modelo.VeterinarioDAO;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class VeterinarioListaC {
	Tooltip toolTip;
	@FXML private TableView<Veterinario> tvDatos;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;

	VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Editar veterinario");
			btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnEditar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Eliminar veterinario");
			btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
			btnEliminar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Nuevo veterinario");
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
			List<Veterinario> lista;
			ObservableList<Veterinario> datos = FXCollections.observableArrayList();
			lista = veterinarioDAO.getListaVeterinarios(busqueda);
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<Veterinario, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(100);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Veterinario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Veterinario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCedula()));
				}
			});
			
			TableColumn<Veterinario, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Veterinario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Veterinario, String> param) {
					String nombres = param.getValue().getNombres() + " " + param.getValue().getApellidos();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			TableColumn<Veterinario, String> telefonoColum = new TableColumn<>("Teléfono");
			telefonoColum.setMinWidth(10);
			telefonoColum.setPrefWidth(100);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Veterinario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Veterinario, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getTelefono());
				}
			});
			
			TableColumn<Veterinario, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Veterinario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Veterinario, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstado());
				}
			});
			tvDatos.getColumns().addAll(cedulaColum, nombresColum,telefonoColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarUsuario() {
		try {
			List<Veterinario> listaCliente;
			ObservableList<Veterinario> datos = FXCollections.observableArrayList();
			listaCliente = veterinarioDAO.getListaVeterinarios(txtBuscar.getText().toString());
			datos.setAll(listaCliente);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo() {
		try {
			Context.getInstance().setVeterinario(null);
			helper.abrirPantallaModal("/administracion/FrmVeterinarioEditar.fxml","Registro de veterinarios", Context.getInstance().getStage());
			llenarDatos("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void editar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setVeterinario(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/administracion/FrmVeterinarioEditar.fxml","Registro de veterinarios", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Veterinario a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void eliminar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja al Veterinario.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					Veterinario seleccion = new Veterinario();
					seleccion = tvDatos.getSelectionModel().getSelectedItem();
					seleccion.setEstado("I");
					veterinarioDAO.getEntityManager().getTransaction().begin();
					veterinarioDAO.getEntityManager().merge(seleccion);
					veterinarioDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Veterinario Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Veterinario a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
