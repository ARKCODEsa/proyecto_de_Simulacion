package com.arkcode.proyecto_de_simulacion;

public class Producto {
    private String nombre;
    private int cantidadDisponible;
    private int demandaDiaria;
    private int tiempoReorden;

    public Producto(String nombre, int cantidadDisponible, int demandaDiaria, int tiempoReorden) {
        this.nombre = nombre;
        this.cantidadDisponible = cantidadDisponible;
        this.demandaDiaria = demandaDiaria;
        this.tiempoReorden = tiempoReorden;
    }

    // Getters y Setters
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