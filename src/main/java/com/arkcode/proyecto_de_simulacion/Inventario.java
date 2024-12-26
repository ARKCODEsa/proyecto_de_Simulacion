package com.arkcode.proyecto_de_simulacion;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void eliminarProducto(String nombre) {
        productos.removeIf(producto -> producto.getNombre().equals(nombre));
    }

    //editar producto
    public void editarProducto(String nombre, Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getNombre().equals(nombre)) {
                productos.set(i, producto);
                break;
            }
        }
    }

    public Producto buscarProducto(String nombre) {
        return productos.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().orElse(null);
    }

    public double calcularDemandaPromedio() {
        return productos.stream().mapToDouble(Producto::getDemandaDiaria).average().orElse(0.0);
    }

    public int calcularNivelInventarioInicial() {
        return productos.stream().mapToInt(Producto::getCantidadDisponible).sum();
    }

    public int calcularPuntoReorden() {
        // Por simplicidad, asumimos que el punto de reorden es la suma de demandas multiplicadas
        // por el tiempo de reorden de cada producto.
        return productos.stream().mapToInt(p -> p.getDemandaDiaria() * p.getTiempoReorden()).sum();
    }
}