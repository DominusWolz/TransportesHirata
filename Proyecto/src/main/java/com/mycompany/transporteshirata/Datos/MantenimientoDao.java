/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;

import com.mycompany.transporteshirata.Logica.Camion;
import com.mycompany.transporteshirata.Logica.Mantenimiento;
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
public class MantenimientoDao {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<Mantenimiento> listarMantenimientos() {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT m.*, c.patente, c.marca, c.modelo FROM Mantenimiento m INNER JOIN Camion c ON m.idCamion = c.idCamion";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Mantenimiento m = new Mantenimiento();
                m.setIdMantenimiento(rs.getInt("idMantenimiento"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setTipo(rs.getString("tipo"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setKilometrajeMantenimiento(rs.getInt("kilometrajeMantenimiento"));

                Camion c = new Camion();
                c.setIdCamion(rs.getInt("idCamion"));
                c.setPatente(rs.getString("patente"));
                c.setMarca(rs.getString("marca"));
                c.setModelo(rs.getString("modelo"));
                m.setCamion(c);

                lista.add(m);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar mantenimientos: " + e.toString());
        }
        return lista;
    }

    public boolean registrarMantenimiento(Mantenimiento m) {
        String sql = "INSERT INTO Mantenimiento (fecha, tipo, descripcion, kilometrajeMantenimiento, idCamion) VALUES (?, ?, ?, ?, ?)";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(m.getFecha()));
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getDescripcion());
            ps.setInt(4, m.getKilometrajeMantenimiento());
            ps.setInt(5, m.getCamion().getIdCamion());

            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar mantenimiento: " + e.toString());
            return false;
        }
    }

    public boolean modificarMantenimiento(Mantenimiento m) {
        String sql = "UPDATE Mantenimiento SET fecha=?, tipo=?, descripcion=?, kilometrajeMantenimiento=?, idCamion=? WHERE idMantenimiento=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(m.getFecha()));
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getDescripcion());
            ps.setInt(4, m.getKilometrajeMantenimiento());
            ps.setInt(5, m.getCamion().getIdCamion());
            ps.setInt(6, m.getIdMantenimiento());

            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.toString());
            return false;
        }
    }

    public boolean eliminarMantenimiento(int id) {
        String sql = "DELETE FROM Mantenimiento WHERE idMantenimiento=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.toString());
            return false;
        }
    }

    public int obtenerUltimoKilometrajeMantenimiento(int idCamion) {
        int ultimoKm = 0;
        
        String sql = "SELECT MAX(kilometrajeMantenimiento) AS ultimo FROM Mantenimiento WHERE idCamion = ?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCamion);
            rs = ps.executeQuery();
            if (rs.next()) {
                ultimoKm = rs.getInt("ultimo");
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerUltimoKilometrajeMantenimiento: " + e.getMessage());
        }
        return ultimoKm;
    }
    
    public boolean modificarDetallesMantenimiento(Mantenimiento m) {
        String sql = "UPDATE Mantenimiento SET fecha=?, tipo=?, descripcion=?, kilometrajeMantenimiento=? WHERE idMantenimiento=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(m.getFecha()));
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getDescripcion());
            ps.setInt(4, m.getKilometrajeMantenimiento());
            ps.setInt(5, m.getIdMantenimiento());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar detalles: " + e.toString());
            return false;
        }
    }
}
