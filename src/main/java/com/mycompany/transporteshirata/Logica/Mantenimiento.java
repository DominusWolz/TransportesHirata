/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Logica;
import java.time.LocalDate;
/**
 *
 * @author pccas
 */
public class Mantenimiento {
    private int idMantenimiento;
    private LocalDate fecha;
    private String tipo; // Preventivo o Correctivo
    private String descripcion;
    private int kilometrajeMantenimiento; // Km que tiene el camión el día del mantenimiento
    private Camion camion;


    public Mantenimiento() {
    }


    public Mantenimiento(int idMantenimiento, LocalDate fecha, String tipo, String descripcion, int kilometrajeMantenimiento, Camion camion) {
        this.idMantenimiento = idMantenimiento;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.kilometrajeMantenimiento = kilometrajeMantenimiento;
        this.camion = camion;
    }


    public int getIdMantenimiento() { 
        return idMantenimiento;
    }
    public LocalDate getFecha() {
        return fecha; 
    }
    public String getTipo() {
        return tipo; 
    }
    public String getDescripcion() {
        return descripcion; 
    }
    public int getKilometrajeMantenimiento() {
        return kilometrajeMantenimiento; 
    }
    public Camion getCamion() { 
        return camion; 
    }


    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento; 
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha; 
    }
    public void setTipo(String tipo) {
        this.tipo = tipo; 
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setKilometrajeMantenimiento(int kilometrajeMantenimiento) {
        this.kilometrajeMantenimiento = kilometrajeMantenimiento; 
    }
    public void setCamion(Camion camion) {
        this.camion = camion; 
    }
    
    
}
