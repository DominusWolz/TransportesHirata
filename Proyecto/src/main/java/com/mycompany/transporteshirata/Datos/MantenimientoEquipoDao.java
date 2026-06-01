/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;
import com.mycompany.transporteshirata.Logica.EquipoOficina;
import com.mycompany.transporteshirata.Logica.MantenimientoEquipoOficina;
import com.mycompany.transporteshirata.Logica.PiezaInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author Nicolas
 */
public class MantenimientoEquipoDao {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<MantenimientoEquipoOficina> listarMantenimientos() {
        List<MantenimientoEquipoOficina> lista = new ArrayList<>();
        String sql = "SELECT m.*, e.nombre, e.tipo AS tipoEquipo, e.marca, e.modelo, e.identificador, e.estado, "
                   + "p.idPieza, p.nombre AS nombrePieza, p.descripcion AS descripcionPieza, "
                   + "p.cantidad, p.stockMinimo, p.ubicacion, mep.cantidadUsada "
                   + "FROM MantenimientoEquipoOficina m "
                   + "INNER JOIN EquipoOficina e ON m.idEquipo = e.idEquipo "
                   + "LEFT JOIN MantenimientoEquipoPieza mep ON m.idMantenimiento = mep.idMantenimientoEquipo "
                   + "LEFT JOIN PiezaInventario p ON mep.idPieza = p.idPieza "
                   + "ORDER BY m.fecha DESC, m.idMantenimiento DESC";

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                MantenimientoEquipoOficina m = new MantenimientoEquipoOficina();
                m.setIdMantenimiento(rs.getInt("idMantenimiento"));

                java.sql.Date sqlDate = rs.getDate("fecha");
                if (sqlDate != null) {
                    m.setFecha(sqlDate.toLocalDate());
                }

                m.setTipo(rs.getString("tipo"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setObservaciones(rs.getString("observaciones"));

                EquipoOficina e = new EquipoOficina();
                e.setIdEquipo(rs.getInt("idEquipo"));
                e.setNombre(rs.getString("nombre"));
                e.setTipo(rs.getString("tipoEquipo"));
                e.setMarca(rs.getString("marca"));
                e.setModelo(rs.getString("modelo"));
                try { e.setNumeroIdentificador(rs.getInt("identificador")); } catch (Exception ex) {}
                e.setEstado(rs.getString("estado"));

                m.setEquipo(e);
                cargarPiezaUsada(rs, m);

                lista.add(m);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar mantenimientos: " + ex.toString());
        }
        return lista;
    }
    
