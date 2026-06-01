/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;
import com.mycompany.transporteshirata.Logica.Camion;
import com.mycompany.transporteshirata.Logica.Conductor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author pccas
 */
public class CamionDao {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // listar camiones con su conductor asignado
    public List<Camion> listarCamiones() {
        List<Camion> lista = new ArrayList<>();
        String sql = "SELECT c.*, d.rut, d.nombre FROM Camion c LEFT JOIN Conductor d ON c.idConductor = d.idConductor";
        
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Camion c = new Camion();
                c.setIdCamion(rs.getInt("idCamion"));
                c.setPatente(rs.getString("patente"));
                c.setMarca(rs.getString("marca"));
                c.setModelo(rs.getString("modelo"));
                c.setAnio(rs.getInt("anio"));
                c.setKilometrajeActual(rs.getInt("kilometrajeActual"));

                Conductor d = new Conductor();
                d.setIdConductor(rs.getInt("idConductor"));
                if (rs.getString("rut") != null) {
                    d.setRut(rs.getString("rut"));
                    d.setNombre(rs.getString("nombre"));
                } else {
                    d.setNombre("Sin Conductor");
                    d.setRut("");
                }
                c.setConductor(d);
                
                lista.add(c);
            }
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al listar camiones: " + e.toString());
        }
        return lista;
    }

    
    public boolean registrarCamion(Camion c) {
        if (c.getConductor() != null && c.getConductor().getIdConductor() > 0
                && !existeConductor(c.getConductor().getIdConductor())) {
            Mensajes.mostrarError("El conductor seleccionado ya no existe. Actualice la lista y seleccione otro conductor.");
            return false;
        }

        String sql = "INSERT INTO Camion (patente, marca, modelo, anio, kilometrajeActual, idConductor) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getPatente());
            ps.setString(2, c.getMarca());
            ps.setString(3, c.getModelo());
            ps.setInt(4, c.getAnio());
            ps.setInt(5, c.getKilometrajeActual());
            
            // si el id es 0, significa que no se selecciono   conductor
            if(c.getConductor().getIdConductor() == 0) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, c.getConductor().getIdConductor());
            }
            
            ps.execute();
            return true;
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al registrar (patente duplicada?): " + e.toString());
            return false;
        }
    }

   
    public boolean modificarCamion(Camion c) {
        if (c.getConductor() != null && c.getConductor().getIdConductor() > 0
                && !existeConductor(c.getConductor().getIdConductor())) {
            Mensajes.mostrarError("El conductor seleccionado ya no existe. Actualice la lista y seleccione otro conductor.");
            return false;
        }

        String sql = "UPDATE Camion SET patente=?, marca=?, modelo=?, anio=?, idConductor=? WHERE idCamion=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getPatente());
            ps.setString(2, c.getMarca());
            ps.setString(3, c.getModelo());
            ps.setInt(4, c.getAnio());
            
            if(c.getConductor().getIdConductor() == 0) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, c.getConductor().getIdConductor());
            }
            
            ps.setInt(6, c.getIdCamion());
            ps.execute();
            return true;
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al modificar: " + e.toString());
            return false;
        }
    }

    
    public boolean actualizarKilometraje(int idCamion, int nuevoKilometraje) {
        String sql = "UPDATE Camion SET kilometrajeActual = ? WHERE idCamion = ?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, nuevoKilometraje);
            ps.setInt(2, idCamion);
            ps.execute();
            return true;
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al actualizar kilometraje: " + e.toString());
            return false;
        }
    }


    public boolean eliminarCamion(int id) {
        String sql = "DELETE FROM Camion WHERE idCamion=?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al eliminar camion: " + e.toString());
            return false;
        }
    }

    public boolean existeCamion(int idCamion) {
        String sql = "SELECT idCamion FROM Camion WHERE idCamion = ?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCamion);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al validar camion: " + e.toString());
            return false;
        }
    }

    private boolean existeConductor(int idConductor) {
        String sql = "SELECT idConductor FROM Conductor WHERE idConductor = ?";
        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idConductor);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Mensajes.mostrarError("Error al validar conductor: " + e.toString());
            return false;
        }
    }

  
}
