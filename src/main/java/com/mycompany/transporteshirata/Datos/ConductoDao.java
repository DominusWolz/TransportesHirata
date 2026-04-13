/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;
import com.mycompany.transporteshirata.Logica.Conductor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author pccas
 */
public class ConductoDao {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

 
    public List<Conductor> listarConductores() {
        List<Conductor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Conductor";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Conductor c = new Conductor();
                c.setIdConductor(rs.getInt("idConductor"));
                c.setRut(rs.getString("rut"));
                c.setNombre(rs.getString("nombre"));
                c.setLicencia(rs.getString("licencia"));
                c.setTelefono(rs.getString("telefono"));
                c.setClave(rs.getString("clave"));
                lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar conductores: " + e.toString());
        }
        return lista;
    }


    public boolean registrarConductor(Conductor c) {
        
        String sql = "INSERT INTO Conductor (rut, nombre, licencia, telefono, clave) VALUES (?, ?, ?, ?, ?)";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getRut());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getLicencia());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getClave()); 
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar : " + e.toString());
            return false;
    }
    }

    public boolean modificarConductor(Conductor c) {
        
        String sql = "UPDATE Conductor SET rut=?, nombre=?, licencia=?, telefono=?, clave=? WHERE idConductor=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getRut());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getLicencia());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getClave()); 
            ps.setInt(6, c.getIdConductor());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.toString());
            return false;
        }
    }

 
    public boolean eliminarConductor(int id) {
        String sql = "DELETE FROM Conductor WHERE idConductor=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar. Probablemente tiene un camión asignado.");
            return false;
        }
    }

    public void create(Conductor con) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public List<Conductor> listarConductoresCbm() {
        List<Conductor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Conductor";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Conductor c = new Conductor();
                c.setRut(rs.getString("rut"));
                c.setNombre(rs.getString("nombre"));
                lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar conductores: " + e.toString());
        }
        return lista;
    }
}
