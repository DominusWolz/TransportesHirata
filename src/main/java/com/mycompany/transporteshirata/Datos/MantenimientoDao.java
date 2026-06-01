/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;

import com.mycompany.transporteshirata.Logica.Camion;
import com.mycompany.transporteshirata.Logica.Mantenimiento;
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
public class MantenimientoDao {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<Mantenimiento> listarMantenimientos() {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT m.*, c.patente, c.marca, c.modelo, "
                + "p.idPieza, p.nombre AS nombrePieza, p.descripcion AS descripcionPieza, "
                + "p.cantidad, p.stockMinimo, p.ubicacion, mp.cantidadUsada "
                + "FROM Mantenimiento m "
                + "INNER JOIN Camion c ON m.idCamion = c.idCamion "
                + "LEFT JOIN MantenimientoPieza mp ON m.idMantenimiento = mp.idMantenimiento "
                + "LEFT JOIN PiezaInventario p ON mp.idPieza = p.idPieza "
                + "ORDER BY m.fecha DESC, m.idMantenimiento DESC";

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

                if (rs.getObject("idPieza") != null) {
                    PiezaInventario pieza = new PiezaInventario();
                    pieza.setIdPieza(rs.getInt("idPieza"));
                    pieza.setNombre(rs.getString("nombrePieza"));
                    pieza.setDescripcion(rs.getString("descripcionPieza"));
                    pieza.setCantidad(rs.getInt("cantidad"));
                    pieza.setStockMinimo(rs.getInt("stockMinimo"));
                    pieza.setUbicacion(rs.getString("ubicacion"));
                    m.setPiezaUsada(pieza);
                    m.setCantidadPiezaUsada(rs.getInt("cantidadUsada"));
                }

                lista.add(m);
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al listar mantenimientos: " + e.toString());
        }
        return lista;
    }

    public boolean registrarMantenimiento(Mantenimiento m) {
        if (m == null || m.getCamion() == null) {
            Mensajes.mostrarError("Debe seleccionar un camion para registrar el mantenimiento.");
            return false;
        }
        CamionDao camionDao = new CamionDao();
        if (!camionDao.existeCamion(m.getCamion().getIdCamion())) {
            Mensajes.mostrarError("El camion seleccionado ya no existe en la base de datos. Actualice la lista y seleccione un camion registrado.");
            return false;
        }

        Connection conexion = null;
        try {
            conexion = Conexion.getConexion();
            if (conexion == null) {
                return false;
            }
            conexion.setAutoCommit(false);

            if (usaPieza(m) && !descontarStock(conexion, m.getPiezaUsada().getIdPieza(), m.getCantidadPiezaUsada())) {
                conexion.rollback();
                Mensajes.mostrarError("No hay stock suficiente para registrar la pieza usada.");
                return false;
            }

            int idMantenimiento = insertarMantenimiento(conexion, m);
            if (usaPieza(m)) {
                registrarPiezaUsada(conexion, idMantenimiento, m.getPiezaUsada().getIdPieza(), m.getCantidadPiezaUsada());
            }

            conexion.commit();
            return true;
        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException rollbackError) {
                    Mensajes.mostrarError("Error al revertir mantenimiento: " + rollbackError.toString());
                }
            }
            Mensajes.mostrarError("Error al registrar mantenimiento: " + e.toString());
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    Mensajes.mostrarError("Error al cerrar conexion: " + e.toString());
                }
            }
        }
    }

    private int insertarMantenimiento(Connection conexion, Mantenimiento m) throws SQLException {
        String sql = "INSERT INTO Mantenimiento (fecha, tipo, descripcion, kilometrajeMantenimiento, idCamion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement psInsert = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            psInsert.setDate(1, java.sql.Date.valueOf(m.getFecha()));
            psInsert.setString(2, m.getTipo());
            psInsert.setString(3, m.getDescripcion());
            psInsert.setInt(4, m.getKilometrajeMantenimiento());
            psInsert.setInt(5, m.getCamion().getIdCamion());
            psInsert.executeUpdate();

            try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el ID del mantenimiento registrado.");
    }

    private boolean usaPieza(Mantenimiento m) {
        return m.getPiezaUsada() != null
                && m.getPiezaUsada().getIdPieza() > 0
                && m.getCantidadPiezaUsada() > 0;
    }

    private boolean descontarStock(Connection conexion, int idPieza, int cantidadUsada) throws SQLException {
        String sql = "UPDATE PiezaInventario SET cantidad = cantidad - ? WHERE idPieza = ? AND cantidad >= ?";
        try (PreparedStatement psStock = conexion.prepareStatement(sql)) {
            psStock.setInt(1, cantidadUsada);
            psStock.setInt(2, idPieza);
            psStock.setInt(3, cantidadUsada);
            return psStock.executeUpdate() > 0;
        }
    }

    private void registrarPiezaUsada(Connection conexion, int idMantenimiento, int idPieza, int cantidadUsada) throws SQLException {
        String sql = "INSERT INTO MantenimientoPieza (idMantenimiento, idPieza, cantidadUsada) VALUES (?, ?, ?)";
        try (PreparedStatement psUso = conexion.prepareStatement(sql)) {
            psUso.setInt(1, idMantenimiento);
            psUso.setInt(2, idPieza);
            psUso.setInt(3, cantidadUsada);
            psUso.executeUpdate();
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
            Mensajes.mostrarError("Error al modificar: " + e.toString());
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
            Mensajes.mostrarError("Error al eliminar: " + e.toString());
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
    
    /**
     * RE-1: Devuelve lista de patentes de camiones que llevan 5000+ km sin mantenimiento.
     */
    public List<String> obtenerCamionesPendientesMantenimiento() {
        List<String> pendientes = new ArrayList<>();
        String sql = "SELECT c.patente, c.kilometrajeActual, "
                   + "COALESCE(MAX(m.kilometrajeMantenimiento), 0) AS ultimoKm "
                   + "FROM Camion c "
                   + "LEFT JOIN Mantenimiento m ON c.idCamion = m.idCamion "
                   + "GROUP BY c.idCamion, c.patente, c.kilometrajeActual "
                   + "HAVING (c.kilometrajeActual - COALESCE(MAX(m.kilometrajeMantenimiento), 0)) >= 5000";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String patente = rs.getString("patente");
                int kmActual = rs.getInt("kilometrajeActual");
                int ultimoKm = rs.getInt("ultimoKm");
                pendientes.add(patente + " (" + (kmActual - ultimoKm) + " km sin mantención)");
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar mantenimientos: " + e.getMessage());
        }
        return pendientes;
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
            Mensajes.mostrarError("Error al modificar detalles: " + e.toString());
            return false;
        }
    }
}
