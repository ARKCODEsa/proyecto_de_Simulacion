package com.arkcode.proyecto_de_simulacion;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.Random;

public class InventarioController {

    @FXML
    private TextField averageDemandField;
    @FXML
    private TextField reorderTimeField;
    @FXML
    private TextField reorderPointField;
    @FXML
    private TextField initialInventoryField;
    @FXML
    private TextField simulationTimeField;

    @FXML
    private Text finalInventoryText;
    @FXML
    private Text orderFrequencyText;

    @FXML
    private TableView<InventarioRecord> inventoryTable;
    @FXML
    private TableColumn<InventarioRecord, Integer> timeColumn;
    @FXML
    private TableColumn<InventarioRecord, Integer> demandColumn;
    @FXML
    private TableColumn<InventarioRecord, Integer> inventoryColumn;
    @FXML
    private TableColumn<InventarioRecord, Boolean> reorderColumn;

    @FXML
    private LineChart<Number, Number> inventoryChart;

    @FXML
    private StackPane stackPane;  // Contenedor para la vista 3D

    private ObservableList<InventarioRecord> inventoryRecords;

    private Timeline simulationTimeline;
    private int currentTime = 0;
    private int currentInventory;
    private int totalReorders = 0;
    private double EOQ;

    private boolean isSimulationRunning = false;

    @FXML
    private void initialize() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        demandColumn.setCellValueFactory(new PropertyValueFactory<>("demand"));
        inventoryColumn.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        reorderColumn.setCellValueFactory(new PropertyValueFactory<>("reorder"));

        inventoryRecords = FXCollections.observableArrayList();
        inventoryTable.setItems(inventoryRecords);

        inventoryChart.getData().clear();
    }

    @FXML
    private void clearSimulation() {
        inventoryRecords.clear();
        inventoryChart.getData().clear();
        finalInventoryText.setText("Nivel de Inventario Final: ");
        orderFrequencyText.setText("Frecuencia de Reorden: ");
        averageDemandField.clear();
        reorderTimeField.clear();
        reorderPointField.clear();
        initialInventoryField.clear();
        simulationTimeField.clear();

        if (simulationTimeline != null) {
            simulationTimeline.stop();
        }

        isSimulationRunning = false;
    }

    @FXML
    private void startSimulation() {
        if (!validateInputs()) {
            return;
        }

        if (isSimulationRunning) {
            showAlert(AlertType.INFORMATION, "Simulación en curso", "La simulación ya está en ejecución.");
            return;
        }

        double averageDemand = Double.parseDouble(averageDemandField.getText());
        double reorderTime = Double.parseDouble(reorderTimeField.getText());
        double reorderPoint = Double.parseDouble(reorderPointField.getText());
        double initialInventory = Double.parseDouble(initialInventoryField.getText());
        int simulationTime = Integer.parseInt(simulationTimeField.getText());

        EOQ = calculateEOQ(averageDemand, reorderTime);
        currentInventory = (int) initialInventory;

        inventoryRecords.clear();
        inventoryChart.getData().clear();

        XYChart.Series<Number, Number> inventorySeries = new XYChart.Series<>();
        inventorySeries.setName("Nivel de Inventario");
        inventoryChart.getData().add(inventorySeries);

        startAutoSimulation(simulationTime, inventorySeries);
    }

    private boolean validateInputs() {
        try {
            if (averageDemandField.getText().isEmpty() || Double.parseDouble(averageDemandField.getText()) <= 0) {
                throw new NumberFormatException("Demanda Promedio Inválida");
            }
            if (reorderTimeField.getText().isEmpty() || Double.parseDouble(reorderTimeField.getText()) <= 0) {
                throw new NumberFormatException("Tiempo de Reorden Inválido");
            }
            if (reorderPointField.getText().isEmpty() || Double.parseDouble(reorderPointField.getText()) < 0) {
                throw new NumberFormatException("Punto de Reorden Inválido");
            }
            if (initialInventoryField.getText().isEmpty() || Double.parseDouble(initialInventoryField.getText()) < 0) {
                throw new NumberFormatException("Nivel de Inventario Inicial Inválido");
            }
            if (simulationTimeField.getText().isEmpty() || Integer.parseInt(simulationTimeField.getText()) <= 0) {
                throw new NumberFormatException("Tiempo de Simulación Inválido");
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Entrada Incorrecta", e.getMessage());
            return false;
        }
        return true;
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private double calculateEOQ(double demand, double reorderTime) {
        double orderingCost = 100;
        double holdingCost = 20 + Math.random() * 10;
        return Math.sqrt((2 * demand * orderingCost) / holdingCost);
    }

    private int generateRandomDemand(double averageDemand, double variability) {
        Random rand = new Random();
        double demand = averageDemand + rand.nextGaussian() * variability;
        return Math.max(0, (int) demand);
    }

    private void startAutoSimulation(int simulationTime, XYChart.Series<Number, Number> inventorySeries) {
        currentTime = 0;
        totalReorders = 0;

        isSimulationRunning = true;

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1 + Math.random() * 0.5), e -> {
            int demand = generateRandomDemand(Double.parseDouble(averageDemandField.getText()), 10);
            boolean reorder = false;

            if (currentInventory <= Double.parseDouble(reorderPointField.getText())) {
                double reorderFactor = 1 + Math.random();
                currentInventory += EOQ * reorderFactor;
                reorder = true;
                totalReorders++;
            }

            currentInventory -= demand;
            if (currentInventory < 0) {
                currentInventory = 0;
            }

            inventoryRecords.add(new InventarioRecord(currentTime, demand, currentInventory, reorder));

            inventorySeries.getData().add(new XYChart.Data<>(currentTime, currentInventory));

            finalInventoryText.setText("Nivel de Inventario Final: " + currentInventory);
            orderFrequencyText.setText("Frecuencia de Reorden: " + totalReorders);

            currentTime++;

            if (currentTime >= simulationTime) {
                simulationTimeline.stop();
                isSimulationRunning = false;
                Platform.runLater(() -> showFinalResults());
            }

            update3DView(currentInventory);  // Actualizar la vista 3D
        });

        simulationTimeline = new Timeline(keyFrame);
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        simulationTimeline.play();
    }

    private void showFinalResults() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Simulación Terminada");
        alert.setHeaderText("Detalles de la Simulación:");
        alert.setContentText("Nivel Final de Inventario: " + currentInventory + "\n" +
                "Frecuencia de Reorden: " + totalReorders);
        alert.showAndWait();
    }

    // Método para actualizar la vista 3D (por ejemplo, un cilindro con base en el inventario)
    private void update3DView(int currentInventory) {
        Cylinder cylinder = new Cylinder(50, currentInventory);  // Modificar la altura del cilindro
        stackPane.getChildren().clear();  // Limpiar la vista 3D actual
        stackPane.getChildren().add(cylinder);  // Agregar el cilindro actualizado
    }
}
