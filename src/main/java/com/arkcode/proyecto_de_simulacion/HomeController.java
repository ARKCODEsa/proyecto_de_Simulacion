package com.arkcode.proyecto_de_simulacion;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class HomeController {


    @FXML
    private Button btn_iniciar;

    public void initialize() {

        // cambiar el color del boton cuando el mouse esta encima a azul claro y cuando sale a rojo
        btn_iniciar.setOnMouseEntered(e -> btn_iniciar.setStyle("-fx-background-color: rgba(0,58,255,0.51);"));
        btn_iniciar.setOnMouseExited(e -> btn_iniciar.setStyle("-fx-background-color: #ffffff;"));


        // Evento para el botón Iniciar
        btn_iniciar.setOnAction(_ -> {
            // Cargar la vista InventarioController_view.fxml
            App.loadFXML("InventarioController_view.fxml");

            // desaparezca la ventana actual
            btn_iniciar.getScene().getWindow().hide();

        });
    }

}


