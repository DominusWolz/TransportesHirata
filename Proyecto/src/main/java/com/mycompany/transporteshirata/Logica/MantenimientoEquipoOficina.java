/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Logica;
import java.time.LocalDate;
/**
 *
 * @author Nicolas
 */
public class MantenimientoEquipoOficina {
    
    private int idMantenimiento;
    private LocalDate fecha;
    private String tipo;
    private String descripcion;
    private String observaciones;
    private EquipoOficina equipo;

    public MantenimientoEquipoOficina() {}

    public MantenimientoEquipoOficina(int idMantenimiento, LocalDate fecha, String tipo, String descripcion, String observaciones, EquipoOficina equipo) {
        this.idMantenimiento = idMantenimiento;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.equipo = equipo;
    }

    // Getters y Setters
    public int getIdMantenimiento() { return idMantenimiento; }
    public void setIdMantenimiento(int idMantenimiento) { this.idMantenimiento = idMantenimiento; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public EquipoOficina getEquipo() { return equipo; }
    public void setEquipo(EquipoOficina equipo) { this.equipo = equipo; }

    @Override
    public String toString() {
        return "Mantenimiento " + tipo + " en " + equipo.getNombre() + " (" + fecha + ")";
    }
    
    
    
}
