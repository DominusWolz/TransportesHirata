/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;

import com.mycompany.transporteshirata.Logica.EquipoOficina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Nicolas
 */
public class EquipoOficinaDao {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Listar todos los equipos de oficina
    public List<EquipoOficina> listarEquipos() {
        List<EquipoOficina> lista = new ArrayList<>();
        String sql = "SELECT * FROM EquipoOficina";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                EquipoOficina eq = new EquipoOficina();
                eq.setIdEquipo(rs.getInt("idEquipo"));
                eq.setNombre(rs.getString("nombre"));
                eq.setTipo(rs.getString("tipo"));
                eq.setMarca(rs.getString("marca"));
                eq.setModelo(rs.getString("modelo"));
                try {
                    eq.setNumeroIdentificador(rs.getInt("identificador"));
                } catch (Exception ex) {
                    eq.setNumeroIdentificador(0);
                }
                eq.setEstado(rs.getString("estado"));

                lista.add(eq);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar equipos: " + e.toString());
        }
        return lista;
    }

    // Registrar nuevo equipo
    public boolean registrarEquipo(EquipoOficina eq) {
        String sql = "INSERT INTO EquipoOficina (nombre, tipo, marca, modelo, identificador, estado) VALUES (?, ?, ?, ?, ?,?)";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, eq.getNombre());
            ps.setString(2, eq.getTipo());
            ps.setString(3, eq.getMarca());
            ps.setString(4, eq.getModelo());
            ps.setInt(5, eq.getNumeroIdentificador());
            ps.setString(6, eq.getEstado());

            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar equipo: " + e.toString());
            return false;
        }
    }
    // Modificar equipo existente
    public boolean modificarEquipo(EquipoOficina e) {
    String sql = "UPDATE EquipoOficina SET nombre=?, tipo=?, marca=?, modelo=?, identificador=?, estado=? WHERE idEquipo=?";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, e.getNombre());
        ps.setString(2, e.getTipo());
        ps.setString(3, e.getMarca());
        ps.setString(4, e.getModelo());
        if (e.getNumeroIdentificador() == 0) {
            ps.setNull(5, java.sql.Types.INTEGER);
        } else {
            ps.setInt(5, e.getNumeroIdentificador());
        }
        ps.setString(6, e.getEstado());
        ps.setInt(7, e.getIdEquipo());

        ps.executeUpdate();
        return true;
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al modificar equipo: " + ex.toString());
        return false;
    }
}
    public boolean eliminarEquipo(int id) {
    String sql = "DELETE FROM EquipoOficina WHERE idEquipo=?";
    try {
        con = Conexion.getConexion();
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.execute();
        return true;
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al eliminar equipo: " + e.toString());
        return false;
    }
    }
    // Validar duplicados por nombre (guardar)
    public boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM EquipoOficina WHERE nombre = ?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar nombre: " + e.toString());
        }
        return false;
    }

    // Validar duplicados por nombre (editar, excluyendo el mismo id)
    public boolean existeNombre(String nombre, int idEquipo) {
        String sql = "SELECT COUNT(*) FROM EquipoOficina WHERE nombre = ? AND idEquipo <> ?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setInt(2, idEquipo);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar nombre: " + e.toString());
        }
        return false;
    }
// Validar duplicados por identificador (guardar)
    public boolean existeIdentificador(int identificador) {
        String sql = "SELECT COUNT(*) FROM EquipoOficina WHERE identificador = ?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, identificador);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar identificador: " + e.toString());
        }
        return false;
    }

    // Validar duplicados por identificador (editar, excluyendo el mismo id)
    public boolean existeIdentificador(int identificador, int idEquipo) {
        String sql = "SELECT COUNT(*) FROM EquipoOficina WHERE identificador = ? AND idEquipo <> ?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, identificador);
            ps.setInt(2, idEquipo);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar identificador: " + e.toString());
        }
        return false;
    }
}