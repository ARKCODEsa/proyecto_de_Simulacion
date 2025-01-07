package com.arkcode.proyecto_de_simulacion;
// importar las clases necesarias
import java.util.ArrayList;
import java.util.List;
// Clase Inventario para manejar los productos
public class Inventario {
    // Atributos de la clase Inventario
    private List<Producto> productos;
    // Constructor de la clase Inventario

    public Inventario() {
        // Inicializar la lista de productos
        this.productos = new ArrayList<>();
    }
    // Métodos de la clase Inventario para agregar, eliminar, editar y buscar productos

    public List<Producto> getProductos() {
        // Obtener la lista de productos
        return productos;
    }

    public void agregarProducto(Producto producto) {
        // Agregar un producto a la lista
        productos.add(producto);
    }

    public void eliminarProducto(String nombre) {
        // Eliminar un producto por su nombre
        productos.removeIf(producto -> producto.getNombre().equals(nombre));
    }

    //editar producto
    public void editarProducto(String nombre, Producto producto) {
        // Buscar el producto por su nombre y reemplazarlo
        for (int i = 0; i < productos.size(); i++) {
            // Si el nombre del producto es igual al nombre del producto a editar
            if (productos.get(i).getNombre().equals(nombre)) {
                productos.set(i, producto);
                break;
            }
        }
    }

    public Producto buscarProducto(String nombre) {
        // Buscar un producto por su nombre
        return productos.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().orElse(null);
    }
    // Métodos de la clase Inventario para calcular la demanda promedio, el nivel de inventario inicial y el punto de reorden
    public double calcularDemandaPromedio() {
        // Por simplicidad, asumimos que la demanda promedio es el promedio de las demandas diarias de los productos.
        return productos.stream().mapToDouble(Producto::getDemandaDiaria).average().orElse(0.0);
    }
    //calcular nivel de inventario inicial
    public int calcularNivelInventarioInicial() {
        // Por simplicidad, asumimos que el nivel de inventario inicial es la suma de las cantidades
        return productos.stream().mapToInt(Producto::getCantidadDisponible).sum();
    }
    //calcular punto de reorden
    public int calcularPuntoReorden() {
        // Por simplicidad, asumimos que el punto de reorden es la suma de demandas multiplicadas
        // por el tiempo de reorden de cada producto.
        return productos.stream().mapToInt(p -> p.getDemandaDiaria() * p.getTiempoReorden()).sum();
    }
}