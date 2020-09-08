package com.veterinaria.controlador;

import java.util.List;

import com.veterinaria.modelo.Enfermedad;
import com.veterinaria.modelo.EnfermedadDAO;
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

public class ListaEnfermedadC {
	Tooltip toolTip;
	@FXML private TableView<Enfermedad> tvDatos;
	@FXML private TextField txtBuscar;
	EnfermedadDAO enfermedadDAO = new EnfermedadDAO();
	
	public void initialize() {
		try {
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarEnfermedades();
					} 
				} 
			}); 
			tvDatos.setRowFactory(tv -> {
	            TableRow<Enfermedad> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	    					Context.getInstance().setEnfermedad(tvDatos.getSelectionModel().getSelectedItem());
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
	public void buscarEnfermedades() {
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
			enfermedadColum.setPrefWidth(200);
			enfermedadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Enfermedad,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Enfermedad, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNombre());
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
			tvDatos.getColumns().addAll(codigoColum, enfermedadColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
