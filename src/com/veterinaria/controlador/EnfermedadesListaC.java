package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Enfermedad;
import com.veterinaria.modelo.EnfermedadDAO;
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

public class EnfermedadesListaC {
	Tooltip toolTip;
	@FXML private TableView<Enfermedad> tvDatos;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;

	EnfermedadDAO enfermedadDAO = new EnfermedadDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Editar enfermedad");
			btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnEditar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Eliminar enfermedad");
			btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
			btnEliminar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Nueva enfermedad");
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
						buscar();
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
			List<Enfermedad> lista;
			ObservableList<Enfermedad> datos = FXCollections.observableArrayList();
			lista = enfermedadDAO.getListaEnfermedades(busqueda);
			datos.setAll(lista);
			//llenar los datos en la tabla
			TableColumn<Enfermedad, String> codigoColum = new TableColumn<>("Código");
			codigoColum.setMinWidth(10);
			codigoColum.setPrefWidth(90);
			codigoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Enfermedad,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Enfermedad, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdEnfermedad()));
				}
			});
			TableColumn<Enfermedad, String> enfermedadColum = new TableColumn<>("Enfermedad");
			enfermedadColum.setMinWidth(10);
			enfermedadColum.setPrefWidth(100);
			enfermedadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Enfermedad,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Enfermedad, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNombre());
				}
			});
			TableColumn<Enfermedad, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(200);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Enfermedad,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Enfermedad, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getDescripcion());
				}
			});
			TableColumn<Enfermedad, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Enfermedad,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Enfermedad, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstado());
				}
			});
			tvDatos.getColumns().addAll(codigoColum, enfermedadColum,descripcionColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscar() {
		try {
			List<Enfermedad> lista;
			ObservableList<Enfermedad> datos = FXCollections.observableArrayList();
			lista = enfermedadDAO.getListaEnfermedades(txtBuscar.getText().toString());
			datos.setAll(lista);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo() {
		try {
			Context.getInstance().setEnfermedad(null);
			helper.abrirPantallaModal("/administracion/FrmEnfermedadesEditar.fxml","Registro de enfermedades", Context.getInstance().getStage());
			llenarDatos("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void editar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setEnfermedad(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/administracion/FrmEnfermedadesEditar.fxml","Registro de enfermedades", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar una enfermedad a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void eliminar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja al cliente.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					Enfermedad seleccion = new Enfermedad();
					seleccion = tvDatos.getSelectionModel().getSelectedItem();
					seleccion.setEstado("I");
					enfermedadDAO.getEntityManager().getTransaction().begin();
					enfermedadDAO.getEntityManager().merge(seleccion);
					enfermedadDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Enfermedad Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar una Enfermedad a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
