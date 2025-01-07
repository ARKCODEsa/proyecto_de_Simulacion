package com.arkcode.proyecto_de_simulacion;
// importar las clases necesarias
import javafx.fxml.FXML;
import javafx.scene.control.Button;

// Clase HomeController
public class HomeController {

    // Atributos de la clase HomeController
    @FXML
    private Button btn_iniciar;
    // Método para inicializar la vista
    public void initialize() {

        // cambiar el color del boton cuando el mouse esta encima a azul claro y cuando sale a rojo
        btn_iniciar.setOnMouseEntered(e -> btn_iniciar.setStyle("-fx-background-color: rgb(200,179,179);"));
        btn_iniciar.setOnMouseExited(e -> btn_iniciar.setStyle("-fx-background-color: red;"));


        // Evento para el botón Iniciar
        btn_iniciar.setOnAction(_ -> {
            // Cargar la vista InventarioController_view.fxml
            App.loadFXML("InventarioController_view.fxml");

            // desaparezca la ventana actual
            btn_iniciar.getScene().getWindow().hide();

        });
    }

}


