package com.arkcode.proyecto_de_simulacion;
// Clase Producto para almacenar los productos
public class Producto {
    // Atributos de la clase Producto para almacenar los productos
    private String nombre;
    private int cantidadDisponible;
    private int demandaDiaria;
    private int tiempoReorden;

    // Constructor de la clase Producto con parámetros para inicializar los atributos
    public Producto(String nombre, int cantidadDisponible, int demandaDiaria, int tiempoReorden) {
        this.nombre = nombre;
        this.cantidadDisponible = cantidadDisponible;
        this.demandaDiaria = demandaDiaria;
        this.tiempoReorden = tiempoReorden;
    }

    // Getters y Setters de la clase Producto para acceder a los atributos y modificarlos si es necesario
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public int getDemandaDiaria() {
        return demandaDiaria;
    }

    public void setDemandaDiaria(int demandaDiaria) {
        this.demandaDiaria = demandaDiaria;
    }

    public int getTiempoReorden() {
        return tiempoReorden;
    }

    public void setTiempoReorden(int tiempoReorden) {
        this.tiempoReorden = tiempoReorden;
    }
}