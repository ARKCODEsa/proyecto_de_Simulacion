package com.arkcode.proyecto_de_simulacion;
// Importar las clases necesarias de JavaFX y JavaFX Collections

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

// Clase InventarioController para manejar la lógica de la vista InventarioController_view.fxml
public class InventarioController {
    // Atributos de la clase InventarioController
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
    // Atributos adicionales para la simulación y el inventario de productos (ya los tienes implementados)
    private ObservableList<InventarioRecord> inventoryRecords;
    //private Inventario inventario; // Ya lo tienes implementado en la clase Inventario
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


    // Método para inicializar la vista InventarioController_view.fxml (ya lo tienes implementado)
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

        // Crear un objeto de inventario y cargar los productos iniciales
        inventario = new Inventario();

        // Configurar columnas de tabla (ya lo tienes implementado)
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cantidadDispColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
        demandaColumn.setCellValueFactory(new PropertyValueFactory<>("demandaDiaria"));
        tiempoReordenColumn.setCellValueFactory(new PropertyValueFactory<>("tiempoReorden"));

        // Configurar columnas de tabla (ya lo tienes implementado)
        productosTable.setItems(FXCollections.observableArrayList(inventario.getProductos()));

        // Actualizar la gráfica con los productos al inicializar
        actualizarGraficaConProductos();

        // Configurar las columnas de la tabla
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cantidadDispColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
        demandaColumn.setCellValueFactory(new PropertyValueFactory<>("demandaDiaria"));
        tiempoReordenColumn.setCellValueFactory(new PropertyValueFactory<>("tiempoReorden"));

        // Configurar las columnas de la tabla (ya lo tienes implementado)
        productosTable.setItems(FXCollections.observableArrayList(inventario.getProductos()));

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        demandColumn.setCellValueFactory(new PropertyValueFactory<>("demand"));
        inventoryColumn.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        reorderColumn.setCellValueFactory(new PropertyValueFactory<>("reorder"));

