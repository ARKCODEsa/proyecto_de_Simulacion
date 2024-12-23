package com.arkcode.proyecto_de_simulacion;

public class InventarioRecord {
    private int time;
    private int demand;
    private int inventory;
    private boolean reorder;

    // Constructor
    public InventarioRecord(int time, int demand, int inventory, boolean reorder) {
        this.time = time;
        this.demand = demand;
        this.inventory = inventory;
        this.reorder = reorder;
    }

    // Getters y Setters
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public boolean isReorder() {
        return reorder;
    }

    public void setReorder(boolean reorder) {
        this.reorder = reorder;
    }
}
