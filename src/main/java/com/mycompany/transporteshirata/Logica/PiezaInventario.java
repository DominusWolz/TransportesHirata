/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Logica;

/**
 *
 * @author pccas
 */
public class PiezaInventario {

    private int idPieza;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private int stockMinimo;
    private String ubicacion;

    public PiezaInventario() {
    }

    public PiezaInventario(int idPieza, String nombre, String descripcion, int cantidad, int stockMinimo, String ubicacion) {
        this.idPieza = idPieza;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
        this.ubicacion = ubicacion;
    }

    public int getIdPieza() {
        return idPieza;
    }

    public void setIdPieza(int idPieza) {
        this.idPieza = idPieza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        if (idPieza <= 0) {
            return nombre == null ? "Sin pieza" : nombre;
        }
        return nombre + " (stock: " + cantidad + ")";
    }
}
