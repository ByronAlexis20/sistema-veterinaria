package com.veterinaria.controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.veterinaria.modelo.Cliente;
import com.veterinaria.modelo.ClienteMascota;
import com.veterinaria.modelo.ClienteMascotaDAO;
import com.veterinaria.modelo.Mascota;
import com.veterinaria.modelo.MascotaDAO;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class MascotaSinDueno {
	Tooltip toolTip;
	@FXML private TableView<Mascota> tvDatos;
	@FXML private TextField txtBuscar;
	ClienteMascotaDAO clienteMascotaDAO = new ClienteMascotaDAO();
	MascotaDAO mascotaDAO = new MascotaDAO();
	Cliente clienteSeleccionado;
	ControllerHelper helper = new ControllerHelper();
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
	            TableRow<Mascota> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	                		if(grabarMascota() == false) {
	                			helper.mostrarAlertaInformacion("Error al grabar", Context.getInstance().getStage());
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
	private boolean grabarMascota() {
		try {
			boolean bandera = false;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Agregar mascota?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				ClienteMascota mascota = new ClienteMascota();
				ClienteMascota mascotaGrabar = new ClienteMascota();
				mascota.setIdClienteMascota(null);
				mascota.setEstado("A");
				mascota.setFechaRegistro(new Date());
				mascota.setCliente(clienteSeleccionado);
				mascota.setMascota(tvDatos.getSelectionModel().getSelectedItem());
				clienteMascotaDAO.getEntityManager().getTransaction().begin();
				mascotaGrabar = (ClienteMascota) clienteMascotaDAO.getEntityManager().merge(mascota);
				clienteMascotaDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
				Context.getInstance().setClienteMascota(mascotaGrabar);
				Context.getInstance().getStageModal().close();
				bandera = true;
			}
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}
	public void buscarClienteMascota() {
		try {
			List<ClienteMascota> listaConDueno;
			List<Mascota> lista;
			List<Mascota> listaAgregar = new ArrayList<>();
			lista = mascotaDAO.getListaMascotas(txtBuscar.getText().toString());
			listaConDueno = clienteMascotaDAO.getListaMascotaActivos();
			boolean bandera = false;
			for(Mascota ms : lista) {
				bandera = false;
				for(ClienteMascota cl : listaConDueno) {
					if(cl.getMascota().getIdMascota() == ms.getIdMascota())
						bandera = true;
				}
				if (bandera == false) {
					listaAgregar.add(ms);
				}
			}
			
			
			ObservableList<Mascota> datos = FXCollections.observableArrayList();
			
			datos.setAll(listaAgregar);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarDatos(String busqueda) {
		try {
			List<ClienteMascota> listaConDueno;
			List<Mascota> lista;
			List<Mascota> listaAgregar = new ArrayList<>();
			lista = mascotaDAO.getListaMascotas(txtBuscar.getText().toString());
			listaConDueno = clienteMascotaDAO.getListaMascotaActivos();
			boolean bandera = false;
			for(Mascota ms : lista) {
				bandera = false;
				for(ClienteMascota cl : listaConDueno) {
					if(cl.getMascota().getIdMascota() == ms.getIdMascota())
						bandera = true;
				}
				if (bandera == false) {
					listaAgregar.add(ms);
				}
			}
			ObservableList<Mascota> datos = FXCollections.observableArrayList();
			datos.setAll(listaAgregar);
			
			//llenar los datos en la tabla
			TableColumn<Mascota, String> codigoColum = new TableColumn<>("Código");
			codigoColum.setMinWidth(10);
			codigoColum.setPrefWidth(120);
			codigoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdMascota()));
				}
			});
			
			
			TableColumn<Mascota, String> nombreMascotaColum = new TableColumn<>("Nombre Mascota");
			nombreMascotaColum.setMinWidth(10);
			nombreMascotaColum.setPrefWidth(100);
			nombreMascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNombre());
				}
			});
			TableColumn<Mascota, String> tipoMascotaColum = new TableColumn<>("Tipo de mascota");
			tipoMascotaColum.setMinWidth(10);
			tipoMascotaColum.setPrefWidth(100);
			tipoMascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRaza().getTipoMascota().getDescripcion());
				}
			});
			TableColumn<Mascota, String> razaColum = new TableColumn<>("Raza");
			razaColum.setMinWidth(10);
			razaColum.setPrefWidth(100);
			razaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRaza().getDescripcion());
				}
			});
			tvDatos.getColumns().addAll(codigoColum,nombreMascotaColum,tipoMascotaColum,razaColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
