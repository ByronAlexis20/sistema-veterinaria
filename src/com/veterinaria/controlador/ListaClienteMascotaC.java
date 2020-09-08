package com.veterinaria.controlador;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Cliente;
import com.veterinaria.modelo.ClienteDAO;
import com.veterinaria.modelo.ClienteMascota;
import com.veterinaria.modelo.ClienteMascotaDAO;
import com.veterinaria.modelo.Mascota;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class ListaClienteMascotaC {
	Tooltip toolTip;
	@FXML private TableView<Cliente> tvDatos;
	@FXML private TextField txtBuscar;
	ClienteDAO empleadoDAO = new ClienteDAO();
	ControllerHelper helper = new ControllerHelper();
	ClienteMascotaDAO clienteMascotaDAO = new ClienteMascotaDAO();
	public void initialize() {
		try {
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarCliente();
					} 
				} 
			}); 
			tvDatos.setRowFactory(tv -> {
	            TableRow<Cliente> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	                		//grabar el cliente
	                		Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea asignar dueño a la mascota?",Context.getInstance().getStage());
	        				if(result.get() == ButtonType.OK){
	        					Mascota mascota = Context.getInstance().getMascota();
	        					Cliente  cliente = tvDatos.getSelectionModel().getSelectedItem();
	        					ClienteMascota clienteMascotaGrabar = new ClienteMascota();
	        					clienteMascotaGrabar.setCliente(cliente);
	        					clienteMascotaGrabar.setEstado("A");
	        					clienteMascotaGrabar.setFechaRegistro(new Date());
	        					clienteMascotaGrabar.setIdClienteMascota(null);
	        					clienteMascotaGrabar.setMascota(mascota);
	        					
	        					clienteMascotaDAO.getEntityManager().getTransaction().begin();
	        					clienteMascotaDAO.getEntityManager().persist(clienteMascotaGrabar);
	        					clienteMascotaDAO.getEntityManager().getTransaction().commit();;
	        					Context.getInstance().getStageModal().close();
	        					helper.mostrarAlertaInformacion("Dueño asignado a la mascota", Context.getInstance().getStage());
	        				}
	    				}
	                }
	            });
	            return row ;
	        });
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarCliente() {
		try {
			List<Cliente> lista;
			ObservableList<Cliente> datos = FXCollections.observableArrayList();
			lista = empleadoDAO.getListaClientes(txtBuscar.getText().toString());
			datos.setAll(lista);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarDatos(String busqueda) {
		try {
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<Cliente> lista;
			ObservableList<Cliente> datos = FXCollections.observableArrayList();
			lista = empleadoDAO.getListaClientes(busqueda);
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<Cliente, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(90);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCedula());
				}
			});
			
			TableColumn<Cliente, String> nombresColum = new TableColumn<>("Apellidos y nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					String nombres = param.getValue().getNombre() + " " + param.getValue().getApellido();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			
			TableColumn<Cliente, String> telefonoColum = new TableColumn<>("Telefono");
			telefonoColum.setMinWidth(10);
			telefonoColum.setPrefWidth(100);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getTelefono());
				}
			});
			tvDatos.getColumns().addAll(cedulaColum, nombresColum,telefonoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
