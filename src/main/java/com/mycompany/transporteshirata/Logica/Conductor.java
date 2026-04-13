/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Logica;

/**
 *
 * @author pccas
 */
public class Conductor {

    private int idConductor;
    private String rut;
    private String nombre;
    private String licencia;
    private String telefono;
    private String clave;

    public Conductor() {
    }

    public Conductor(int idConductor, String rut, String nombre, String licencia, String telefono, String clave) {
        this.idConductor = idConductor;
        this.rut = rut;
        this.nombre = nombre;
        this.licencia = licencia;
        this.telefono = telefono;
        this.clave = clave;
    }

    public int getIdConductor() {
        return idConductor;
    }

    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLicencia() {
        return licencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getClave() {
        return clave;
    }

    public void setIdConductor(int idConductor) {
        this.idConductor = idConductor;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public String toString() {
        return rut + " - " + nombre;
    }
}
