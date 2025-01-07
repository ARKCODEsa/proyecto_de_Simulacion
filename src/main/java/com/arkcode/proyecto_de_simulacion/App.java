package com.arkcode.proyecto_de_simulacion;
// importar las clases necesarias
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
// Clase principal de la aplicación
public class App extends Application {
    // Método para cargar una vista FXML
    public static void loadFXML(String s) {
        // Cargar la vista FXML
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource
                    (s));
            Scene scene = new Scene(fxmlLoader.load(), 800, 400);

            Stage stage = new Stage();
            stage.setTitle("Simulador de Inventarios - Panadería");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("Para regresar a la ventana anterior, cierre esta ventana.");
            alert.showAndWait();

            // al cerrar la ventana actual, se abre la ventana anterior
            stage.setOnCloseRequest(_ -> {
                try {
                    FXMLLoader fxmlLoader2 = new FXMLLoader(App.class.getResource("HomeController_view.fxml"));
                    Scene scene2 = new Scene(fxmlLoader2.load(), 800, 400);
                    Stage stage2 = new Stage();
                    stage2.setTitle("Simulador de Inventarios - Panadería");
                    stage2.setScene(scene2);
                    stage2.setResizable(false);
                    stage2.centerOnScreen();
                    stage2.show();
                    // catch para manejar errores
                } catch (IOException e) {
                    // cuadro de dialogo de error
                    javafx.scene.control.Alert alert2 = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert2.setTitle("Error");
                    alert2.setHeaderText(null);
                    alert2.setContentText("No se pudo cargar la vista: HomeController_view.fxml");
                    alert2.showAndWait();
                }
            });
            // catch para manejar errores
        } catch (IOException e) {
            // cuadro de dialogo de error
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo cargar la vista: " + s);
            alert.showAndWait();
        }
    }
    // Método para iniciar la aplicación
    @Override
    public void start(Stage stage) throws IOException {

        // Cargar el archivo FXML de la vista
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeController_view.fxml")); // Cambia el nombre del archivo FXML si es necesario
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);  // Ajusta el tamaño de la ventana según lo necesario
        stage.setTitle("Simulador de Inventarios - Panadería");

        // Configuración y visualización de la ventana
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
    // Método principal de la aplicación para iniciar la aplicación
    public static void main(String[] args) {
        launch(args);  // Llamar a launch() para iniciar la aplicación
    }
}
