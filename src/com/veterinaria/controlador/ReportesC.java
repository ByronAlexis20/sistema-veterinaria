package com.veterinaria.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.veterinaria.modelo.ClaseDAO;
import com.veterinaria.modelo.Empresa;
import com.veterinaria.modelo.EmpresaDAO;
import com.veterinaria.modelo.Mascota;
import com.veterinaria.modelo.Raza;
import com.veterinaria.modelo.TipoMascota;
import com.veterinaria.modelo.TipoMascotaDAO;
import com.veterinaria.modelo.Veterinario;
import com.veterinaria.modelo.VeterinarioDAO;
import com.veterinaria.util.PrintReport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class ReportesC {
	@FXML private Button btnVisualizarEstadistico;
	@FXML private DatePicker dtpFinHistorial;
	@FXML private ComboBox<Veterinario> cboVeterinarioEstadistico;
	@FXML private Button btnVisualizarConsultaVeterinario;
	@FXML private Button btnVisualizarHistorial;
	@FXML private DatePicker dtpInicioConsultaVeterinario;
	@FXML private DatePicker dtpInicioHistorial;
	@FXML private ComboBox<TipoMascota> cboTipoMascota;
	@FXML private Button btnVisualizarConsultaMascota;
	@FXML private DatePicker dtpInicioEstadistico;
	@FXML private DatePicker dtpFinEstadistico;
	@FXML private Button btnVisualizarMascota;
	@FXML private DatePicker dtpFinConsultaMascota;
	@FXML private ComboBox<Veterinario> cboVeterinario;
	@FXML private DatePicker dtpInicioConsultaMascota;
	@FXML private DatePicker dtpFinConsultaVeterinario;
	@FXML private Button btnVisualizarCliente;
	@FXML private DatePicker dtpInicioEstadistico2;
	@FXML private DatePicker dtpFinEstadistico2;
	@FXML private ComboBox<TipoMascota> cboTipoMascota2;
	@FXML private ComboBox<Mascota> cbo_nombreMascota;
	
	private String empresa = "";
	private String ruc = "";
	private String direccion = "";
	private String email = "";
	ClaseDAO claseDAO = new ClaseDAO();
	EmpresaDAO empresaDAO = new EmpresaDAO();
	TipoMascotaDAO tipoMascotaDAO = new TipoMascotaDAO();
	VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
	
	public void initialize() {
		try {
			dtpInicioEstadistico.setValue(convertToLocalDate(new Date()));
			dtpInicioConsultaMascota.setValue(convertToLocalDate(new Date()));
			dtpInicioConsultaVeterinario.setValue(convertToLocalDate(new Date()));
			dtpInicioHistorial.setValue(convertToLocalDate(new Date()));
			dtpFinConsultaMascota.setValue(convertToLocalDate(new Date()));
			dtpFinConsultaVeterinario.setValue(convertToLocalDate(new Date()));
			dtpFinEstadistico.setValue(convertToLocalDate(new Date()));
			dtpFinHistorial.setValue(convertToLocalDate(new Date()));
			cargarDatosEmpresa();
			cargarTipoMascota();
			cargarTipoMascota2();
			cargarVeterinario();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void cargarVeterinario() {
		try {
			cboVeterinario.getItems().clear();
			ObservableList<Veterinario> datos = FXCollections.observableArrayList();
			List<Veterinario> lista = veterinarioDAO.getListaVeterinarios("");
			datos.addAll(lista);
			cboVeterinario.setItems(datos);
			cboVeterinario.setPromptText("Seleccione veterinario");
		}catch(Exception ex) {

		}
	}
	
	public void cargarTipoMascota() {
		try {
			cboTipoMascota.getItems().clear();
			ObservableList<TipoMascota> datos = FXCollections.observableArrayList();
			List<TipoMascota> lista = tipoMascotaDAO.getListaTipoMascotas();
			datos.addAll(lista);
			cboTipoMascota.setItems(datos);
			cboTipoMascota.setPromptText("Seleccione tipo de mascota");
		}catch(Exception ex) {

		}
	}
	
	public void cargarTipoMascota2() {
		try {
			cboTipoMascota2.getItems().clear();
			ObservableList<TipoMascota> datos = FXCollections.observableArrayList();
			List<TipoMascota> lista = tipoMascotaDAO.getListaTipoMascotas();
			datos.addAll(lista);
			cboTipoMascota2.setItems(datos);
			cboTipoMascota2.setPromptText("Seleccione tipo de mascota");
		}catch(Exception ex) {

		}
	}
	
	public void cargarMascotasPorTipoMascota(ActionEvent event) {
		try { 
			  List<Raza> listaRaza = new  ArrayList<Raza>(); 
			  List<Mascota> listaMascota = new ArrayList<Mascota>();
			  cbo_nombreMascota.getItems().clear(); 
			  ObservableList<Mascota> datos =  FXCollections.observableArrayList(); 
			  List<TipoMascota> lista = tipoMascotaDAO.getListaMascotaPorTipo(cboTipoMascota2.getSelectionModel().getSelectedItem().getIdTipoMascota()); 
			  listaRaza.addAll(lista.get(0).getRazas()); 
			  for (int i = 0; i <listaRaza.size(); i++) {
				for (int j = 0; j < listaRaza.get(i).getMascotas().size(); j++) {
					listaMascota.add(listaRaza.get(i).getMascotas().get(j));
				}
			  }
			  System.out.println(listaMascota);	  
			  datos.addAll(listaMascota); 
			  cbo_nombreMascota.setItems(datos);
			  cbo_nombreMascota.setPromptText("Seleccione mascota");
			 
		}catch(Exception ex) {

		}
	}
	
	
	private void cargarDatosEmpresa() {
		try {
			List<Empresa> lista = empresaDAO.getListaEmpresa();
			for(Empresa emp : lista) {
				empresa = emp.getRazonSocial();
				ruc = emp.getRuc();
				direccion = emp.getDireccion();
				email = emp.getEmail();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void visualizarCliente() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("EMAIL", email);
			param.put("RUC", ruc);
			param.put("TITULO", "LISTADO DE CLIENTES");
			pr.crearReporte("/recursos/reportes/rptListaClientes.jasper", claseDAO, param);
			pr.showReport("Lista clientes");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void visualizarMascota() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("EMAIL", email);
			param.put("RUC", ruc);
			param.put("TITULO", "LISTADO DE MASCOTAS");
			pr.crearReporte("/recursos/reportes/rptListaMascota.jasper", claseDAO, param);
			pr.showReport("Lista Mascotas");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void imprimirHistorial() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("EMAIL", email);
			param.put("RUC", ruc);
			param.put("TITULO", "HISTORIAL DE ENFERMEDADES");
			param.put("FECHA_INICIO", convertToDate(dtpInicioHistorial.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpFinHistorial.getValue()));
			param.put("ID_TIPO_MASCOTA", cboTipoMascota2.getSelectionModel().getSelectedItem().getIdTipoMascota());
			param.put("ID_MASCOTA", cbo_nombreMascota.getSelectionModel().getSelectedItem().getIdMascota());
			pr.crearReporte("/recursos/reportes/rptHistorialEnfermedades.jasper", claseDAO, param);
			pr.showReport("Estadistico");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void visualizarConsultaVeterinario() {
		try {
			if(cboVeterinario.getSelectionModel().getSelectedItem() == null) {
				return;
			}
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("EMAIL", email);
			param.put("RUC", ruc);
			param.put("TITULO", "CONSULTA POR VETERINARIO");
			param.put("FECHA_INICIO", convertToDate(dtpInicioConsultaVeterinario.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpFinConsultaVeterinario.getValue()));
			param.put("ID_VETERINARIO", cboVeterinario.getSelectionModel().getSelectedItem().getIdVeterinario());
			pr.crearReporte("/recursos/reportes/rptConsultaVeterinario.jasper", claseDAO, param);
			pr.showReport("Consulta por veterinario");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void visualizarConsultaMascota() {
		try {
			if(cboTipoMascota.getSelectionModel().getSelectedItem() == null) {
				return;
			}
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("EMAIL", email);
			param.put("RUC", ruc);
			param.put("TITULO", "CONSULTA POR TIPO DE MASCOTA");
			param.put("FECHA_INICIO", convertToDate(dtpInicioConsultaMascota.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpFinConsultaMascota.getValue()));
			param.put("ID_TIPO_MASCOTA", cboTipoMascota.getSelectionModel().getSelectedItem().getIdTipoMascota());
			pr.crearReporte("/recursos/reportes/rptConsultaTipoMascota.jasper", claseDAO, param);
			pr.showReport("Consulta tipo de mascota");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void visualizarEstadistico() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("EMAIL", email);
			param.put("RUC", ruc);
			param.put("TITULO", "ESTADÍSTICO DE CONSULTAS MEDICAS POR VETERINARIO");
			param.put("FECHA_INICIO", convertToDate(dtpInicioEstadistico.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpFinEstadistico.getValue()));
			pr.crearReporte("/recursos/reportes/rptEstadisticoMascota.jasper", claseDAO, param);
			pr.showReport("Estadistico");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void vizualizarEstadisticoEnfermedades() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("EMAIL", email);
			param.put("RUC", ruc);
			param.put("TITULO", "ESTADÍSTICO ENFERMEDADES POR TIPO DE MASCOTA");
			param.put("FECHA_INICIO", convertToDate(dtpInicioEstadistico2.getValue()));
			param.put("FECHA_FIN", convertToDate(dtpFinEstadistico2.getValue()));
			pr.crearReporte("/recursos/reportes/rptEstadisticoEnfermedadesTipoMascota.jasper", claseDAO, param);
			pr.showReport("Estadistico Enfermedades");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public LocalDate convertToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
	}
	public Date convertToDate(LocalDate dateToConvert) {
		return java.sql.Date.valueOf(dateToConvert);
	}
}