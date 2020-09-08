package com.veterinaria.controlador;

import java.util.List;

import com.veterinaria.modelo.Consulta;
import com.veterinaria.modelo.ConsultaDAO;
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

public class FrmListaConsultaC {
	Tooltip toolTip;
	@FXML private TableView<Consulta> tvDatos;
	@FXML private TextField txtBuscar;
	ConsultaDAO consultaDAO = new ConsultaDAO();
	
	public void initialize() {
		try {
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarConsulta();
					} 
				} 
			}); 
			tvDatos.setRowFactory(tv -> {
	            TableRow<Consulta> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	    					Context.getInstance().setConsulta(tvDatos.getSelectionModel().getSelectedItem());
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
	public void buscarConsulta() {
		try {
			List<Consulta> lista;
			ObservableList<Consulta> datos = FXCollections.observableArrayList();
			lista = consultaDAO.getListaConsultas(txtBuscar.getText().toString());
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
			List<Consulta> lista;
			ObservableList<Consulta> datos = FXCollections.observableArrayList();
			lista = consultaDAO.getListaConsultas(busqueda);
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<Consulta, String> facturaColum = new TableColumn<>("Num. Factura");
			facturaColum.setMinWidth(10);
			facturaColum.setPrefWidth(100);
			facturaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consulta,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Consulta, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNumeroFactura()));
				}
			});
			
			TableColumn<Consulta, String> nombresColum = new TableColumn<>("Cliente");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consulta,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Consulta, String> param) {
					String nombres = param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			
			TableColumn<Consulta, String> mascotaColum = new TableColumn<>("Mascota");
			mascotaColum.setMinWidth(10);
			mascotaColum.setPrefWidth(100);
			mascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consulta,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Consulta, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getClienteMascota().getMascota().getNombre());
				}
			});
			TableColumn<Consulta, String> tipoMascotaColum = new TableColumn<>("Tipo Mascota");
			tipoMascotaColum.setMinWidth(10);
			tipoMascotaColum.setPrefWidth(100);
			tipoMascotaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consulta,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Consulta, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getClienteMascota().getMascota().getRaza().getTipoMascota().getDescripcion());
				}
			});
			TableColumn<Consulta, String> razaColum = new TableColumn<>("Raza");
			razaColum.setMinWidth(10);
			razaColum.setPrefWidth(100);
			razaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consulta,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Consulta, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getClienteMascota().getMascota().getRaza().getDescripcion());
				}
			});
			tvDatos.getColumns().addAll(facturaColum, nombresColum,mascotaColum,tipoMascotaColum,razaColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
