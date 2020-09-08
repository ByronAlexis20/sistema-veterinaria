package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.TipoMascota;
import com.veterinaria.modelo.TipoMascotaDAO;
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

public class TipoMascotaC {
	Tooltip toolTip;
	@FXML private TableView<TipoMascota> tvDatos;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;

	TipoMascotaDAO tipoMascotaDAO = new TipoMascotaDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Editar tipo de mascota");
			btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnEditar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Eliminar Tipo de mascota");
			btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
			btnEliminar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Nuevo Tipo de mascota");
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
			List<TipoMascota> lista;
			ObservableList<TipoMascota> datos = FXCollections.observableArrayList();
			lista = tipoMascotaDAO.getListaTipoMascota(busqueda);
			datos.setAll(lista);
			//llenar los datos en la tabla
			TableColumn<TipoMascota, String> codigoColum = new TableColumn<>("Código");
			codigoColum.setMinWidth(10);
			codigoColum.setPrefWidth(90);
			codigoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TipoMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<TipoMascota, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdTipoMascota()));
				}
			});
			TableColumn<TipoMascota, String> tipoMascotaColum = new TableColumn<>("Tipo de mascota");
			tipoMascotaColum.setMinWidth(10);
			tipoMascotaColum.setPrefWidth(150);
			tipoMascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TipoMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<TipoMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getDescripcion());
				}
			});
			
			TableColumn<TipoMascota, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TipoMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<TipoMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstado());
				}
			});
			tvDatos.getColumns().addAll(codigoColum, tipoMascotaColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscar() {
		try {
			List<TipoMascota> lista;
			ObservableList<TipoMascota> datos = FXCollections.observableArrayList();
			lista = tipoMascotaDAO.getListaTipoMascota(txtBuscar.getText().toString());
			datos.setAll(lista);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo() {
		try {
			Context.getInstance().setTipoMascota(null);
			helper.abrirPantallaModal("/administracion/FrmTipoMascotaEditar.fxml","Registro de tipos de mascotas", Context.getInstance().getStage());
			llenarDatos("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void editar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setTipoMascota(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/administracion/FrmTipoMascotaEditar.fxml","Registro de tipos de mascotas", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un tipo de mascota a Editar", Context.getInstance().getStage());
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
					TipoMascota seleccion = new TipoMascota();
					seleccion = tvDatos.getSelectionModel().getSelectedItem();
					seleccion.setEstado("I");
					tipoMascotaDAO.getEntityManager().getTransaction().begin();
					tipoMascotaDAO.getEntityManager().merge(seleccion);
					tipoMascotaDAO.getEntityManager().getTransaction().commit();
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
