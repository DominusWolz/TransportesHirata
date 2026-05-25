/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Logica;

import java.time.LocalDate;
/**
 *
 * @author asus
 */
public class SoftwareEquipo {
   private int idSoftware;
    private EquipoOficina equipo;
    private String nombreSoftware;
    private String version;
    private LocalDate fechaInstalacion;
    private LocalDate fechaActualizacion;
    private String observaciones;

    public SoftwareEquipo() {}

    public int getIdSoftware() { return idSoftware; }
    public void setIdSoftware(int idSoftware) { this.idSoftware = idSoftware; }
    public EquipoOficina getEquipo() { return equipo; }
    public void setEquipo(EquipoOficina equipo) { this.equipo = equipo; }
    public String getNombreSoftware() { return nombreSoftware; }
    public void setNombreSoftware(String nombreSoftware) { this.nombreSoftware = nombreSoftware; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public LocalDate getFechaInstalacion() { return fechaInstalacion; }
    public void setFechaInstalacion(LocalDate fechaInstalacion) { this.fechaInstalacion = fechaInstalacion; }
    public LocalDate getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDate fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return nombreSoftware + " v" + version +
               (equipo != null ? " (" + equipo.getNombre() + ")" : "");
    } 
}