    public List<MantenimientoEquipoOficina> listarTodos() {
        return listarMantenimientos();
    }
    
    
    public boolean registrarMantenimiento(MantenimientoEquipoOficina m) {
        String sql = "INSERT INTO MantenimientoEquipoOficina (fecha, tipo, descripcion, observaciones, idEquipo) VALUES (?, ?, ?, ?, ?)";
        Connection conexion = null;
        try {
            conexion = Conexion.getConexion();
            if (conexion == null) {
                return false;
            }
            conexion.setAutoCommit(false);

            if (usaPieza(m) && !descontarStock(conexion, m.getPiezaUsada().getIdPieza(), m.getCantidadPiezaUsada())) {
                conexion.rollback();
                JOptionPane.showMessageDialog(null, "No hay stock suficiente para registrar la pieza usada.");
                return false;
            }

            int idMantenimiento = insertarMantenimiento(conexion, sql, m);
            if (usaPieza(m)) {
                registrarPiezaUsada(conexion, idMantenimiento, m.getPiezaUsada().getIdPieza(), m.getCantidadPiezaUsada());
            }

            conexion.commit();
            return true;
        } catch (SQLException ex) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException rollbackError) {
                    JOptionPane.showMessageDialog(null, "Error al revertir mantenimiento: " + rollbackError.toString());
                }
            }
            JOptionPane.showMessageDialog(null, "Error al registrar mantenimiento: " + ex.toString());
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + ex.toString());
                }
            }
        }
    }
    public boolean modificarMantenimiento(MantenimientoEquipoOficina m) {
    String sql = "UPDATE MantenimientoEquipoOficina SET fecha=?, tipo=?, descripcion=?, observaciones=?, idEquipo=? WHERE idMantenimiento=?";
    try {
        con = Conexion.getConexion();
        ps = con.prepareStatement(sql);
        ps.setDate(1, java.sql.Date.valueOf(m.getFecha()));
        ps.setString(2, m.getTipo());
        ps.setString(3, m.getDescripcion());
        ps.setString(4, m.getObservaciones());
        ps.setInt(5, m.getEquipo().getIdEquipo());
        ps.setInt(6, m.getIdMantenimiento());
        ps.execute();
        return true;
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al modificar mantenimiento: " + ex.toString());
        return false;
    }
    }
    public boolean eliminarMantenimiento(int idMantenimiento) {
    String sql = "DELETE FROM MantenimientoEquipoOficina WHERE idMantenimiento=?";
    try {
        con = Conexion.getConexion();
        ps = con.prepareStatement(sql);
        ps.setInt(1, idMantenimiento);
        int filas = ps.executeUpdate();
        return filas > 0;
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al eliminar mantenimiento: " + ex.toString());
        return false;
    }
}
    public MantenimientoEquipoOficina buscarPorId(int idMantenimiento) {
    String sql = "SELECT m.*, e.nombre, e.tipo AS tipoEquipo, e.marca, e.modelo, e.identificador, e.estado, "
               + "p.idPieza, p.nombre AS nombrePieza, p.descripcion AS descripcionPieza, "
               + "p.cantidad, p.stockMinimo, p.ubicacion, mep.cantidadUsada "
               + "FROM MantenimientoEquipoOficina m INNER JOIN EquipoOficina e ON m.idEquipo = e.idEquipo "
               + "LEFT JOIN MantenimientoEquipoPieza mep ON m.idMantenimiento = mep.idMantenimientoEquipo "
               + "LEFT JOIN PiezaInventario p ON mep.idPieza = p.idPieza "
               + "WHERE m.idMantenimiento = ?";
    try {
        con = Conexion.getConexion();
        ps = con.prepareStatement(sql);
        ps.setInt(1, idMantenimiento);
        rs = ps.executeQuery();

        if (rs.next()) {
            MantenimientoEquipoOficina m = new MantenimientoEquipoOficina();
            m.setIdMantenimiento(rs.getInt("idMantenimiento"));
            java.sql.Date sqlDate = rs.getDate("fecha");
            if (sqlDate != null) {
                m.setFecha(sqlDate.toLocalDate());
            }
            m.setTipo(rs.getString("tipo"));
            m.setDescripcion(rs.getString("descripcion"));
            m.setObservaciones(rs.getString("observaciones"));

            EquipoOficina e = new EquipoOficina();
            e.setIdEquipo(rs.getInt("idEquipo"));
            e.setNombre(rs.getString("nombre"));
            e.setTipo(rs.getString("tipoEquipo"));
            e.setMarca(rs.getString("marca"));
            e.setModelo(rs.getString("modelo"));
            try { e.setNumeroIdentificador(rs.getInt("identificador")); } catch (Exception ex) {}
            e.setEstado(rs.getString("estado"));

            m.setEquipo(e);
            cargarPiezaUsada(rs, m);
            return m;
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al buscar mantenimiento: " + ex.toString());
    }
    return null;
}

    private int insertarMantenimiento(Connection conexion, String sql, MantenimientoEquipoOficina m) throws SQLException {
        try (PreparedStatement psInsert = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            psInsert.setDate(1, java.sql.Date.valueOf(m.getFecha()));
            psInsert.setString(2, m.getTipo());
            psInsert.setString(3, m.getDescripcion());
            psInsert.setString(4, m.getObservaciones());
            psInsert.setInt(5, m.getEquipo().getIdEquipo());
            psInsert.executeUpdate();

            try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el ID del mantenimiento registrado.");
    }

    private boolean usaPieza(MantenimientoEquipoOficina m) {
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
        String sql = "INSERT INTO MantenimientoEquipoPieza (idMantenimientoEquipo, idPieza, cantidadUsada) VALUES (?, ?, ?)";
        try (PreparedStatement psUso = conexion.prepareStatement(sql)) {
            psUso.setInt(1, idMantenimiento);
            psUso.setInt(2, idPieza);
            psUso.setInt(3, cantidadUsada);
            psUso.executeUpdate();
        }
    }

    private void cargarPiezaUsada(ResultSet rs, MantenimientoEquipoOficina m) throws SQLException {
        if (rs.getObject("idPieza") == null) {
            return;
        }

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
    
}

