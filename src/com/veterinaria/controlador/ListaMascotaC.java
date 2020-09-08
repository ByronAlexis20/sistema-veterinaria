package com.veterinaria.controlador;

import java.util.List;

import com.veterinaria.modelo.Cliente;
import com.veterinaria.modelo.ClienteMascota;
import com.veterinaria.modelo.ClienteMascotaDAO;
import com.veterinaria.util.Context;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class ListaMascotaC {
	Tooltip toolTip;
	@FXML private TableView<ClienteMascota> tvDatos;
	@FXML private TextField txtBuscar;
	ClienteMascotaDAO clienteMascotaDAO = new ClienteMascotaDAO();
	Cliente clienteSeleccionado;
	public void initialize() {
		try {
			clienteSeleccionado = Context.getInstance().getCliente();
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarClienteMascota();
					} 
				} 
			}); 
			tvDatos.setRowFactory(tv -> {
	            TableRow<ClienteMascota> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	    					Context.getInstance().setClienteMascota(tvDatos.getSelectionModel().getSelectedItem());
	    					Context.getInstance().getStageModal().close();
	    				}
	                }
	            });
	            return row ;
	        });
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarClienteMascota() {
		try {
			List<ClienteMascota> lista;
			ObservableList<ClienteMascota> datos = FXCollections.observableArrayList();
			lista = clienteMascotaDAO.getListaMascotaPorCliente(txtBuscar.getText().toString(),clienteSeleccionado.getIdCliente());
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
			List<ClienteMascota> lista;
			ObservableList<ClienteMascota> datos = FXCollections.observableArrayList();
			lista = clienteMascotaDAO.getListaMascotaPorCliente(busqueda,clienteSeleccionado.getIdCliente());
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<ClienteMascota, String> cedulaColum = new TableColumn<>("Cédula Dueño");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(120);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCliente().getCedula());
				}
			});
			
			TableColumn<ClienteMascota, String> nombresColum = new TableColumn<>("Apellidos y nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					String nombres = param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			
			TableColumn<ClienteMascota, String> nombreMascotaColum = new TableColumn<>("Nombre Mascota");
			nombreMascotaColum.setMinWidth(10);
			nombreMascotaColum.setPrefWidth(100);
			nombreMascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMascota().getNombre());
				}
			});
			TableColumn<ClienteMascota, String> tipoMascotaColum = new TableColumn<>("Tipo de mascota");
			tipoMascotaColum.setMinWidth(10);
			tipoMascotaColum.setPrefWidth(100);
			tipoMascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMascota().getRaza().getTipoMascota().getDescripcion());
				}
			});
			TableColumn<ClienteMascota, String> razaColum = new TableColumn<>("Raza");
			razaColum.setMinWidth(10);
			razaColum.setPrefWidth(100);
			razaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClienteMascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ClienteMascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMascota().getRaza().getDescripcion());
				}
			});
			tvDatos.getColumns().addAll(cedulaColum, nombresColum,nombreMascotaColum,tipoMascotaColum,razaColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
