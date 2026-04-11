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
import javax.swing.JOptionPane;

/**
 * /**
 *
 * @author danie
 */
public class LoginDao {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Método que solo obtiene los datos del login (rut y contraseña)
    public Login obtenerDatosLogin(String rut) {
        Login login = null;
        String sql = "SELECT rutConductor, contrasena FROM Login_Conductor WHERE rutConductor = ?";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, rut);
            rs = ps.executeQuery();

            if (rs.next()) {
                login = new Login();
                login.setRutConductor(rs.getString("rutConductor"));
                login.setContrasena(rs.getString("contrasena"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener datos de login: " + e.toString());
        }
        return login;
    }

    public boolean validarCredenciales(String rut, String contrasenaIngresada) {
        String sql = "SELECT contrasena FROM Login_Conductor WHERE rutConductor = ?";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, rut);
            rs = ps.executeQuery();

            if (rs.next()) {
                String contrasenaAlmacenada = rs.getString("contrasena");
                // Comparación directa (asumiendo que no está encriptada)
                return contrasenaAlmacenada.equals(contrasenaIngresada);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al validar credenciales: " + e.toString());
        }
        return false;
    }
}
