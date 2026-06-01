/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author pccas
 */
public class Conexion {

    private static final String CONFIG_LOCAL = "hirata-db.properties";
    private static final String URL_DEFECTO = "jdbc:mysql://localhost:3306/Transportes_Hirata";
    private static final String USUARIO_DEFECTO = "root";
    private static final String PASSWORD_DEFECTO = "";
    private static boolean esquemaVerificado = false;

    public static Connection getConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties config = cargarConfig();
            String url = obtenerConfig(config, "db.url", "HIRATA_DB_URL", URL_DEFECTO);
            String usuario = obtenerConfig(config, "db.user", "HIRATA_DB_USER", USUARIO_DEFECTO);
            String password = obtenerConfig(config, "db.password", "HIRATA_DB_PASSWORD", PASSWORD_DEFECTO);
            asegurarBaseDeDatos(url, usuario, password);
            Connection con = DriverManager.getConnection(url, usuario, password);
            asegurarTablas(con);
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            Mensajes.mostrarError("Error de Conexion a BD Hirata: " + e.getMessage());
            return null;
        }
    }

    private static Properties cargarConfig() {
        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_LOCAL)) {
            config.load(fis);
        } catch (IOException e) {
            // Si no existe el archivo local, se usan variables de entorno o valores por defecto.
        }
        return config;
    }

    private static String obtenerConfig(Properties config, String claveArchivo, String claveEntorno, String valorDefecto) {
        String valorArchivo = config.getProperty(claveArchivo);
        if (valorArchivo != null && !valorArchivo.trim().isEmpty()) {
            return valorArchivo.trim();
        }

        String valorEntorno = System.getenv(claveEntorno);
        if (valorEntorno != null && !valorEntorno.trim().isEmpty()) {
            return valorEntorno.trim();
        }

        return valorDefecto;
    }

    private static void asegurarBaseDeDatos(String url, String usuario, String password) throws SQLException {
        String nombreBaseDatos = obtenerNombreBaseDatos(url);
        if (nombreBaseDatos.isEmpty()) {
            return;
        }

        String urlServidor = obtenerUrlServidor(url);
        try (Connection con = DriverManager.getConnection(urlServidor, usuario, password);
             PreparedStatement ps = con.prepareStatement("CREATE DATABASE IF NOT EXISTS `" + nombreBaseDatos + "`")) {
            ps.executeUpdate();
        }
    }

    private static void asegurarTablas(Connection con) throws SQLException {
        if (esquemaVerificado) {
            return;
        }

        try (Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Conductor ("
                    + "idConductor INT AUTO_INCREMENT PRIMARY KEY,"
                    + "rut VARCHAR(20) NOT NULL UNIQUE,"
                    + "nombre VARCHAR(100) NOT NULL,"
                    + "licencia VARCHAR(50) NOT NULL,"
                    + "telefono VARCHAR(20) NOT NULL,"
                    + "clave VARCHAR(100) NOT NULL"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS Camion ("
                    + "idCamion INT AUTO_INCREMENT PRIMARY KEY,"
                    + "patente VARCHAR(20) NOT NULL UNIQUE,"
                    + "marca VARCHAR(100) NOT NULL,"
                    + "modelo VARCHAR(100) NOT NULL,"
                    + "anio INT NOT NULL,"
                    + "kilometrajeActual INT NOT NULL DEFAULT 0,"
                    + "idConductor INT NULL,"
                    + "CONSTRAINT fk_camion_conductor FOREIGN KEY (idConductor) REFERENCES Conductor(idConductor)"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS Mantenimiento ("
                    + "idMantenimiento INT AUTO_INCREMENT PRIMARY KEY,"
                    + "fecha DATE NOT NULL,"
                    + "tipo VARCHAR(50) NOT NULL,"
                    + "descripcion VARCHAR(255) NOT NULL,"
                    + "kilometrajeMantenimiento INT NOT NULL,"
                    + "idCamion INT NOT NULL,"
                    + "CONSTRAINT fk_mantenimiento_camion FOREIGN KEY (idCamion) REFERENCES Camion(idCamion)"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS PiezaInventario ("
                    + "idPieza INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nombre VARCHAR(100) NOT NULL UNIQUE,"
                    + "descripcion VARCHAR(255),"
                    + "cantidad INT NOT NULL DEFAULT 0,"
                    + "stockMinimo INT NOT NULL DEFAULT 0,"
                    + "ubicacion VARCHAR(100)"
                    + ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS MantenimientoPieza ("
                    + "idMantenimientoPieza INT AUTO_INCREMENT PRIMARY KEY,"
                    + "idMantenimiento INT NOT NULL,"
                    + "idPieza INT NOT NULL,"
                    + "cantidadUsada INT NOT NULL,"
                    + "CONSTRAINT fk_mp_mantenimiento FOREIGN KEY (idMantenimiento) REFERENCES Mantenimiento(idMantenimiento) ON DELETE CASCADE,"
                    + "CONSTRAINT fk_mp_pieza FOREIGN KEY (idPieza) REFERENCES PiezaInventario(idPieza)"
                    + ")");

            st.executeUpdate("INSERT IGNORE INTO Conductor (idConductor, rut, nombre, licencia, telefono, clave) "
                    + "VALUES (1, '10.000.000-8', 'Conductor Base', 'BASE-1', '99999999', 'base123')");
        }

        esquemaVerificado = true;
    }

    private static String obtenerNombreBaseDatos(String url) {
        String sinParametros = url.split("\\?", 2)[0];
        int ultimoSlash = sinParametros.lastIndexOf('/');
        if (ultimoSlash == -1 || ultimoSlash == sinParametros.length() - 1) {
            return "";
        }
        return sinParametros.substring(ultimoSlash + 1);
    }

    private static String obtenerUrlServidor(String url) {
        String[] partes = url.split("\\?", 2);
        String sinParametros = partes[0];
        int ultimoSlash = sinParametros.lastIndexOf('/');
        String urlServidor = ultimoSlash == -1 ? sinParametros : sinParametros.substring(0, ultimoSlash + 1);
        return partes.length == 2 ? urlServidor + "?" + partes[1] : urlServidor;
    }
}
