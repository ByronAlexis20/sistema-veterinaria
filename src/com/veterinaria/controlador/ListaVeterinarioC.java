package com.veterinaria.controlador;

import java.util.List;

import com.veterinaria.modelo.Veterinario;
import com.veterinaria.modelo.VeterinarioDAO;
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

public class ListaVeterinarioC {
	Tooltip toolTip;
	@FXML private TableView<Veterinario> tvDatos;
	@FXML private TextField txtBuscar;
	VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
	
	public void initialize() {
		try {
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarVeterinario();
					} 
				} 
			}); 
			tvDatos.setRowFactory(tv -> {
	            TableRow<Veterinario> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	    					Context.getInstance().setVeterinario(tvDatos.getSelectionModel().getSelectedItem());
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
	public void buscarVeterinario() {
		try {
			List<Veterinario> lista;
			ObservableList<Veterinario> datos = FXCollections.observableArrayList();
			lista = veterinarioDAO.getListaVeterinarios(txtBuscar.getText().toString());
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
			List<Veterinario> lista;
			ObservableList<Veterinario> datos = FXCollections.observableArrayList();
			lista = veterinarioDAO.getListaVeterinarios(busqueda);
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<Veterinario, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(90);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Veterinario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Veterinario, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCedula());
				}
			});
			
			TableColumn<Veterinario, String> nombresColum = new TableColumn<>("Apellidos y nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Veterinario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Veterinario, String> param) {
					String nombres = param.getValue().getNombres() + " " + param.getValue().getApellidos();
					return new SimpleObjectProperty<String>(nombres);
				}
			});
			
			TableColumn<Veterinario, String> profesionColum = new TableColumn<>("Profesion");
			profesionColum.setMinWidth(10);
			profesionColum.setPrefWidth(100);
			profesionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Veterinario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Veterinario, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getProfesion());
				}
			});
			tvDatos.getColumns().addAll(cedulaColum, nombresColum,profesionColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
