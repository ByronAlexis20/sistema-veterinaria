package com.veterinaria.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.controlsfx.control.Notifications;

import com.veterinaria.main.LaunchVeterinaria;
import com.veterinaria.modelo.Menu;
import com.veterinaria.modelo.MenuDAO;
import com.veterinaria.modelo.Permiso;
import com.veterinaria.modelo.PermisoDAO;

import br.com.supremeforever.suprememdiwindow.MDICanvas;
import br.com.supremeforever.suprememdiwindow.MDIWindow;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class ControllerHelper {
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public void abrirPantallaModal(String uriVista, String titulo,Stage parent){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(LaunchVeterinaria.class.getResource(uriVista));
			Parent page = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle(titulo);
			stage.getIcons().add(new Image("/icono.png"));
			stage.initOwner(parent);
			stage.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(page);
			stage.setScene(scene);
			Context.getInstance().setStageModal(stage);
			stage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace(); //Retorna Connection reset cuando demora mucho
		}
	}
	public void abrirPantallaModalSecundario(String uriVista, String titulo,Stage parent){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(LaunchVeterinaria.class.getResource(uriVista));
			Parent page = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle(titulo);
			stage.getIcons().add(new Image("/icono.png"));
			stage.initOwner(parent);
			stage.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(page);
			stage.setScene(scene);
			Context.getInstance().setStageModalSecundario(stage);
			stage.showAndWait();
		} catch(Exception e) {
			e.printStackTrace(); //Retorna Connection reset cuando demora mucho
		}
	}
	public void abrirPantallaPrincipal() {
		try {
			ImageView imagen = new ImageView();
			Image ima = new Image("/imagen_ventana.png");
			imagen.setImage(ima);
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/icono.png"));
			stage.setTitle("Sistema de veterinaria");
			stage.setMaximized(true);
			
			MDICanvas canvas = new MDICanvas();
			canvas.setPrefSize(50000, 50000);
			canvas.setStyle("-fx-background-image: url('fondo.png'); -fx-background-size: 100% 100%; -fx-background-repeat: no-repeat;   ");
			PermisoDAO permisoDAO = new PermisoDAO();
			MenuDAO menuDAO = new MenuDAO();
			//armar el menu
			List<Menu> listaMenuPadre = new ArrayList<Menu>();
			boolean bandera = false;
			MenuBar menu = new MenuBar();
			menu.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; ");
			List<Permiso> listaPermiso = permisoDAO.getPermisos(Context.getInstance().getUsuario().getPerfil().getIdPerfil());
			if(listaPermiso.size() > 0) {
				for(Permiso per : listaPermiso) {
					//obtener los menus padres
					bandera = false;
					Menu menuPadre = menuDAO.getMenuPadre(per.getMenu().getIdMenuPadre());
					if(menuPadre != null) {
						for(Menu mnu : listaMenuPadre) {
							if(menuPadre.getIdMenu() == mnu.getIdMenu())
								bandera = true;
						}
						if(bandera == false)//aun no esta entre el listado de menus padre
							listaMenuPadre.add(menuPadre);
					}
				}
			}
			
			//recorro los menus padre
			for(Menu mnuP : listaMenuPadre) {
				javafx.scene.control.Menu menuPadre = new javafx.scene.control.Menu(mnuP.getDescripcion());
				//ahora agregar los hijos
				for(Permiso mnuH : listaPermiso) {//el listado de permisos solo tiene los hijos
					if(mnuH.getMenu().getIdMenuPadre() == mnuP.getIdMenu()) {
						MenuItem menuHijo = new MenuItem(mnuH.getMenu().getDescripcion());
						abrirModal(menuHijo,canvas,mnuH.getMenu().getFxml(),mnuH.getMenu().getDescripcion(),imagen,String.valueOf(mnuH.getMenu().getIdMenu()));
						menuPadre.getItems().add(menuHijo);
					}
				}
				menu.getMenus().add(menuPadre);
			}
			
			//menu de acceso rapido
			VBox box = new VBox();
			box.getChildren().addAll(menu,canvas);
			
			AnchorPane.setBottomAnchor(box, -35.0);
			AnchorPane.setLeftAnchor(box, 0d);
			AnchorPane.setRightAnchor(box, 0d);
			AnchorPane.setTopAnchor(box, 0d);
			
			FXMLLoader root = new FXMLLoader();
			root.setLocation(getClass().getResource("/principal/FrmPrincipal.fxml"));
			AnchorPane page = (AnchorPane) root.load();
			
			page.getChildren().add(box);
			Scene scene = new Scene(page);
			
			stage.setScene(scene);
			Context.getInstance().getStagePrincipal().close();
			stage.show();
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Optional<ButtonType> result = mostrarAlertaConfirmacion("Desea salir del sistema?",Context.getInstance().getStage());
					if(result.get() == ButtonType.OK)
						System.exit(0);
					else
						event.consume();
					//System.exit(0);
				}
			});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void abrirModal(MenuItem item,MDICanvas canvas,String fxml,String titulo,ImageView image,String idFxml) {
		try {
			item.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						System.out.println(fxml);
						AnchorPane root = FXMLLoader.load(getClass().getResource(fxml));
						MDIWindow window = new MDIWindow(idFxml,image , titulo, root);
						window.minHeight(500.0);
						
						//window.autosize();
						canvas.addMDIWindow(window);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public Parent ventanaParent(String uriVista){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(LaunchVeterinaria.class.getResource(uriVista));
			Parent page = (Parent) loader.load();
			return page;
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	public void mostrarAlertaError(String mensaje,Stage stage)
	{
		Image img = new Image("/icon_error.png", 70, 70,false,false);
		Notifications not = Notifications.create()
				.title("Información")
				.text(mensaje)
				.graphic(new ImageView(img))
				.hideAfter(Duration.seconds(2))
				.position(Pos.BOTTOM_RIGHT);
		not.darkStyle();
		not.show();
	}

	public void mostrarAlertaInformacion(String mensaje,Stage stage)
	{
		Image img = new Image("/icon_confirm.png", 70, 70,false,false);
		Notifications not = Notifications.create()
				.title("Información")
				.text(mensaje)
				.graphic(new ImageView(img))
				.hideAfter(Duration.seconds(2))
				.position(Pos.BOTTOM_RIGHT);
		not.darkStyle();
		not.show();
	}
	public void mostrarAlertaAdvertencia(String mensaje,Stage stage)
	{
		Alert dialogoAlert = new Alert(AlertType.WARNING);
		dialogoAlert.setTitle("Advertencia");
		dialogoAlert.setContentText(mensaje);
		dialogoAlert.initOwner(stage);
		dialogoAlert.show();
	}
	public Optional<ButtonType> mostrarAlertaConfirmacion(String mensaje,Stage stage){
		Alert dialogoAlert = new Alert(AlertType.CONFIRMATION);
		dialogoAlert.setTitle("Confirmación");
		dialogoAlert.setHeaderText(null);
		dialogoAlert.initStyle(StageStyle.UTILITY);
		dialogoAlert.setContentText(mensaje);
		dialogoAlert.initOwner(stage);
		return dialogoAlert.showAndWait();
	}
	public void mostrarVentanaContenedor(String uriVista,AnchorPane ap_contenedor){
		try{
			ap_contenedor.getChildren().removeAll();
			FXMLLoader loader = new FXMLLoader(LaunchVeterinaria.class.getResource(uriVista));
			AnchorPane page=(AnchorPane) loader.load();

			FadeTransition ft = new FadeTransition(Duration.millis(1000));
			ft.setNode(page);
			ft.setFromValue(0.1);
			ft.setToValue(1);
			ft.setCycleCount(1);
			ft.setAutoReverse(false);
			ft.play();
			AnchorPane.setBottomAnchor(page, 00.0);
			AnchorPane.setLeftAnchor(page, 00.0);
			AnchorPane.setTopAnchor(page, 00.0);
			AnchorPane.setRightAnchor(page, 00.0);
			ap_contenedor.getChildren().setAll(page);

		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public String encodeFileToBase64Binary(Image imagen) throws IOException{
		BufferedImage bImage = SwingFXUtils.fromFXImage(imagen, null);
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		ImageIO.write(bImage, "png", s);
		byte[] res  = s.toByteArray();
		s.close(); //especially if you are using a different output stream.
		return Base64.encodeBase64URLSafeString(res);
	}
	public ImageView getImageFromBase64String(String imageDataString) throws IOException {
		byte[] imgByte = Base64.decodeBase64(imageDataString);
		InputStream in = new ByteArrayInputStream(imgByte);
		BufferedImage bf = ImageIO.read(in);
		WritableImage wr = null;
		if (bf != null) {
			wr = new WritableImage(bf.getWidth(), bf.getHeight());
			PixelWriter pw = wr.getPixelWriter();
			for (int x = 0; x < bf.getWidth(); x++) {
				for (int y = 0; y < bf.getHeight(); y++) {
					pw.setArgb(x, y, bf.getRGB(x, y));
				}
			}
		}
		ImageView imView = new ImageView(wr);
		return imView;
	}
	public boolean validarDeCedula(String cedula) {
		boolean cedulaCorrecta = false;
		try {
			if (cedula.length() == 10) // ConstantesApp.LongitudCedula
			{
				int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
				if (tercerDigito < 6) {
					// Coeficientes de validación cédula
					// El decimo digito se lo considera dígito verificador
					int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
					int verificador = Integer.parseInt(cedula.substring(9,10));
					int suma = 0;
					int digito = 0;
					for (int i = 0; i < (cedula.length() - 1); i++) {
						digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
						suma += ((digito % 10) + (digito / 10));
					}

					if ((suma % 10 == 0) && (suma % 10 == verificador)) {
						cedulaCorrecta = true;
					}
					else if ((10 - (suma % 10)) == verificador) {
						cedulaCorrecta = true;
					} else {
						cedulaCorrecta = false;
					}
				} else {
					cedulaCorrecta = false;
				}
			} else {
				cedulaCorrecta = false;
			}
		} catch (NumberFormatException nfe) {
			cedulaCorrecta = false;
		} catch (Exception err) {
			System.out.println("Una excepcion ocurrio en el proceso de validadcion");
			cedulaCorrecta = false;
		}

		if (!cedulaCorrecta) {
			System.out.println("La Cédula ingresada es Incorrecta");
		}
		return cedulaCorrecta;
	}

	public boolean validarEmail(String email) {
		try{
			// Compiles the given regular expression into a pattern.
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			// Match the given input against this pattern
			Matcher matcher = pattern.matcher(email);
			return matcher.matches();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	
	public static BufferedImage createImageFromBytes(byte[] imageData) {
		ByteArrayInputStream bite = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(bite);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

