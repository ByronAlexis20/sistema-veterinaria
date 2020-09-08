package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Mascota;
import com.veterinaria.modelo.MascotaDAO;
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

public class MascotaListaC {
	Tooltip toolTip;
	@FXML private TableView<Mascota> tvDatos;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;

	MascotaDAO mascotaDAO = new MascotaDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Editar mascota");
			btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnEditar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Eliminar mascota");
			btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
			btnEliminar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Nueva mascota");
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
			List<Mascota> lista;
			ObservableList<Mascota> datos = FXCollections.observableArrayList();
			lista = mascotaDAO.getListaMascotas(busqueda);
			datos.setAll(lista);
			//llenar los datos en la tabla
			TableColumn<Mascota, String> codigoColum = new TableColumn<>("Código");
			codigoColum.setMinWidth(10);
			codigoColum.setPrefWidth(90);
			codigoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdMascota()));
				}
			});
			TableColumn<Mascota, String> tipoMascotaColum = new TableColumn<>("Tipo de mascota");
			tipoMascotaColum.setMinWidth(10);
			tipoMascotaColum.setPrefWidth(110);
			tipoMascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRaza().getTipoMascota().getDescripcion());
				}
			});
			TableColumn<Mascota, String> razaColum = new TableColumn<>("Raza");
			razaColum.setMinWidth(10);
			razaColum.setPrefWidth(200);
			razaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRaza().getDescripcion());
				}
			});
			TableColumn<Mascota, String> nombreColum = new TableColumn<>("Nombre ");
			nombreColum.setMinWidth(10);
			nombreColum.setPrefWidth(200);
			nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNombre());
				}
			});
			TableColumn<Mascota, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstado());
				}
			});
			tvDatos.getColumns().addAll(codigoColum,tipoMascotaColum, razaColum,nombreColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscar() {
		try {
			List<Mascota> lista;
			ObservableList<Mascota> datos = FXCollections.observableArrayList();
			lista = mascotaDAO.getListaMascotas(txtBuscar.getText().toString());
			datos.setAll(lista);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo() {
		try {
			Context.getInstance().setRaza(null);
			helper.abrirPantallaModal("/mascotas/FrmMascotaEditar.fxml","Registro de mascota", Context.getInstance().getStage());
			llenarDatos("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void editar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setMascota(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/mascotas/FrmMascotaEditar.fxml","Registro de mascotas", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar una mascota a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void eliminar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja a la mascota.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					Mascota seleccion = new Mascota();
					seleccion = tvDatos.getSelectionModel().getSelectedItem();
					seleccion.setEstado("I");
					mascotaDAO.getEntityManager().getTransaction().begin();
					mascotaDAO.getEntityManager().merge(seleccion);
					mascotaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Mascota Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar una Mascota a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
