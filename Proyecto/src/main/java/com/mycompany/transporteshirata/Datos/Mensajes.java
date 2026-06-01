/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;

import java.awt.GraphicsEnvironment;
import javax.swing.JOptionPane;

/**
 *
 * @author pccas
 */
public class Mensajes {

    public static void mostrarError(String mensaje) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println(mensaje);
        } else {
            JOptionPane.showMessageDialog(null, mensaje);
        }
    }
}
