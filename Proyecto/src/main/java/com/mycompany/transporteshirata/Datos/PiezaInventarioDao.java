/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;

import com.mycompany.transporteshirata.Logica.PiezaInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pccas
 */
public class PiezaInventarioDao {

    public PiezaInventarioDao() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS PiezaInventario ("
                + "idPieza INT AUTO_INCREMENT PRIMARY KEY,"
                + "nombre VARCHAR(100) NOT NULL UNIQUE,"
                + "descripcion VARCHAR(255),"
                + "cantidad INT NOT NULL DEFAULT 0,"
                + "stockMinimo INT NOT NULL DEFAULT 0,"
                + "ubicacion VARCHAR(100)"
                + ")";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return;
            }
            try (Statement st = con.createStatement()) {
                st.execute(sql);
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al preparar inventario de piezas: " + e.toString());
        }
    }

    public List<PiezaInventario> listarPiezas() {
        List<PiezaInventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM PiezaInventario ORDER BY nombre";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return lista;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    PiezaInventario pieza = new PiezaInventario();
                    pieza.setIdPieza(rs.getInt("idPieza"));
                    pieza.setNombre(rs.getString("nombre"));
                    pieza.setDescripcion(rs.getString("descripcion"));
                    pieza.setCantidad(rs.getInt("cantidad"));
                    pieza.setStockMinimo(rs.getInt("stockMinimo"));
                    pieza.setUbicacion(rs.getString("ubicacion"));
                    lista.add(pieza);
                }
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al listar piezas: " + e.toString());
        }
        return lista;
    }

    public boolean registrarPieza(PiezaInventario pieza) {
        if (existeNombre(pieza.getNombre(), 0)) {
            Mensajes.mostrarError("Ya existe una pieza registrada con ese nombre.");
            return false;
        }

        String sql = "INSERT INTO PiezaInventario (nombre, descripcion, cantidad, stockMinimo, ubicacion) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, pieza.getNombre());
                ps.setString(2, pieza.getDescripcion());
                ps.setInt(3, pieza.getCantidad());
                ps.setInt(4, pieza.getStockMinimo());
                ps.setString(5, pieza.getUbicacion());
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al registrar pieza: " + e.toString());
            return false;
        }
    }

    public boolean modificarPieza(PiezaInventario pieza) {
        if (existeNombre(pieza.getNombre(), pieza.getIdPieza())) {
            Mensajes.mostrarError("Ya existe otra pieza registrada con ese nombre.");
            return false;
        }

        String sql = "UPDATE PiezaInventario SET nombre=?, descripcion=?, cantidad=?, stockMinimo=?, ubicacion=? WHERE idPieza=?";
        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, pieza.getNombre());
                ps.setString(2, pieza.getDescripcion());
                ps.setInt(3, pieza.getCantidad());
                ps.setInt(4, pieza.getStockMinimo());
                ps.setString(5, pieza.getUbicacion());
                ps.setInt(6, pieza.getIdPieza());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al modificar pieza: " + e.toString());
            return false;
        }
    }

    public boolean eliminarPieza(int idPieza) {
        String sql = "DELETE FROM PiezaInventario WHERE idPieza=?";
        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, idPieza);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al eliminar pieza: " + e.toString());
            return false;
        }
    }

    public boolean descontarStock(int idPieza, int cantidadUsada) {
        String sql = "UPDATE PiezaInventario SET cantidad = cantidad - ? WHERE idPieza = ? AND cantidad >= ?";
        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, cantidadUsada);
                ps.setInt(2, idPieza);
                ps.setInt(3, cantidadUsada);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al descontar stock: " + e.toString());
            return false;
        }
    }

    private boolean existeNombre(String nombre, int idIgnorado) {
        String sql = "SELECT idPieza FROM PiezaInventario WHERE LOWER(nombre) = LOWER(?) AND idPieza <> ?";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return true;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, nombre);
                ps.setInt(2, idIgnorado);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al validar pieza duplicada: " + e.toString());
            return true;
        }
    }
}
