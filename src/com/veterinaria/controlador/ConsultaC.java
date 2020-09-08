package com.veterinaria.controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.veterinaria.modelo.Cliente;
import com.veterinaria.modelo.ClienteMascota;
import com.veterinaria.modelo.Consulta;
import com.veterinaria.modelo.ConsultaDAO;
import com.veterinaria.modelo.ConsultaDetalle;
import com.veterinaria.modelo.Empresa;
import com.veterinaria.modelo.EmpresaDAO;
import com.veterinaria.modelo.Enfermedad;
import com.veterinaria.modelo.Veterinario;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;
import com.veterinaria.util.PrintReport;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

public class ConsultaC {
	Tooltip toolTip;
	@FXML private TextField txtRazaMascota;
	@FXML private TextField txtTotalConsulta;
	@FXML private Button btnAgregarTratamiento;
	@FXML private Button btnNuevo;
	@FXML private Button btnBuscarFactura;
	@FXML private CheckBox chkExamenFisico;
	@FXML private TextField txtNumeroFactura;
	@FXML private TextField txtCedulaCliente;
	@FXML private TextField txtDireccionCliente;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtProfesionVeterinario;
	@FXML private TextField txtNombresCliente;
	@FXML private TextField txtApellidosCliente;
	@FXML private TextField txtNombresVeterinario;
	@FXML private Button btnQuitarTratamiento;
	@FXML private Button btnAgregarEnfermedad;
	@FXML private Button btnBuscarMascota;
	@FXML private TextField txtTelefonoCliente;
	@FXML private TextField txtNombresMascota;
	@FXML private TextField txtCodigoMascota;
	@FXML private TextField txtTiempoTratamiento;
	@FXML private TextField txtEmailCliente;
	@FXML private TextField txtTelefonoVeterinario;
	@FXML private Button btnBuscarVeterinario;
	@FXML private Button btnBuscarCliente;
	@FXML private Button btnQuitarEnfermedad;
	@FXML private TableView<String> tvDatosTratamiento;
	@FXML private TextField txtCedulaVeterinario;
	@FXML private Button btnGrabar;
	@FXML private TableView<ConsultaDetalle> tvDatosEnfermedad;
	@FXML private TextField txtTipoMascota;

	private Cliente clienteSeleccionado;
	private Veterinario veterinarioSeleccionado;
	private ClienteMascota clienteMascotaSeleccionado;
	private Consulta consulta;
	ControllerHelper helper = new ControllerHelper();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	ConsultaDAO consultaDAO = new ConsultaDAO();

	private String empresa = "";
	private String telefono = "";
	private String direccion = "";
	private String email = "";
	private Integer MaxFactura = 0;
	EmpresaDAO empresaDAO = new EmpresaDAO();
	
