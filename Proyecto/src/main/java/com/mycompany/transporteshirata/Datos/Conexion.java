/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author pccas
 */
public class Conexion {
    
    private static final String URL = "jdbc:mysql://localhost:3306/Transportes_Hirata";
    
    // cambia la wea a tu cuenta
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "Catdog10"; 

    public static Connection getConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Conexión a BD Hirata: " + e.getMessage());
            return null;
        }
    }
}
