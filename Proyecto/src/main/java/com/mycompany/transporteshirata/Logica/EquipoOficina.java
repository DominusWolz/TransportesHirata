/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Logica;

/**
 *
 * @author Nicolas
 */
public class EquipoOficina {
    
    private int idEquipo;
    private String nombre;       
    private String tipo;         
    private String marca;
    private String modelo;
    private String estado;  
    
    public EquipoOficina(){           
    }
    
    public EquipoOficina(int idEquipo, String nombre, String tipo, String marca, String modelo, String estado){
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.estado = estado;
        }
    
    public int getIdEquipo() { 
        return idEquipo; 
    }
    public String getNombre(){
        return nombre;
    }
    public String getTipo() { 
        return tipo; 
    }
    public String getMarca() { 
        return marca; 
    }
    public String getModelo() {
        return modelo; 
    }

    public String getEstado() { 
        return estado; 
    }
    
    public void setIdEquipo(int idEquipo) { 
        this.idEquipo = idEquipo;
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre;
    }
    public void setTipo(String tipo) { 
        this.tipo = tipo;
    }
    public void setMarca(String marca) { 
        this.marca = marca;
    }
    public void setModelo(String modelo) { 
        this.modelo = modelo ;
    }
    
    public void setEstado(String estado) { 
        this.estado = estado ;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + tipo + " - " + marca + " " + modelo + ")";
    }
}
