package com.veterinaria.controlador;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.veterinaria.modelo.Consulta;
import com.veterinaria.modelo.ConsultaDAO;
import com.veterinaria.modelo.Empresa;
import com.veterinaria.modelo.EmpresaDAO;
import com.veterinaria.util.PrintReport;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class FrmConsultaPDF {
	Tooltip toolTip;
	@FXML private TableView<Consulta> tvDatos;
	@FXML private TextField txtBuscar;
	ConsultaDAO consultaDAO = new ConsultaDAO();
	private String empresa = "";
	private String telefono = "";
	private String direccion = "";
	private String email = "";
	EmpresaDAO empresaDAO = new EmpresaDAO();
	Consulta factura = new Consulta();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	public void initialize() {
		try {
			cargarDatosEmpresa();
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
	    					factura=(tvDatos.getSelectionModel().getSelectedItem());
	    					System.out.println(factura);
	    					imprimirFactura(factura.getIdConsulta());
	    				}
	                }
	            });
	            return row ;
	        });
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarDatosEmpresa() {
		try {
			List<Empresa> lista = empresaDAO.getListaEmpresa();
			for(Empresa emp : lista) {
				empresa = emp.getRazonSocial();
				direccion = emp.getDireccion();
				email = emp.getEmail();
				telefono = emp.getTelefono();
			}
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
			facturaColum.setPrefWidth(40);
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
			TableColumn<Consulta, String> TotalColum = new TableColumn<>("Total ");
			TotalColum.setMinWidth(10);
			TotalColum.setPrefWidth(70);
			TotalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consulta,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Consulta, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getValorCosto()));
				}
			});
			tvDatos.getColumns().addAll(facturaColum, nombresColum,mascotaColum,tipoMascotaColum,razaColum,TotalColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void imprimirFactura(Integer idfactura) {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("NO_FACTURA", "NO: 0001-00" + factura.getNumeroFactura());
			param.put("TELEFONO", telefono);
			param.put("CORREO", email);
			param.put("FECHA", "Fecha: " + formateador.format(factura.getFecha()));
			param.put("TOTAL", String.valueOf(factura.getValorCosto()));
			param.put("ID_CONSULTA", idfactura);
			String tratamiento = "";
			String[] parts = factura.getMotivo().split("&");
			for(int i = 0 ; i < parts.length ; i++) {
				tratamiento = tratamiento + parts[i] + ", ";
			}
			param.put("TRATAMIENTO", tratamiento);
			pr.crearReporte("/recursos/reportes/rptFactura.jasper", consultaDAO, param);
			pr.showReport("Factura");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		
	}
}
