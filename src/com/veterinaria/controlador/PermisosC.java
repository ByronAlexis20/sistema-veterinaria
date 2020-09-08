package com.veterinaria.controlador;

import java.util.ArrayList;
import java.util.List;

import com.veterinaria.modelo.Menu;
import com.veterinaria.modelo.MenuDAO;
import com.veterinaria.modelo.Perfil;
import com.veterinaria.modelo.PerfilDAO;
import com.veterinaria.modelo.Permiso;
import com.veterinaria.modelo.PermisoDAO;
import com.veterinaria.util.Context;
import com.veterinaria.util.ControllerHelper;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class PermisosC {
	@FXML private TableView<Menu> tvMenu;
    @FXML private Button btnAsignar;
    @FXML private ComboBox<Perfil> cboPerfil;
    @FXML private TableView<Permiso> tvAsignados;
    @FXML private Button btnQuitar;
    PerfilDAO perfilDAO = new PerfilDAO();
    MenuDAO menuDAO = new MenuDAO();
    PermisoDAO permisoDAO = new PermisoDAO();
    ControllerHelper helper = new ControllerHelper();
    
    public void initialize() {
    	try {
    		cargarPerfil();
    		llenarDatosMenu();
    		llenarDatosMenuAsignado();
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    @SuppressWarnings("unchecked")
	private void llenarDatosMenu() {
		try {
			tvMenu.getColumns().clear();
			tvMenu.getItems().clear();

			List<Menu> lista;
			ObservableList<Menu> datos = FXCollections.observableArrayList();
			lista = menuDAO.getListaMenuAcivos();
			datos.setAll(lista);
			
			//llenar los datos en la tabla
			TableColumn<Menu, String> codigoColum = new TableColumn<>("Código");
			codigoColum.setMinWidth(10);
			codigoColum.setPrefWidth(80);
			codigoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Menu,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Menu, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdMenu()));
				}
			});
			TableColumn<Menu, String> menuColum = new TableColumn<>("Menú");
			menuColum.setMinWidth(10);
			menuColum.setPrefWidth(200);
			menuColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Menu,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Menu, String> param) {
					String menuHijo = param.getValue().getDescripcion();
					String menuPadre = "";
					Menu mnuPadre = menuDAO.getMenuPadre(param.getValue().getIdMenuPadre());
					if(mnuPadre != null)
						menuPadre = mnuPadre.getDescripcion(); 
					String menu = menuPadre + " --> " + menuHijo;
					return new SimpleObjectProperty<String>(menu);
				}
			});
			tvMenu.getColumns().addAll(codigoColum, menuColum);
			tvMenu.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
    @SuppressWarnings("unchecked")
	private void llenarDatosMenuAsignado() {
		try {
			tvAsignados.getColumns().clear();
			tvAsignados.getItems().clear();

			//llenar los datos en la tabla
			TableColumn<Permiso, String> codigoColum = new TableColumn<>("Código");
			codigoColum.setMinWidth(10);
			codigoColum.setPrefWidth(80);
			codigoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Permiso,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Permiso, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getMenu().getIdMenu()));
				}
			});
			TableColumn<Permiso, String> menuColum = new TableColumn<>("Menú");
			menuColum.setMinWidth(10);
			menuColum.setPrefWidth(200);
			menuColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Permiso,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Permiso, String> param) {
					String menuHijo = param.getValue().getMenu().getDescripcion();
					String menuPadre = "";
					Menu mnuPadre = menuDAO.getMenuPadre(param.getValue().getMenu().getIdMenuPadre());
					if(mnuPadre != null)
						menuPadre = mnuPadre.getDescripcion(); 
					String menu = menuPadre + " --> " + menuHijo;
					return new SimpleObjectProperty<String>(menu);
				}
			});
			tvAsignados.getColumns().addAll(codigoColum, menuColum);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
    private void cargarPerfil() {
    	try {
    		ObservableList<Perfil> datos = FXCollections.observableArrayList();
			List<Perfil> lista = perfilDAO.getListaPerfilesActivos();
			datos.addAll(lista);
			cboPerfil.setItems(datos);
			cboPerfil.setPromptText("Seleccione perfil");
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    
    public void cargarPermisos() {
    	try {
    		//cargar los permisos del perfil
    		tvAsignados.getItems().clear();
    		List<Permiso> lista;
			ObservableList<Permiso> datos = FXCollections.observableArrayList();
			lista = permisoDAO.getPermisos(cboPerfil.getSelectionModel().getSelectedItem().getIdPerfil());
			datos.setAll(lista);
			tvAsignados.setItems(datos);
			tvAsignados.refresh();
			
			//cargar los permisos pendientes
			tvMenu.getItems().clear();
			List<Menu> listaPendientes = new ArrayList<>();
			List<Menu> listaActivos;
			ObservableList<Menu> datosPendientes = FXCollections.observableArrayList();
			listaActivos = menuDAO.getListaMenuAcivos();
			boolean bandera = false;
			for(Menu mnu : listaActivos) {
				bandera = false;
				for(Permiso per : lista) {
					if(per.getMenu().getIdMenu() == mnu.getIdMenu())
						bandera = true;
				}
				if(bandera == false)
					listaPendientes.add(mnu);
			}
			
			datosPendientes.setAll(listaPendientes);
			tvMenu.setItems(datosPendientes);
			tvMenu.refresh();
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }

    public void asignar() {
    	try {
    		if(tvMenu.getSelectionModel().getSelectedItem() == null) {
    			helper.mostrarAlertaError("Debe Seleccionar un menu a asignar", Context.getInstance().getStage());
    			return;
    		}
    		Permiso permis = new Permiso();
    		permis.setEstado("A");
    		permis.setIdPermiso(null);
    		permis.setMenu(tvMenu.getSelectionModel().getSelectedItem());
    		permis.setPerfil(cboPerfil.getSelectionModel().getSelectedItem());
    		
    		permisoDAO.getEntityManager().getTransaction().begin();
    		permisoDAO.getEntityManager().persist(permis);
    		permisoDAO.getEntityManager().getTransaction().commit();
    		helper.mostrarAlertaInformacion("Menu Asignado", Context.getInstance().getStage());
    		cargarPermisos();
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }

    public void quitar() {
    	try {
    		if(tvAsignados.getSelectionModel().getSelectedItem() == null) {
    			helper.mostrarAlertaError("Debe Seleccionar un menu a quitar", Context.getInstance().getStage());
    			return;
    		}
    		Permiso permis = tvAsignados.getSelectionModel().getSelectedItem();
    		permis.setEstado("I");
    		
    		permisoDAO.getEntityManager().getTransaction().begin();
    		permisoDAO.getEntityManager().merge(permis);
    		permisoDAO.getEntityManager().getTransaction().commit();
    		helper.mostrarAlertaInformacion("Menu quitado del perfil", Context.getInstance().getStage());
    		cargarPermisos();
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
}