	public void initialize() {
		Date fechaActual = new Date();
		dtpFecha.setValue(convertToLocalDate(fechaActual));
		toolTip = new Tooltip("Grabar consulta");
		btnGrabar.setStyle("-fx-graphic: url('/grabar.png');-fx-cursor: hand;");
		btnGrabar.setTooltip(toolTip);
		toolTip = new Tooltip("Nueva consulta");
		btnNuevo.setStyle("-fx-graphic: url('/nuevo.png');-fx-cursor: hand;");
		btnNuevo.setTooltip(toolTip);
		toolTip = new Tooltip("Buscar Cliente");
		btnBuscarCliente.setStyle("-fx-cursor: hand;");
		btnBuscarCliente.setTooltip(toolTip);
		toolTip = new Tooltip("Buscar consulta");
		btnBuscarFactura.setStyle("-fx-cursor: hand;");
		btnBuscarFactura.setTooltip(toolTip);
		toolTip = new Tooltip("Buscar veterinario");
		btnBuscarVeterinario.setStyle("-fx-cursor: hand;");
		btnBuscarVeterinario.setTooltip(toolTip);
		toolTip = new Tooltip("Buscar mascota");
		btnBuscarMascota.setStyle("-fx-cursor: hand;");
		btnBuscarMascota.setTooltip(toolTip);
		llenarDatosEnfermedades();
		llenarDatosTratamiento();
		nuevo();
		restricciones();
		cargarDatosEmpresa();
		cargarNumeroFactura();
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
	private void cargarNumeroFactura() {
		try {
			List<Integer> consulta = consultaDAO.getConsultaMax();
			txtNumeroFactura.setText(String.valueOf(consulta.get(0)+1));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void llenarDatosEnfermedades() {
		try {
			tvDatosEnfermedad.getColumns().clear();
			tvDatosEnfermedad.getItems().clear();

			//llenar los datos en la tabla
			TableColumn<ConsultaDetalle, String> codigoColum = new TableColumn<>("Código");
			codigoColum.setMinWidth(10);
			codigoColum.setPrefWidth(90);
			codigoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ConsultaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ConsultaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getEnfermedad().getIdEnfermedad()));
				}
			});
			TableColumn<ConsultaDetalle, String> enfermedadColum = new TableColumn<>("Enfermedad");
			enfermedadColum.setMinWidth(10);
			enfermedadColum.setPrefWidth(200);
			enfermedadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ConsultaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ConsultaDetalle, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEnfermedad().getNombre());
				}
			});
			tvDatosEnfermedad.getColumns().addAll(codigoColum, enfermedadColum);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarDatosTratamiento() {
		try {
			tvDatosTratamiento.getColumns().clear();
			tvDatosTratamiento.getItems().clear();

			//llenar los datos en la tabla
			TableColumn<String, String> tratamientoColum = new TableColumn<>("Tratamiento");
			tratamientoColum.setMinWidth(10);
			tratamientoColum.setPrefWidth(450);
			tratamientoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<String, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue()));
				}
			});
			tvDatosTratamiento.getColumns().addAll(tratamientoColum);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void buscarCliente() {
		try {
			Context.getInstance().setCliente(null);
			helper.abrirPantallaModal("/consulta/FrmListaCliente.fxml","Listado de clientes", Context.getInstance().getStage());
			if(Context.getInstance().getCliente() != null) {
				cargarDatosCliente(Context.getInstance().getCliente());
			}else {
				limpiarCliente();
				limpiarMascota();
			}
			Context.getInstance().setCliente(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarDatosCliente(Cliente cliente) {
		clienteSeleccionado = cliente;
		txtNombresCliente.setText(clienteSeleccionado.getNombre());
		txtTelefonoCliente.setText(clienteSeleccionado.getTelefono());
		txtEmailCliente.setText(clienteSeleccionado.getEmail());
		txtDireccionCliente.setText(clienteSeleccionado.getDireccion());
		txtApellidosCliente.setText(clienteSeleccionado.getApellido());
		txtCedulaCliente.setText(clienteSeleccionado.getCedula());
	}
	private void limpiarCliente() {
		clienteSeleccionado = null;
		txtNombresCliente.setText("");
		txtTelefonoCliente.setText("");
		txtEmailCliente.setText("");
		txtApellidosCliente.setText("");
		txtDireccionCliente.setText("");
		txtCedulaCliente.setText("");
	}
	public void buscarVeterinario() {
		try {
			Context.getInstance().setVeterinario(null);
			helper.abrirPantallaModal("/consulta/FrmListaVeterinario.fxml","Listado de veterinarios", Context.getInstance().getStage());
			if(Context.getInstance().getVeterinario() != null) {
				cargarDatosVeterinario(Context.getInstance().getVeterinario());
			}else {
				limpiarVeterinario();
			}
			Context.getInstance().setCliente(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarDatosVeterinario(Veterinario veterinario) {
		veterinarioSeleccionado = veterinario;
		txtCedulaVeterinario.setText(veterinarioSeleccionado.getCedula());
		txtNombresVeterinario.setText(veterinarioSeleccionado.getNombres() + " " + veterinario.getApellidos());
		txtTelefonoVeterinario.setText(veterinarioSeleccionado.getTelefono());
		txtProfesionVeterinario.setText(veterinarioSeleccionado.getProfesion());
	}
	private void limpiarVeterinario() {
		veterinarioSeleccionado = null;
		txtCedulaVeterinario.setText("");
		txtNombresVeterinario.setText("");
		txtTelefonoVeterinario.setText("");
		txtProfesionVeterinario.setText("");
	}

	public void buscarMascota() {
		try {
			if(clienteSeleccionado == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un cliente", Context.getInstance().getStage());
				return;
			}
			Context.getInstance().setCliente(clienteSeleccionado);
			helper.abrirPantallaModal("/consulta/FrmListaMascota.fxml","Listado de mascotas", Context.getInstance().getStage());
			if(Context.getInstance().getClienteMascota() != null) {
				cargarDatosmascota(Context.getInstance().getClienteMascota());
			}else {
				limpiarMascota();
			}
			Context.getInstance().setClienteMascota(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void cargarDatosmascota(ClienteMascota mascota) {
		clienteMascotaSeleccionado = mascota;
		txtCodigoMascota.setText(String.valueOf(clienteMascotaSeleccionado.getMascota().getIdMascota()));
		txtNombresMascota.setText(clienteMascotaSeleccionado.getMascota().getNombre());
		txtRazaMascota.setText(clienteMascotaSeleccionado.getMascota().getRaza().getDescripcion());
		txtTipoMascota.setText(clienteMascotaSeleccionado.getMascota().getRaza().getTipoMascota().getDescripcion());
	}
	private void limpiarMascota() {
		clienteMascotaSeleccionado = null;
		txtCodigoMascota.setText("");
		txtNombresMascota.setText("");
		txtRazaMascota.setText("");
		txtTipoMascota.setText("");
	}

	public void buscarFactura() {
		try {
			Context.getInstance().setConsulta(null);
			helper.abrirPantallaModal("/consulta/FrmListaConsultas.fxml","Listado de consultas", Context.getInstance().getStage());
			if(Context.getInstance().getConsulta() != null) {
				cargarFactura(Context.getInstance().getConsulta());
			}else {
				nuevo();
			}
			Context.getInstance().setConsulta(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void cargarFactura(Consulta con) {
		consulta = con;
		txtTotalConsulta.setText(String.valueOf(con.getValorCosto()));
		txtNumeroFactura.setText(String.valueOf(con.getNumeroFactura()));
		txtTiempoTratamiento.setText(String.valueOf(con.getTiempoTratamiento()));
		dtpFecha.setValue(convertToLocalDate(con.getFecha()));
		cargarDatosCliente(con.getCliente());
		cargarDatosmascota(con.getClienteMascota());
		cargarDatosVeterinario(con.getVeterinario());
		tvDatosEnfermedad.getItems().clear();
		tvDatosTratamiento.getItems().clear();
		String[] parts = con.getMotivo().split("&");
		for(int i = 0 ; i < parts.length ; i++) {
			tvDatosTratamiento.getItems().add(parts[i]);
		}
		tvDatosTratamiento.refresh();
		
		for(ConsultaDetalle det : con.getConsultaDetalles()) {
			tvDatosEnfermedad.getItems().add(det);
		}
		tvDatosEnfermedad.refresh();
		if(con.getExamenFisico().equals("SI"))
			chkExamenFisico.setSelected(true);
		else
			chkExamenFisico.setSelected(false);
	}
	public void agregarEnfermedad() {
		try {
			Context.getInstance().setEnfermedad(null);
			helper.abrirPantallaModal("/consulta/FrmListaEnfermedad.fxml","Listado de enfermedades", Context.getInstance().getStage());
			if(Context.getInstance().getEnfermedad() != null) {
				Enfermedad enfermedadAdd = Context.getInstance().getEnfermedad();
				ConsultaDetalle detalle = new ConsultaDetalle();
				detalle.setEnfermedad(enfermedadAdd);
				detalle.setIdDetalle(null);
				detalle.setEstado("A");
				tvDatosEnfermedad.getItems().add(detalle);
				tvDatosEnfermedad.refresh();
			}
			Context.getInstance().setCliente(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void quitarEnfermedad() {
		try {
			if(tvDatosEnfermedad.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar una enfermedad a quitar", Context.getInstance().getStage());
				return;
			}
			ConsultaDetalle detalle = new ConsultaDetalle();
			detalle = tvDatosEnfermedad.getSelectionModel().getSelectedItem();
			tvDatosEnfermedad.getItems().remove(detalle);
			tvDatosEnfermedad.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void quitarTratamiento() {
		try {
			if(tvDatosTratamiento.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un tratamiento a quitar", Context.getInstance().getStage());
				return;
			}
			String detalle = new String();
			detalle = tvDatosTratamiento.getSelectionModel().getSelectedItem();
			tvDatosTratamiento.getItems().remove(detalle);
			tvDatosTratamiento.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}


	public void agregarTratamiento() {
		try {
			Context.getInstance().setTratamiento("");
			helper.abrirPantallaModal("/consulta/FrmTratamiento.fxml","Agregar tratamiento", Context.getInstance().getStage());
			if(!Context.getInstance().getTratamiento().equals("")) {
				String tratamientoAdd = Context.getInstance().getTratamiento();
				tvDatosTratamiento.getItems().add(tratamientoAdd);
				tvDatosTratamiento.refresh();
			}
			Context.getInstance().setTratamiento("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				cargarDatosCliente();
				consulta.setCliente(clienteSeleccionado);
				consulta.setClienteMascota(clienteMascotaSeleccionado);
				consulta.setEstado("A");
				consulta.setFecha(convertToDate(dtpFecha.getValue()));
				if(chkExamenFisico.isSelected())
					consulta.setExamenFisico("SI");
				else
					consulta.setExamenFisico("NO");
				consulta.setNumeroFactura(Integer.parseInt(txtNumeroFactura.getText()));
				String tratamiento = "";
				for(String t : tvDatosTratamiento.getItems()) {
					tratamiento = tratamiento + t + "&";
				}
				consulta.setMotivo(tratamiento);
				consulta.setTiempoTratamiento(Integer.parseInt(txtTiempoTratamiento.getText()));
				consulta.setValorCosto(Double.parseDouble(txtTotalConsulta.getText()));
				consulta.setVeterinario(veterinarioSeleccionado);
				if(consulta.getConsultaDetalles() == null) {//no tiene datos de detalle
					List<ConsultaDetalle> detalle = new ArrayList<>();
					for(ConsultaDetalle det : tvDatosEnfermedad.getItems()) {
						det.setConsulta(consulta);
						detalle.add(det);
					}
					consulta.setConsultaDetalles(detalle);
				}else {// tiene datos de detalle
					boolean bandera = false;
					for(ConsultaDetalle det : tvDatosEnfermedad.getItems()) {
						if(det.getIdDetalle() == null) {
							det.setConsulta(consulta);
							consulta.getConsultaDetalles().add(det);
						}
					}
					for(ConsultaDetalle deta : consulta.getConsultaDetalles()) {
						bandera = false;
						if(deta.getIdDetalle() != null) {
							for(ConsultaDetalle det : tvDatosEnfermedad.getItems()) {
								if(det.getIdDetalle() != null) {
									if(det.getIdDetalle() == deta.getIdDetalle())
										bandera = true;//si esta en la lista de los que estan grabados
								}
							}
						}
						if(bandera == false) {//si la bandera sigue siendo falso.. es xq se elimino
							deta.setEstado("I");
						}
					}
				}
				
				if(consulta.getIdConsulta() != null) {//modifica
					consultaDAO.getEntityManager().getTransaction().begin();
					consultaDAO.getEntityManager().merge(consulta);
					consultaDAO.getEntityManager().merge(clienteSeleccionado);
					consultaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					System.out.println(consulta.getIdConsulta() + " modifica");
					imprimirFactura(consulta.getIdConsulta());
					nuevo();
				}else {//inserta
					consultaDAO.getEntityManager().getTransaction().begin();
					consultaDAO.getEntityManager().merge(clienteSeleccionado);
					consultaDAO.getEntityManager().persist(consulta);
					consultaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
					System.out.println(consulta.getIdConsulta() + " inserta");
					imprimirFactura(consulta.getIdConsulta());
					nuevo();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
		}
	}
	private void cargarDatosCliente() {
		if(clienteSeleccionado != null) {
			clienteSeleccionado.setApellido(txtApellidosCliente.getText());
			clienteSeleccionado.setNombre(txtNombresCliente.getText());
			clienteSeleccionado.setCedula(txtCedulaCliente.getText());
			clienteSeleccionado.setDireccion(txtDireccionCliente.getText());
			clienteSeleccionado.setEmail(txtEmailCliente.getText());
		}
	}
	private void imprimirFactura(Integer idfactura) {
		try {
			
			
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("EMPRESA", empresa);
			param.put("DIRECCION", direccion);
			param.put("NO_FACTURA", "NO: 0001-00" + txtNumeroFactura.getText());
			param.put("TELEFONO", telefono);
			param.put("CORREO", email);
			param.put("FECHA", "Fecha: " + formateador.format(convertToDate(dtpFecha.getValue())));
			param.put("TOTAL", txtTotalConsulta.getText());
			System.out.println("id factura: " + idfactura);
			param.put("ID_CONSULTA", idfactura);
			String tratamiento = "";
			for(String t : tvDatosTratamiento.getItems()) {
				tratamiento = tratamiento + t + ", ";
			}
			param.put("TRATAMIENTO", tratamiento);
			pr.crearReporte("/recursos/reportes/rptFactura.jasper", consultaDAO, param);
			pr.showReport("Factura");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		
	}
	private boolean validarDatos() {
		try {
			boolean bandera = false;
			if(clienteSeleccionado == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un cliente", Context.getInstance().getStage());
				return false;
			}
			if(clienteMascotaSeleccionado == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar una mascota del cliente", Context.getInstance().getStage());
				return false;
			}
			if(veterinarioSeleccionado == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un veterinario que atiende a la mascota", Context.getInstance().getStage());
				return false;
			}
			if(txtTiempoTratamiento.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar tiempo de tratamiento en dias", Context.getInstance().getStage());
				return false;
			}			
			if(txtTotalConsulta.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe registrar total a pagar de la consulta", Context.getInstance().getStage());
				return false;
			}
			bandera = true;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}


	public void nuevo() {
		consulta = new Consulta();
		limpiarCliente();
		limpiarMascota();
		limpiarVeterinario();
		tvDatosEnfermedad.getItems().clear();
		tvDatosTratamiento.getItems().clear();
		chkExamenFisico.setSelected(true);
		txtTotalConsulta.setText("0.00");
		txtTiempoTratamiento.setText("0");
	}
	private void restricciones() {
		//longitud de las cadenas de textos ingresadas
		txtTiempoTratamiento.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtTiempoTratamiento.getText().length() > 4) {
					String s = txtTiempoTratamiento.getText().substring(0, 4);
					txtTiempoTratamiento.setText(s);
				}
			}
		});
		//validar solo numeros
		txtTiempoTratamiento.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtTiempoTratamiento.setText(oldValue);
				}
			}
		});



		txtTotalConsulta.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtTotalConsulta.getText().length() > 4) {
					String s = txtTotalConsulta.getText().substring(0, 4);
					txtTotalConsulta.setText(s);
				}
			}
		});
		//validar solo numeros
		txtTotalConsulta.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					txtTotalConsulta.setText(oldValue);
				}
			}
		});
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
