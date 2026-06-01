/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;

import com.mycompany.transporteshirata.Logica.Conductor;
import com.mycompany.transporteshirata.Logica.Login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * /**
 *
 * @author danie
 */
public class LoginDao {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean validarCredenciales(String rut, String contrasenaIngresada) {
        
        String sql = "SELECT clave FROM Conductor WHERE rut = ?";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, rut);
            rs = ps.executeQuery();

            if (rs.next()) {
                String contrasenaAlmacenada = rs.getString("clave");
                return contrasenaAlmacenada.equals(contrasenaIngresada);
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al validar credenciales: " + e.toString());
        }
        return false;
    }
}
