// File: module-info.java
module com.arkcode.proyecto_de_simulacion {
    // Requiere javafx.controls y javafx.fxml
    requires javafx.controls;
    requires javafx.fxml;
    // Abre el paquete com.arkcode.proyecto_de_simulacion a javafx.fxml
    opens com.arkcode.proyecto_de_simulacion to javafx.fxml;
    exports com.arkcode.proyecto_de_simulacion;
}