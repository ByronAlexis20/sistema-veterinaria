package com.veterinaria.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Cliente;
import com.veterinaria.modelo.ClienteDAO;
import com.veterinaria.modelo.ClienteMascota;
import com.veterinaria.modelo.ClienteMascotaDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class AsignarMascotaC {
	Tooltip toolTip;
	@FXML private TableView<ClienteMascota> tvDatosMascotas;
	@FXML private TextField txtDireccion;
	@FXML private Button btnSeleccionar;
	@FXML private TextField txtTelefono;
	@FXML private TableView<Cliente> tvDatosClientes;
	@FXML private TextField txtNombres;
	@FXML private Button btnQuitarMascota;
	@FXML private Button btnAgregarMascota;
	@FXML private TextField txtBuscar;
	@FXML private TextField txtCedula;
	
	ClienteDAO clienteDAO = new ClienteDAO();
	Cliente clienteSeleccionado;
	ControllerHelper helper = new ControllerHelper();
	ClienteMascotaDAO clienteMascotaDAO = new ClienteMascotaDAO();
	public void initialize() {
		try {
			toolTip = new Tooltip("Quitar mascota");
			btnQuitarMascota.setStyle("-fx-cursor: hand;");
			btnQuitarMascota.setTooltip(toolTip);

			toolTip = new Tooltip("Agregar mascota");
			btnAgregarMascota.setStyle("-fx-cursor: hand;");
			btnAgregarMascota.setTooltip(toolTip);
			
			toolTip = new Tooltip("Seleccionar cliente");
			btnSeleccionar.setStyle("-fx-cursor: hand;");
			btnSeleccionar.setTooltip(toolTip);
			
			cargarClientes("");
			llenarDatosMascota();
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						cargarClientes(txtBuscar.getText());
					} 
				} 
			}); 
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarDatosMascota() {
		try {
			tvDatosMascotas.getColumns().clear();
			tvDatosMascotas.getItems().clear();
			//llenar los datos en la tabla
			TableColumn<ClienteMascota, String> tipoMascotaColum = new TableColumn<>("Tipo de mascota");
			tipoMascotaColum.setMinWidth(10);
			tipoMascotaColum.setPrefWidth(110);
			tipoMascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMascota().getRaza().getTipoMascota().getDescripcion());
				}
			});
			TableColumn<ClienteMascota, String> razaColum = new TableColumn<>("Raza");
			razaColum.setMinWidth(10);
			razaColum.setPrefWidth(200);
			razaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMascota().getRaza().getDescripcion());
				}
			});
			TableColumn<ClienteMascota, String> nombreColum = new TableColumn<>("Nombre");
			nombreColum.setMinWidth(10);
			nombreColum.setPrefWidth(200);
			nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMascota().getNombre());
				}
			});
			tvDatosMascotas.getColumns().addAll(tipoMascotaColum, razaColum,nombreColum);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void cargarClientes(String busqueda) {
		try {
			tvDatosClientes.getColumns().clear();
			tvDatosClientes.getItems().clear();
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
			telefonoColum.setPrefWidth(120);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getTelefono());
				}
			});
			tvDatosClientes.getColumns().addAll(cedulaColum, nombresColum,telefonoColum);
			tvDatosClientes.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void cargarCliente() {
		try {
			if(tvDatosClientes.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un cliente", Context.getInstance().getStage());
				return;
			}
			clienteSeleccionado = tvDatosClientes.getSelectionModel().getSelectedItem();
			txtCedula.setText(clienteSeleccionado.getCedula());
			txtNombres.setText(clienteSeleccionado.getNombre() + " " + clienteSeleccionado.getApellido());
			txtTelefono.setText(clienteSeleccionado.getTelefono());
			txtDireccion.setText(clienteSeleccionado.getDireccion());
			if(clienteSeleccionado.getClienteMascotas().size() > 0)
				cargarMascotas(clienteSeleccionado);
			else
				tvDatosMascotas.getItems().clear();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void cargarMascotas(Cliente cliente) {
		try {
			List<ClienteMascota> listaMascotas = new ArrayList<>();
			listaMascotas = clienteMascotaDAO.getListaMascotaPorCliente("", cliente.getIdCliente());
			tvDatosMascotas.getColumns().clear();
			tvDatosMascotas.getItems().clear();
			ObservableList<ClienteMascota> datos = FXCollections.observableArrayList();
			datos.setAll(listaMascotas);
			//llenar los datos en la tabla
			TableColumn<ClienteMascota, String> tipoMascotaColum = new TableColumn<>("Tipo de mascota");
			tipoMascotaColum.setMinWidth(10);
			tipoMascotaColum.setPrefWidth(110);
			tipoMascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMascota().getRaza().getTipoMascota().getDescripcion());
				}
			});
			TableColumn<ClienteMascota, String> razaColum = new TableColumn<>("Raza");
			razaColum.setMinWidth(10);
			razaColum.setPrefWidth(200);
			razaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMascota().getRaza().getDescripcion());
				}
			});
			TableColumn<ClienteMascota, String> nombreColum = new TableColumn<>("Nombre");
			nombreColum.setMinWidth(10);
			nombreColum.setPrefWidth(200);
			nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMascota().getNombre());
				}
			});
			tvDatosMascotas.getColumns().addAll(tipoMascotaColum, razaColum,nombreColum);
			tvDatosMascotas.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void quitarMascota() {
		try {
			if(tvDatosMascotas.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar quitar la mascota.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					ClienteMascota seleccion = new ClienteMascota();
					seleccion = tvDatosMascotas.getSelectionModel().getSelectedItem();
					seleccion.setEstado("I");
					clienteDAO.getEntityManager().getTransaction().begin();
					clienteDAO.getEntityManager().merge(seleccion);
					clienteDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Cliente Dado de Baja", Context.getInstance().getStage());
					tvDatosMascotas.getItems().remove(seleccion);
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Cliente a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void agregarMascota() {
		try {
			if(clienteSeleccionado == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un cliente", Context.getInstance().getStage());
				return;
			}
			Context.getInstance().setClienteMascota(null);
			Context.getInstance().setCliente(clienteSeleccionado);
			helper.abrirPantallaModal("/clientes/FrmMascotaSinDueno.fxml","Consulta de mascotas sin dueño", Context.getInstance().getStage());
			cargarMascotas(clienteSeleccionado);
			Context.getInstance().setClienteMascota(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}