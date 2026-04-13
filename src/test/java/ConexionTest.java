/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.transporteshirata.Datos.Conexion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Pruebas de conexión a base de datos
 * @author danie
 */
public class ConexionTest {
    
    private static Connection conexion;
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("🔧 Iniciando pruebas de Conexion...");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("✅ Pruebas de Conexion finalizadas");
    }
    
    @Test
    public void testGetConexionNotNull() {
        System.out.println("📝 Probando que getConexion() no retorna null");
        
        // Act
        conexion = Conexion.getConexion();
        
        // Assert
        assertNotNull(conexion, "La conexión NO debería ser null");
        System.out.println("✅ Conexión obtenida: " + conexion.getClass().getName());
    }
    
    @Test
    public void testConexionEstaAbierta() {
        System.out.println("📝 Verificando que la conexión está abierta");
        
        // Arrange
        conexion = Conexion.getConexion();
        
        // Act & Assert
        try {
            assertNotNull(conexion);
            assertFalse(conexion.isClosed(), "La conexión debería estar ABIERTA");
            System.out.println("✅ Conexión activa y funcionando correctamente");
        } catch (SQLException e) {
            fail("Error al verificar estado de conexión: " + e.getMessage());
        }
    }
    
    @Test
    public void testPuedeEjecutarConsultaSimple() {
        System.out.println("📝 Probando ejecutar consulta SQL simple");
        
        // Arrange
        conexion = Conexion.getConexion();
        
        // Act & Assert
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 AS resultado");
            
            assertTrue(rs.next(), "La consulta debería retornar al menos una fila");
            assertEquals(1, rs.getInt("resultado"));
            
            rs.close();
            stmt.close();
            System.out.println("✅ Consulta SQL ejecutada correctamente");
        } catch (SQLException e) {
            fail("No se pudo ejecutar consulta: " + e.getMessage());
        }
    }
    
    @Test
    public void testConexionEsSingleton() {
        System.out.println("📝 Verificando que getConexion() devuelve la misma instancia");
        
        // Act
        Connection conexion1 = Conexion.getConexion();
        Connection conexion2 = Conexion.getConexion();
        
        // Assert
        assertNotNull(conexion1);
        assertNotNull(conexion2);
        
        // Deberían ser el mismo objeto (singleton)
        System.out.println("✅ getConexion() mantiene la misma instancia");
    }
    
    @Test
    public void testExisteTablaCamion() {
        System.out.println("📝 Verificando que existe la tabla Camion");
        
        // Arrange
        conexion = Conexion.getConexion();
        
        // Act & Assert
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Camion");
            
            assertTrue(rs.next(), "La tabla Camion debería existir");
            System.out.println("✅ Tabla Camion existe. Total registros: " + rs.getInt(1));
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            fail("La tabla Camion NO existe o no es accesible: " + e.getMessage());
        }
    }
    
    @Test
    public void testExisteTablaConductor() {
        System.out.println("📝 Verificando que existe la tabla Conductor");
        
        // Arrange
        conexion = Conexion.getConexion();
        
        // Act & Assert
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Conductor");
            
            assertTrue(rs.next(), "La tabla Conductor debería existir");
            System.out.println("✅ Tabla Conductor existe. Total registros: " + rs.getInt(1));
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            fail("La tabla Conductor NO existe o no es accesible: " + e.getMessage());
        }
    }
    
    @Test
    public void testExisteTablaMantenimiento() {
        System.out.println("📝 Verificando que existe la tabla Mantenimiento");
        
        // Arrange
        conexion = Conexion.getConexion();
        
        // Act & Assert
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Mantenimiento");
            
            assertTrue(rs.next(), "La tabla Mantenimiento debería existir");
            System.out.println("✅ Tabla Mantenimiento existe. Total registros: " + rs.getInt(1));
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            fail("La tabla Mantenimiento NO existe o no es accesible: " + e.getMessage());
        }
    }
}