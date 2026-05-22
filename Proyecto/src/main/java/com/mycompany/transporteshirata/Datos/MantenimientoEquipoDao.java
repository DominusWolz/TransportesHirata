/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;
import com.mycompany.transporteshirata.Logica.EquipoOficina;
import com.mycompany.transporteshirata.Logica.MantenimientoEquipoOficina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sql = "SELECT m.*, e.nombre, e.tipo AS tipoEquipo, e.marca, e.modelo, e.identificador, e.estado "
                   + "FROM MantenimientoEquipoOficina m INNER JOIN EquipoOficina e ON m.idEquipo = e.idEquipo";

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
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(m.getFecha()));
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getDescripcion());
            ps.setString(4, m.getObservaciones());
            ps.setInt(5, m.getEquipo().getIdEquipo());

            ps.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar mantenimiento: " + ex.toString());
            return false;
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
}

