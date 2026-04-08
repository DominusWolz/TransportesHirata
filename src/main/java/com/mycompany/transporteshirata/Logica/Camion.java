/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Logica;
import com.mycompany.transporteshirata.GUI.GuiRegistrarCamion;
/**
 *
 * @author pccas
 */
public class Camion {
    private int idCamion;
    private String patente;
    private String marca;
    private String modelo;
    private int anio;
    private int kilometrajeActual;
    private Conductor conductor;

    // Constructor vacío pa recibir las weas de la base de datos
    public Camion() {
    }

    // Constructor completo pa mandar las weas a la base de datos
    public Camion(int idCamion, String patente, String marca, String modelo, int anio, int kilometrajeActual, Conductor conductor) {
        this.idCamion = idCamion;
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.kilometrajeActual = kilometrajeActual;
        this.conductor = conductor;
    }

    // Getters
    public int getIdCamion() { 
        return idCamion; 
    }
    public String getPatente() { 
        return patente; 
    }
    public String getMarca() { 
        return marca; 
    }
    public String getModelo() {
        return modelo; 
    }
    public int getAnio() { 
        return anio; 
    }
    public int getKilometrajeActual() { 
        return kilometrajeActual; 
    }
    public Conductor getConductor() { 
        return conductor; 
    }

    // Setters
    public void setIdCamion(int idCamion) { 
        this.idCamion = idCamion; 
    }
    public void setPatente(String patente) { 
        this.patente = patente; 
    }
    public void setMarca(String marca) { 
        this.marca = marca; 
    }
    public void setModelo(String modelo) { 
        this.modelo = modelo; 
    }
    public void setAnio(int anio) { 
        this.anio = anio; 
    }
    public void setKilometrajeActual(int kilometrajeActual) { 
        this.kilometrajeActual = kilometrajeActual;
    }
    public void setConductor(Conductor conductor) { 
        this.conductor = conductor; 
    }

    // toString para los ComboBox
    @Override
    public String toString() {
        return patente + " (" + marca + " " + modelo + ")";
    }
}
