module com.arkcode.proyecto_de_simulacion {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.arkcode.proyecto_de_simulacion to javafx.fxml;
    exports com.arkcode.proyecto_de_simulacion;
}