package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Cliente;
import com.veterinaria.modelo.ClienteDAO;
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

public class ClientesListaC {
	Tooltip toolTip;
	@FXML private TableView<Cliente> tvDatos;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;

	ClienteDAO clienteDAO = new ClienteDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			toolTip = new Tooltip("Editar cliente");
			btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnEditar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Eliminar cliente");
			btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
			btnEliminar.setTooltip(toolTip);
			
			toolTip = new Tooltip("Nuevo cliente");
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
			List<Cliente> listaClientes;
			ObservableList<Cliente> datos = FXCollections.observableArrayList();
			listaClientes = clienteDAO.getListaClientes(busqueda);
			datos.setAll(listaClientes);
			
			//llenar los datos en la tabla
			TableColumn<Cliente, String> cedulaColum = new TableColumn<>("Código");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(90);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdCliente()));
				}
			});
			
			TableColumn<Cliente, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					String nombres = param.getValue().getNombre() + " " + param.getValue().getApellido();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			TableColumn<Cliente, String> telefonoColum = new TableColumn<>("Teléfono");
			telefonoColum.setMinWidth(10);
			telefonoColum.setPrefWidth(100);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getTelefono());
				}
			});
			
			TableColumn<Cliente, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
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
			List<Cliente> listaCliente;
			ObservableList<Cliente> datos = FXCollections.observableArrayList();
			listaCliente = clienteDAO.getListaClientes(txtBuscar.getText().toString());
			datos.setAll(listaCliente);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevoCliente() {
		try {
			helper.abrirPantallaModal("/clientes/FrmClientesEditar.fxml","Registro de clientes", Context.getInstance().getStage());
			llenarDatos("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void editarCliente() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setCliente(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/clientes/FrmClientesEditar.fxml","Registro de clientes", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Usuario a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void eliminarCliente() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja al cliente.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					Cliente seleccion = new Cliente();
					seleccion = tvDatos.getSelectionModel().getSelectedItem();
					seleccion.setEstado("I");
					clienteDAO.getEntityManager().getTransaction().begin();
					clienteDAO.getEntityManager().merge(seleccion);
					clienteDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Cliente Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Cliente a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
