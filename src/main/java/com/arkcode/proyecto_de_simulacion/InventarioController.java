package com.arkcode.proyecto_de_simulacion;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import javafx.scene.shape.Cylinder;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InventarioController {
    @FXML
    public Button startSimulationButton;
    @FXML
    public Button clearSimulationButton;
    @FXML
    private TableView<Producto> productosTable;
    @FXML
    private TableColumn<Producto, String> nombreColumn;
    @FXML
    private TableColumn<Producto, Integer> cantidadDispColumn;
    @FXML
    private TableColumn<Producto, Integer> demandaColumn;
    @FXML
    private TableColumn<Producto, Integer> tiempoReordenColumn;

    @FXML
    private TextField nombreProductoField;
    @FXML
    private TextField cantidadProductoField;
    @FXML
    private TextField demandaProductoField;
    @FXML
    private TextField tiempoReordenProductoField;

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
    private Inventario inventario = new Inventario();

    @FXML
    private Button editarButton;

    @FXML
    private Button eliminarButton;

    @FXML
    private Button calcularButton;

    @FXML
    private Button agregarButton;



    @FXML
    private void initialize() {

        // cambiar el color del boton cuando el mouse esta encima

        startSimulationButton.setOnMouseEntered(e -> startSimulationButton.setStyle("-fx-background-color: rgb(123,255,0);"));
        startSimulationButton.setOnMouseExited(e -> startSimulationButton.setStyle("-fx-background-color: blue;"));

        clearSimulationButton.setOnMouseEntered(e -> clearSimulationButton.setStyle("-fx-background-color: rgb(123,255,0);"));
        clearSimulationButton.setOnMouseExited(e -> clearSimulationButton.setStyle("-fx-background-color: red;"));

        editarButton.setOnMouseEntered(e -> editarButton.setStyle("-fx-background-color: rgb(123,255,0);"));
        editarButton.setOnMouseExited(e -> editarButton.setStyle("-fx-background-color: red;"));

        eliminarButton.setOnMouseEntered(e -> eliminarButton.setStyle("-fx-background-color: rgb(123,255,0);"));
        eliminarButton.setOnMouseExited(e -> eliminarButton.setStyle("-fx-background-color: red;"));

        calcularButton.setOnMouseEntered(e -> calcularButton.setStyle("-fx-background-color: rgb(123,255,0);"));
        calcularButton.setOnMouseExited(e -> calcularButton.setStyle("-fx-background-color: blue;"));

        agregarButton.setOnMouseEntered(e -> agregarButton.setStyle("-fx-background-color: rgb(123,255,0);"));
        agregarButton.setOnMouseExited(e -> agregarButton.setStyle("-fx-background-color: red;"));



        inventario = new Inventario();

        // Configurar columnas de tabla (ya lo tienes implementado)
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cantidadDispColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
        demandaColumn.setCellValueFactory(new PropertyValueFactory<>("demandaDiaria"));
        tiempoReordenColumn.setCellValueFactory(new PropertyValueFactory<>("tiempoReorden"));

        productosTable.setItems(FXCollections.observableArrayList(inventario.getProductos()));

        // Actualizar la gráfica con los productos al inicializar
        actualizarGraficaConProductos();

        // Configurar las columnas de la tabla
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cantidadDispColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
        demandaColumn.setCellValueFactory(new PropertyValueFactory<>("demandaDiaria"));
        tiempoReordenColumn.setCellValueFactory(new PropertyValueFactory<>("tiempoReorden"));

        productosTable.setItems(FXCollections.observableArrayList(inventario.getProductos()));

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
    private void startAutoSimulation(int simulationTime, XYChart.Series<Number, Number> inventorySeries) {
        currentTime = 0;
        totalReorders = 0;
        isSimulationRunning = true;

        // Crear el Timeline con un KeyFrame que ejecuta la simulación día a día
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), _ -> {
            if (currentTime >= simulationTime) {
                // Detenemos la simulación al finalizar automáticamente
                stopSimulation();
                showFinalResults();
                return; // Detener cualquier procesamiento adicional
            }

            // Generar la demanda y actualizar el inventario
            int demand = generateRandomDemand(Double.parseDouble(averageDemandField.getText()), 10);
            boolean reorder = false;

            if (currentInventory <= Double.parseDouble(reorderPointField.getText())) {
                double reorderFactor = 1 + Math.random();
                currentInventory += (int) (EOQ * reorderFactor);
                reorder = true;
                totalReorders++;
            }

            currentInventory -= demand;
            currentInventory = Math.max(0, currentInventory); // Asegurar que no sea negativo

            // Agregar datos al registro y al gráfico
            inventoryRecords.add(new InventarioRecord(currentTime, demand, currentInventory, reorder));
            inventorySeries.getData().add(new XYChart.Data<>(currentTime, currentInventory));

            // Actualizar los rótulos
            finalInventoryText.setText("Nivel de Inventario Final: " + currentInventory);
            orderFrequencyText.setText("Frecuencia de Reorden: " + totalReorders);

            // Incrementar tiempo
            currentTime++;

            // Actualizar la vista 3D (si aplica)
            update3DView(currentInventory);
        });

        // Configuramos el Timeline
        simulationTimeline = new Timeline(keyFrame);
        simulationTimeline.setCycleCount(simulationTime); // Duración total
        simulationTimeline.play();
    }

    private void stopSimulation() {
        if (simulationTimeline != null) {
            simulationTimeline.stop(); // Detener el Timeline
            showFinalResults();
        }

        if (isSimulationRunning) {
            isSimulationRunning = false; // Actualizar el estado de la simulación
            startSimulationButton.setText("Iniciar Simulación"); // Cambiar el texto del botón

            // Mostrar los resultados automáticamente
            showFinalResults();
        }
    }

    @FXML
    private void startSimulation() {
        if (isSimulationRunning) {
            // Si la simulación está en curso, detenerla
            stopSimulation(); // Llama al método ya creado para detener la simulación
            return;
        }
        // Calcular el tiempo de reorden antes de iniciar, si aplica
        actualizarTiempoReorden();

        // Validar entradas antes de continuar
        if (!validateInputs()) {
            return;
        }

        // Verificar si hay una simulación en curso
        if (isSimulationRunning) {
            showAlert(Alert.AlertType.INFORMATION, "Simulación en curso", "La simulación ya está en ejecución.");
            return;
        }

        // Obtener el tiempo de simulación desde el campo de texto
        int simulationTime = Integer.parseInt(simulationTimeField.getText());

        // Limpiar los datos de la gráfica y registros previos
        inventoryRecords.clear();
        inventoryChart.getData().clear();

        // Configurar la simulación para cada producto en el inventario
        for (Producto producto : inventario.getProductos()) {
            // Crear un nuevo conjunto de datos para este producto
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(producto.getNombre()); // Mostrar el nombre del producto en el gráfico

            // Inicializar el inventario actual del producto
            currentInventory = producto.getCantidadDisponible();

            // Iniciar simulación dinámica para este producto
            startAutoSimulation(simulationTime, series);

            // Añadir la serie de este producto a la gráfica
            inventoryChart.getData().add(series);
        }

        // Configuración final: activar el indicador de simulación en curso
        isSimulationRunning = true;

        // **Actualizar el texto del botón**
        startSimulationButton.setText("Detener Simulación");

        showFinalResults();
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


    private void showFinalResults() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulación Terminada");
        alert.setHeaderText("Detalles de la Simulación:");
        alert.setContentText("Nivel Final de Inventario: " + currentInventory + "\n" +
                "Frecuencia de Reorden: " + totalReorders);
        alert.showAndWait(); // Mostrar el mensaje y esperar interacción del usuario
    }

    // Método para actualizar la vista 3D (por ejemplo, un cilindro con base en el inventario)
    private void update3DView(int currentInventory) {
        Cylinder cylinder = new Cylinder(50, currentInventory);  // Modificar la altura del cilindro
        stackPane.getChildren().clear();  // Limpiar la vista 3D actual
        stackPane.getChildren().add(cylinder);  // Agregar el cilindro actualizado
    }

    @FXML
    private void agregarProductoAction() {
        try {
            String nombre = nombreProductoField.getText();
            int cantidad = Integer.parseInt(cantidadProductoField.getText());
            int demanda = Integer.parseInt(demandaProductoField.getText());
            int tiempoReorden = Integer.parseInt(tiempoReordenProductoField.getText());

            Producto producto = new Producto(nombre, cantidad, demanda, tiempoReorden);
            inventario.agregarProducto(producto);

            productosTable.getItems().setAll(inventario.getProductos());

            actualizarGraficaConProductos(); // Actualizar la gráfica después de agregar un producto
            limpiarCampos();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Entrada inválida", "Por favor, ingrese datos válidos.");
        }
    }



    private void limpiarCampos() {
        nombreProductoField.clear();
        cantidadProductoField.clear();
        demandaProductoField.clear();
        tiempoReordenProductoField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void actualizarDatosSimulacion() {
        double demandaPromedio = inventario.calcularDemandaPromedio();
        int inventarioInicial = inventario.calcularNivelInventarioInicial();
        int puntoReorden = inventario.calcularPuntoReorden();

        averageDemandField.setText(String.format("%.2f", demandaPromedio));
        initialInventoryField.setText(String.valueOf(inventarioInicial));
        reorderPointField.setText(String.valueOf(puntoReorden));
    }

    @FXML
    public void editarProductoAction() {
        // Obtener el producto seleccionado en la tabla
        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            // Mostrar alerta si no hay un producto seleccionado
            showAlert(Alert.AlertType.WARNING, "Selección requerida", "Debe seleccionar un producto para editar.");
            return;
        }

        try {
            // Obtener valores de los campos del formulario
            String nuevoNombre = nombreProductoField.getText().trim();
            if (nuevoNombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
            }
            int nuevaCantidad = Integer.parseInt(cantidadProductoField.getText());
            int nuevaDemanda = Integer.parseInt(demandaProductoField.getText());
            int nuevoTiempoReorden = Integer.parseInt(tiempoReordenProductoField.getText());

            // Crear un objeto de producto actualizado
            Producto productoActualizado = new Producto(nuevoNombre, nuevaCantidad, nuevaDemanda, nuevoTiempoReorden);

            // Llamar al método editarProducto en Inventario
            inventario.editarProducto(productoSeleccionado.getNombre(), productoActualizado);

            // Actualizar la tabla
            productosTable.getItems().setAll(inventario.getProductos());

            // Limpiar los campos del formulario tras la edición
            limpiarCampos();

            // Mostrar confirmación al usuario
            showAlert(Alert.AlertType.INFORMATION, "Edición Exitosa", "El producto ha sido actualizado correctamente.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Entrada inválida", "Por favor, ingrese valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    public void eliminarProductoAction() {
        // Obtener el producto seleccionado en la tabla
        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            // Mostrar alerta si no hay un producto seleccionado
            showAlert(Alert.AlertType.WARNING, "Selección requerida", "Debe seleccionar un producto para eliminar.");
            return;
        }

        // Mostrar una confirmación antes de eliminar
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar el producto " + productoSeleccionado.getNombre() + "?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Llamar al método eliminarProducto en Inventario
                inventario.eliminarProducto(productoSeleccionado.getNombre());

                // Actualizar la tabla después de eliminar
                productosTable.getItems().setAll(inventario.getProductos());

                // Mostrar confirmación de eliminación
                showAlert(Alert.AlertType.INFORMATION, "Eliminación Exitosa", "El producto ha sido eliminado correctamente.");
            }
        });
    }
    @FXML
    private void actualizarGraficaConProductos() {
        inventoryChart.getData().clear(); // Limpiar todas las líneas previas del gráfico

        // Verificar si hay productos en el inventario
        if (inventario.getProductos().isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Sin datos", "No hay productos en el inventario para graficar.");
            return;
        }

        // Iterar sobre cada producto para generar su línea en la gráfica
        for (Producto producto : inventario.getProductos()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(producto.getNombre()); // Identificar la serie con el nombre del producto

            // Inicializar datos para este producto
            int inventarioActual = producto.getCantidadDisponible(); // Inventario inicial
            int demandaPromedio = producto.getDemandaDiaria(); // Demanda promedio del producto
            int tiempoSimulacion = 30; // Tiempo de simulación (en días, puedes ajustarlo)

            // Generar datos de simulación día a día
            for (int dia = 0; dia <= tiempoSimulacion; dia++) {
                // Reducir inventario con una demanda aleatoria
                int demanda = generarDemandaAleatoria(demandaPromedio);
                inventarioActual = Math.max(0, inventarioActual - demanda); // Garantizar que no sea negativo

                // Agregar un punto a la serie del producto
                series.getData().add(new XYChart.Data<>(dia, inventarioActual));

                // Reabastecimiento (supongamos un punto crítico en 5)
                if (inventarioActual <= 5) {
                    inventarioActual += 20; // Reabastecimiento de 20 unidades
                }
            }

            // Añadir la serie generada al gráfico
            inventoryChart.getData().add(series);
        }
    }
    private int generarDemandaAleatoria(int demandaPromedio) {
        Random random = new Random();
        return Math.max(1, demandaPromedio + random.nextInt(5) - 2); // Variaciones de -2 a +2
    }
    private int calcularTiempoReorden(Producto producto) {
        int inventarioActual = producto.getCantidadDisponible(); // Inventario inicial del producto
        int demandaPromedio = producto.getDemandaDiaria(); // Demanda diaria promedio
        int tiempoReorden = 0; // Días necesarios para alcanzar el reorden

        // Obtener el punto de reorden dinámicamente (puedes configurar la lógica)
        int puntoReorden = (int) Math.max(5, demandaPromedio * 2); // Ejemplo: 2 días de demanda promedio

        // Simulación día a día del consumo de inventario
        while (inventarioActual > puntoReorden) {
            // Generar demanda aleatoria con variabilidad para mayor realismo
            int demanda = generarDemandaAleatoria(demandaPromedio);
            inventarioActual -= demanda; // Reducir inventario con la demanda
            tiempoReorden++; // Incrementar días necesarios

            // Evitar que el inventario caiga en valores negativos
            if (inventarioActual <= 0) {
                inventarioActual = 0; // Forzamos a cero
                break; // Inventario agotado, terminamos la simulación
            }
        }

        // Regresar el tiempo total necesario para alcanzar el punto de reorden
        return tiempoReorden;
    }
    private int calcularTiempoReordenTotal() {
        List<Integer> tiemposReorden = new ArrayList<>();

        // Calcular el tiempo de reorden para cada producto
        for (Producto producto : inventario.getProductos()) {
            int tiempoReorden = calcularTiempoReorden(producto);
            tiemposReorden.add(tiempoReorden); // Guardar TR para el producto
        }

        // Aquí decides cómo combinar estos resultados:
        // Promedio de los tiempos de reorden
        int sumaTiempos = tiemposReorden.stream().mapToInt(Integer::intValue).sum();
        return sumaTiempos / tiemposReorden.size(); // Retornar promedio
    }
    @FXML
    public void actualizarTiempoReorden() {
        // Verificar si hay productos en el inventario
        if (inventario.getProductos().isEmpty()) {
            reorderTimeField.setText(""); // Limpiar el campo si el inventario está vacío
            return;
        }

        // Calcular el tiempo de reorden promedio para todos los productos
        int tiempoReorden = calcularTiempoReordenTotal();

        // Actualizar el campo reorderTimeField con el valor calculado
        reorderTimeField.setText(String.valueOf(tiempoReorden));
    }

}