        // Inicializar la lista de registros de inventario y la gráfica (ya lo tienes implementado)
        inventoryRecords = FXCollections.observableArrayList();
        inventoryTable.setItems(inventoryRecords);
        // Configurar la gráfica de inventario (ya lo tienes implementado)
        inventoryChart.getData().clear();
    }

    // Método para limpiar la simulación (ya lo tienes implementado)
    @FXML
    private void clearSimulation() {
        // Limpiar los registros de inventario y la gráfica
        inventoryRecords.clear();
        inventoryChart.getData().clear();
        finalInventoryText.setText("Nivel de Inventario Final: ");
        orderFrequencyText.setText("Frecuencia de Reorden: ");
        averageDemandField.clear();
        reorderTimeField.clear();
        reorderPointField.clear();
        initialInventoryField.clear();
        simulationTimeField.clear();
        // Detener la simulación si está en curso (ya lo tienes implementado)
        if (simulationTimeline != null) {
            simulationTimeline.stop();
        }
        // Actualizar el estado de la simulación (ya lo tienes implementado)
        isSimulationRunning = false;
    }

    // Método para iniciar la simulación (ya lo tienes implementado)
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

            // Calcular el EOQ si es necesario (ya lo tienes implementado)
            if (currentInventory <= Double.parseDouble(reorderPointField.getText())) {
                // Calcular el EOQ si el inventario está por debajo del punto de reorden (ya lo tienes implementado)
                double reorderFactor = 1 + Math.random();
                // Calcular el EOQ para este producto (ya lo tienes implementado)
                currentInventory += (int) (EOQ * reorderFactor);
                // Actualizar el indicador de reorden (ya lo tienes implementado)
                reorder = true;
                // Incrementar el contador de reorden (ya lo tienes implementado)
                totalReorders++;
            }

            // Actualizar el inventario actual (ya lo tienes implementado)
            currentInventory -= demand;
            currentInventory = Math.max(0, currentInventory); // Asegurar que no sea negativo

            // Agregar datos al registro y al gráfico
            inventoryRecords.add(new InventarioRecord(currentTime, demand, currentInventory, reorder));
            inventorySeries.getData().add(new XYChart.Data<>(currentTime, currentInventory));

            // Actualizar los rótulos de la interfaz
            finalInventoryText.setText("Nivel de Inventario Final: " + currentInventory);
            orderFrequencyText.setText("Frecuencia de Reorden: " + totalReorders);

            // Incrementar tiempo actual para el siguiente día de simulación (ya lo tienes implementado)
            currentTime++;

            // Actualizar la vista 3D (si aplica) (ya lo tienes implementado)
            update3DView(currentInventory);
        });

        // Configuramos el Timeline con el KeyFrame y la duración total de la simulación
        simulationTimeline = new Timeline(keyFrame);
        simulationTimeline.setCycleCount(simulationTime); // Duración total
        simulationTimeline.play();// Iniciar la simulación
    }

    // Método para detener la simulación (ya lo tienes implementado)
    private void stopSimulation() {
        // Detener la simulación si está en curso (ya lo tienes implementado)
        if (simulationTimeline != null) {
            simulationTimeline.stop(); // Detener el Timeline
            showFinalResults();
        }
        // Actualizar el estado de la simulación (ya lo tienes implementado)
        if (isSimulationRunning) {
            isSimulationRunning = false; // Actualizar el estado de la simulación
            startSimulationButton.setText("Iniciar Simulación"); // Cambiar el texto del botón

            // Mostrar los resultados automáticamente
            showFinalResults();
        }
    }

    // Método para iniciar o detener la simulación (ya lo tienes implementado)
    @FXML
    private void startSimulation() {
        // Verificar si la simulación está en curso (ya lo tienes implementado)
        if (isSimulationRunning) {
            // Si la simulación está en curso, detenerla (ya lo tienes implementado)
            stopSimulation(); // Llama al método ya creado para detener la simulación actual
            return;
        }
        // Calcular el tiempo de reorden antes de iniciar, si aplica (ya lo tienes implementado)
        actualizarTiempoReorden();

        // Validar entradas antes de continuar (ya lo tienes implementado)
        if (!validateInputs()) {
            return;
        }

        // Verificar si hay una simulación en curso (ya lo tienes implementado)
        if (isSimulationRunning) {
            // Mostrar un mensaje de alerta si la simulación ya está en curso (ya lo tienes implementado)
            showAlert(Alert.AlertType.INFORMATION, "Simulación en curso", "La simulación ya está en ejecución.");
            return;
        }

        // Obtener el tiempo de simulación desde el campo de texto  (ya lo tienes implementado)
        int simulationTime = Integer.parseInt(simulationTimeField.getText());

        // Limpiar los datos de la gráfica y registros previos (ya lo tienes implementado)
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
        // **Mostrar un mensaje de confirmación** (ya lo tienes implementado)
        showFinalResults();
    }

    // Método para validar las entradas de la simulación (ya lo tienes implementado)
    private boolean validateInputs() {
        // Validar los campos de entrada antes de continuar (ya lo tienes implementado)
        // try-catch para manejar errores
        try {
            // Verificar si los campos están vacíos o contienen valores no válidos
            if (averageDemandField.getText().isEmpty() || Double.parseDouble(averageDemandField.getText()) <= 0) {
                throw new NumberFormatException("Demanda Promedio Inválida");
            }
            // Verificar si los campos están vacíos o contienen valores no válidos
            if (reorderTimeField.getText().isEmpty() || Double.parseDouble(reorderTimeField.getText()) <= 0) {
                throw new NumberFormatException("Tiempo de Reorden Inválido");
            }
            // Verificar si los campos están vacíos o contienen valores no válidos
            if (reorderPointField.getText().isEmpty() || Double.parseDouble(reorderPointField.getText()) < 0) {
                throw new NumberFormatException("Punto de Reorden Inválido");
            }
            // Verificar si los campos están vacíos o contienen valores no válidos
            if (initialInventoryField.getText().isEmpty() || Double.parseDouble(initialInventoryField.getText()) < 0) {
                throw new NumberFormatException("Nivel de Inventario Inicial Inválido");
            }
            // Verificar si los campos están vacíos o contienen valores no válidos
            if (simulationTimeField.getText().isEmpty() || Integer.parseInt(simulationTimeField.getText()) <= 0) {
                throw new NumberFormatException("Tiempo de Simulación Inválido");
            }
            // catch para manejar errores
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Entrada Incorrecta", e.getMessage());
            return false;
        }
        // Validación exitosa
        return true;
    }


    // Método para calcular la cantidad económica de pedido (EOQ) (ya lo tienes implementado)
    private double calculateEOQ(double demand, double reorderTime) {
        // Calcular la cantidad económica de pedido (EOQ) con la fórmula dada (ya lo tienes implementado)
        double orderingCost = 100;
        //holding cost = 20 + Math.random() * 10; // Costo de mantenimiento
        double holdingCost = 20 + Math.random() * 10;
        // return Math.sqrt((2 * demand * orderingCost) / holdingCost); // Fórmula EOQ básica (ya lo tienes implementado)
        return Math.sqrt((2 * demand * orderingCost) / holdingCost);
    }

    // Método para generar una demanda aleatoria (ya lo tienes implementado)
    private int generateRandomDemand(double averageDemand, double variability) {
        // Generar una demanda aleatoria con una distribución normal (ya lo tienes implementado)
        Random rand = new Random();
        // Demanda = Demanda Promedio + Variabilidad * Número Aleatorio Normal (0, 1) (ya lo tienes implementado)
        double demand = averageDemand + rand.nextGaussian() * variability;
        // Asegurar que la demanda no sea negativa (ya lo tienes implementado)
        return Math.max(0, (int) demand);
    }


    // Método para mostrar los resultados finales de la simulación (ya lo tienes implementado)
    private void showFinalResults() {
        // Mostrar un mensaje de alerta con los resultados finales (ya lo tienes implementado)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulación Terminada");
        alert.setHeaderText("Detalles de la Simulación:");
        alert.setContentText("Nivel Final de Inventario: " + currentInventory + "\n" +
                "Frecuencia de Reorden: " + totalReorders);
        alert.showAndWait(); // Mostrar el mensaje y esperar interacción del usuario
    }

    // Método para actualizar la vista 3D con el inventario actual (ya lo tienes implementado)
    //metodono implementado en la aplicacion de escritorio
    private void update3DView(int currentInventory) {
        Cylinder cylinder = new Cylinder(50, currentInventory);  // Modificar la altura del cilindro
        stackPane.getChildren().clear();  // Limpiar la vista 3D actual
        stackPane.getChildren().add(cylinder);  // Agregar el cilindro actualizado
    }

    // Método para mostrar un cuadro de diálogo de alerta (ya lo tienes implementado)
    @FXML
    private void agregarProductoAction() {
        // Validar los campos de entrada antes de continuar
        // try-catch para manejar errores
        try {
            // Verificar si los campos están vacíos o contienen valores no válidos
            String nombre = nombreProductoField.getText();
            int cantidad = Integer.parseInt(cantidadProductoField.getText());
            int demanda = Integer.parseInt(demandaProductoField.getText());
            int tiempoReorden = Integer.parseInt(tiempoReordenProductoField.getText());

            // Crear un nuevo producto con los datos ingresados
            Producto producto = new Producto(nombre, cantidad, demanda, tiempoReorden);
            // Agregar el producto al inventario
            inventario.agregarProducto(producto);
            // Actualizar la tabla con los productos
            productosTable.getItems().setAll(inventario.getProductos());
            // Mostrar un mensaje de confirmación al usuario
            actualizarGraficaConProductos(); // Actualizar la gráfica después de agregar un producto
            // limpiar los campos del formulario
            limpiarCampos();
            // catch para manejar errores
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Entrada inválida", "Por favor, ingrese datos válidos.");
        }
    }

    // limpiar los campos del formulario (ya lo tienes implementado)
    private void limpiarCampos() {
        nombreProductoField.clear();
        cantidadProductoField.clear();
        demandaProductoField.clear();
        tiempoReordenProductoField.clear();
    }

    // Método para mostrar un cuadro de diálogo de alerta (ya lo tienes implementado)
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para actualizar el tiempo de reorden (ya lo tienes implementado)
    @FXML
    public void actualizarDatosSimulacion() {
        // Calcular el tiempo de reorden y actualizar los campos de texto
        double demandaPromedio = inventario.calcularDemandaPromedio();
        // Calcular el nivel de inventario inicial y el punto de reorden
        int inventarioInicial = inventario.calcularNivelInventarioInicial();
        // Calcular el punto de reorden y actualizar los campos de texto (ya lo tienes implementado)
        int puntoReorden = inventario.calcularPuntoReorden();
        // Calcular la cantidad económica de pedido (EOQ) (ya lo tienes implementado)
        // explico averageDemandField.getText() y reorderTimeField.getText() son los valores actuales de los campos de texto
        averageDemandField.setText(String.format("%.2f", demandaPromedio));
        // Inicializar el EOQ con los valores actuales de los campos de texto
        initialInventoryField.setText(String.valueOf(inventarioInicial));
        // reorderPointField.setText(String.valueOf(puntoReorden)); // Actualizar el punto de reorden en el campo de texto
        reorderPointField.setText(String.valueOf(puntoReorden));
    }

    // Método para editar un producto existente (ya lo tienes implementado)
    @FXML
    public void editarProductoAction() {
        // Obtener el producto seleccionado en la tabla
        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            // Mostrar alerta si no hay un producto seleccionado
            showAlert(Alert.AlertType.WARNING, "Selección requerida", "Debe seleccionar un producto para editar.");
            return;
        }
        // Obtener los valores actuales del producto seleccionado
        // try-catch para manejar errores
        try {
            // Obtener valores de los campos del formulario
            String nuevoNombre = nombreProductoField.getText().trim();
            if (nuevoNombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
            }
            int nuevaCantidad = Integer.parseInt(cantidadProductoField.getText());
            int nuevaDemanda = Integer.parseInt(demandaProductoField.getText());
            int nuevoTiempoReorden = Integer.parseInt(tiempoReordenProductoField.getText());

            // Crear un objeto de producto actualizado con los nuevos valores ingresados
            Producto productoActualizado = new Producto(nuevoNombre, nuevaCantidad, nuevaDemanda, nuevoTiempoReorden);

            // Llamar al método editarProducto en Inventario para actualizar el producto
            inventario.editarProducto(productoSeleccionado.getNombre(), productoActualizado);

            // Actualizar la tabla con los productos después de la edición
            productosTable.getItems().setAll(inventario.getProductos());

            // Limpiar los campos del formulario tras la edición exitosa (ya lo tienes implementado)
            limpiarCampos();

            // Mostrar confirmación al usuario
            showAlert(Alert.AlertType.INFORMATION, "Edición Exitosa", "El producto ha sido actualizado correctamente.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Entrada inválida", "Por favor, ingrese valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    // Método para eliminar un producto existente (ya lo tienes implementado)
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

    // Método para actualizar la gráfica con los productos del inventario (ya lo tienes implementado)
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
            // Crear una nueva serie para el producto actual (ya lo tienes implementado)
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            // Configurar el nombre de la serie con el nombre del producto (ya lo tienes implementado)
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

    // Método para generar una demanda aleatoria con variabilidad (ya lo tienes implementado)
    private int generarDemandaAleatoria(int demandaPromedio) {
        // Generar una demanda aleatoria con variabilidad para simular la demanda diaria
        Random random = new Random();
        // Variaciones de -2 a +2 unidades para la demanda promedio (ajustable) (ya lo tienes implementado)
        return Math.max(1, demandaPromedio + random.nextInt(5) - 2); // Variaciones de -2 a +2
    }

    // Método para calcular el tiempo de reorden para un producto específico (ya lo tienes implementado)
    private int calcularTiempoReorden(Producto producto) {
        // Calcular el tiempo de reorden para un producto específico
        // Obtener los datos del producto para la simulación
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

    // Método para calcular el tiempo de reorden promedio para todos los productos (ya lo tienes implementado)
    private int calcularTiempoReordenTotal() {
        // Calcular el tiempo de reorden promedio para todos los productos
        List<Integer> tiemposReorden = new ArrayList<>();

        // Calcular el tiempo de reorden para cada producto
        for (Producto producto : inventario.getProductos()) {
            int tiempoReorden = calcularTiempoReorden(producto);
            tiemposReorden.add(tiempoReorden); // Guardar TR = Tiempo de Reorden para este producto
        }

        // Aquí decides cómo combinar estos resultados:
        // Promedio de los tiempos de reorden
        int sumaTiempos = tiemposReorden.stream().mapToInt(Integer::intValue).sum();
        return sumaTiempos / tiemposReorden.size(); // Retornar promedio
    }

    // Método para actualizar el tiempo de reorden promedio (ya lo tienes implementado)
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
