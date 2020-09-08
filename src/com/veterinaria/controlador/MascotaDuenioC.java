package com.veterinaria.controlador;

import java.util.List;
import java.util.Optional;

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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class MascotaDuenioC {
	@FXML private TableView<Mascota> tvDatos;
	@FXML private Button btnAsignar;
	@FXML private TextField txtBuscar;
	@FXML private Button btnQuitar;

	MascotaDAO mascotaDAO = new MascotaDAO();
	ControllerHelper helper = new ControllerHelper();
	ClienteMascotaDAO clienteMascotaDAO = new ClienteMascotaDAO();
	public void initialize() {
		try {
			llenarDatos("");
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
			razaColum.setPrefWidth(100);
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
			TableColumn<Mascota, String> duenioColum = new TableColumn<>("Dueño");
			duenioColum.setMinWidth(10);
			duenioColum.setPrefWidth(100);
			duenioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mascota,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Mascota, String> param) {
					String duenio = "";
					List<ClienteMascota> listado = clienteMascotaDAO.getListaDuenioMascota(param.getValue().getIdMascota());
					if(listado.size() > 0)
						duenio = listado.get(0).getCliente().getNombre() + " " + listado.get(0).getCliente().getApellido();
					else
						duenio = "SIN DUEÑO";
					return new SimpleObjectProperty<String>(duenio);
				}
			});
			tvDatos.getColumns().addAll(codigoColum,tipoMascotaColum, razaColum,nombreColum,duenioColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void asignarDuenio() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar mascota", Context.getInstance().getStage());
				return;
			}
			List<ClienteMascota> listado = clienteMascotaDAO.getListaDuenioMascota(tvDatos.getSelectionModel().getSelectedItem().getIdMascota());
			if(listado.size() > 0) 
				helper.mostrarAlertaAdvertencia("Mascota ya tiene dueño, debe quitar y volver a asignar", Context.getInstance().getStage());
			else {
				Context.getInstance().setCliente(null);
				Context.getInstance().setMascota(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/mascotas/FrmListaCliente.fxml","Listado de clientes", Context.getInstance().getStage());
				llenarDatos("");
				Context.getInstance().setCliente(null);
				Context.getInstance().setMascota(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void quitarDuenio() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar mascota", Context.getInstance().getStage());
				return;
			}
			List<ClienteMascota> listado = clienteMascotaDAO.getListaDuenioMascota(tvDatos.getSelectionModel().getSelectedItem().getIdMascota());
			if(listado.size() > 0) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea quitar dueño?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					ClienteMascota clienteMascotaEliminar = listado.get(0);
					clienteMascotaEliminar.setEstado("I");
					clienteMascotaDAO.getEntityManager().getTransaction().begin();
					clienteMascotaDAO.getEntityManager().merge(clienteMascotaEliminar);
					clienteMascotaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Dueño quitado de la mascota", Context.getInstance().getStage());
					llenarDatos("");
				}
			}
			else
				helper.mostrarAlertaAdvertencia("Mascota no tiene dueño para quitar", Context.getInstance().getStage());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
