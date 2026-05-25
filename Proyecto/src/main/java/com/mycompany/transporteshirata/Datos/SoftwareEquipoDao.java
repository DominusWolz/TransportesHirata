/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;

import com.mycompany.transporteshirata.Logica.EquipoOficina;
import com.mycompany.transporteshirata.Logica.SoftwareEquipo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author asus
 */
public class SoftwareEquipoDao {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<SoftwareEquipo> listarTodos() {
        List<SoftwareEquipo> lista = new ArrayList<>();
        String sql = "SELECT s.*, e.nombre, e.tipo AS tipoEquipo, e.marca, e.modelo, "
                   + "e.identificador, e.estado "
                   + "FROM SoftwareEquipo s "
                   + "INNER JOIN EquipoOficina e ON s.idEquipo = e.idEquipo";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                SoftwareEquipo sw = new SoftwareEquipo();
                sw.setIdSoftware(rs.getInt("idSoftware"));
                sw.setNombreSoftware(rs.getString("nombreSoftware"));
                sw.setVersion(rs.getString("version"));
                java.sql.Date fi = rs.getDate("fechaInstalacion");
                if (fi != null) sw.setFechaInstalacion(fi.toLocalDate());
                java.sql.Date fa = rs.getDate("fechaActualizacion");
                if (fa != null) sw.setFechaActualizacion(fa.toLocalDate());
                sw.setObservaciones(rs.getString("observaciones"));
                EquipoOficina eq = new EquipoOficina();
                eq.setIdEquipo(rs.getInt("idEquipo"));
                eq.setNombre(rs.getString("nombre"));
                eq.setTipo(rs.getString("tipoEquipo"));
                eq.setMarca(rs.getString("marca"));
                eq.setModelo(rs.getString("modelo"));
                try { eq.setNumeroIdentificador(rs.getInt("identificador")); } catch (Exception ex) {}
                eq.setEstado(rs.getString("estado"));
                sw.setEquipo(eq);
                lista.add(sw);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar software: " + e.toString());
        }
        return lista;
    }
    public boolean registrarSoftware(SoftwareEquipo sw) {
        if (existeSoftwareEnEquipo(sw.getEquipo().getIdEquipo(), sw.getNombreSoftware())) {
            JOptionPane.showMessageDialog(null,
                "❌ \"" + sw.getNombreSoftware() + "\" ya está registrado en ese equipo.\n"
                + "Selecciónelo en la tabla y use 'Actualizar Versión'.");
            return false;
        }
        String sql = "INSERT INTO SoftwareEquipo (idEquipo, nombreSoftware, version, "
                   + "fechaInstalacion, fechaActualizacion, observaciones) VALUES (?,?,?,?,?,?)";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, sw.getEquipo().getIdEquipo());
            ps.setString(2, sw.getNombreSoftware());
            ps.setString(3, sw.getVersion());
            ps.setDate(4, java.sql.Date.valueOf(sw.getFechaInstalacion()));
            ps.setDate(5, sw.getFechaActualizacion() != null
                    ? java.sql.Date.valueOf(sw.getFechaActualizacion()) : null);
            ps.setString(6, sw.getObservaciones());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar software: " + e.toString());
            return false;
        }
    }

    public boolean actualizarVersion(SoftwareEquipo sw) {
        String sql = "UPDATE SoftwareEquipo SET version=?, fechaActualizacion=?, observaciones=? "
                   + "WHERE idSoftware=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, sw.getVersion());
            ps.setDate(2, sw.getFechaActualizacion() != null
                    ? java.sql.Date.valueOf(sw.getFechaActualizacion()) : null);
            ps.setString(3, sw.getObservaciones());
            ps.setInt(4, sw.getIdSoftware());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar versión: " + e.toString());
            return false;
        }
    }

    public boolean eliminarSoftware(int idSoftware) {
        String sql = "DELETE FROM SoftwareEquipo WHERE idSoftware=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idSoftware);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar software: " + e.toString());
            return false;
        }
    }

    private boolean existeSoftwareEnEquipo(int idEquipo, String nombreSoftware) {
        String sql = "SELECT COUNT(*) FROM SoftwareEquipo WHERE idEquipo=? AND nombreSoftware=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEquipo);
            ps.setString(2, nombreSoftware);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar software: " + e.toString());
        }
        return false;
    }
}
