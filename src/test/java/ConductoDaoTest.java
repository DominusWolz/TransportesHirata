/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.transporteshirata.Datos.ConductoDao;
import com.mycompany.transporteshirata.Logica.Conductor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Pruebas unitarias para ConductoDao
 * @author danie
 */
public class ConductoDaoTest {
    
    private static ConductoDao conductoDao;
    private Conductor conductorPrueba;
    
    @BeforeAll
    public static void setUpClass() {
        conductoDao = new ConductoDao();
        System.out.println("🔧 Iniciando pruebas de ConductoDao...");
        System.out.println("⚠️ ATENCIÓN: Se modificarán datos en la BD REAL");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("✅ Pruebas de ConductoDao finalizadas");
        conductoDao = null;
    }
    
    @BeforeEach
    public void setUp() {
        conductorPrueba = new Conductor();
        conductorPrueba.setRut("99.999.999-9");
        conductorPrueba.setNombre("Conductor Test JUnit");
        conductorPrueba.setLicencia("TEST-12345");
        conductorPrueba.setTelefono("+56999999999");
        conductorPrueba.setClave("test123");
        
        System.out.println("📝 Configurando prueba con RUT: 99.999.999-9");
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("🧹 Limpiando después de prueba...");
        conductorPrueba = null;
    }
    
    // ==================== PRUEBAS ====================
    // Prueba para listar conductores
    @Test
    public void testListarConductores() {
        System.out.println("📋 Probando listarConductores()");
        
        // Act
        List<Conductor> conductores = conductoDao.listarConductores();
        
        // Assert
        assertNotNull(conductores, "La lista no debería ser null");
        System.out.println("📊 Total conductores encontrados: " + conductores.size());
        
        // Mostrar algunos conductores (opcional)
        for (Conductor c : conductores) {
            System.out.println("   - " + c.getNombre() + " (" + c.getRut() + ")");
        }
    }
    
    @Test
    public void testRegistrarConductor() {
        System.out.println("📝 Probando registrarConductor()");
        
        // Act - Registrar conductor de prueba
        boolean resultado = conductoDao.registrarConductor(conductorPrueba);
        
        // Assert
        assertTrue(resultado, "El conductor debería registrarse correctamente");
        
        // Verificar que existe en la BD
        List<Conductor> conductores = conductoDao.listarConductores();
        boolean encontrado = false;
        for (Conductor c : conductores) {
            if (c.getRut() != null && c.getRut().equals("99.999.999-9")) {
                encontrado = true;
                assertEquals("Conductor Test JUnit", c.getNombre());
                break;
            }
        }
        assertTrue(encontrado, "El conductor debería estar en la lista después de registrar");
        
        // LIMPIAR: Eliminar el conductor de prueba
        limpiarConductorPrueba();
    }
    
    @Test
    public void testRegistrarConductorRutDuplicado() {
        System.out.println("⚠️ Probando registrar conductor con RUT duplicado");
        
        // Arrange - Registrar primero
        conductoDao.registrarConductor(conductorPrueba);
        
        // Intentar registrar otro con mismo RUT
        Conductor conductorDuplicado = new Conductor();
        conductorDuplicado.setRut("99.999.999-9"); // Mismo RUT
        conductorDuplicado.setNombre("Otro Conductor");
        conductorDuplicado.setLicencia("OTRO-123");
        conductorDuplicado.setTelefono("+56988888888");
        conductorDuplicado.setClave("otro123");
        
        // Act
        boolean resultado = conductoDao.registrarConductor(conductorDuplicado);
        
        // Assert - Debería fallar por RUT duplicado (depende de tu BD)
        // Si tu BD tiene UNIQUE en RUT, esto devuelve false
        assertFalse(resultado, "No debería registrar conductor con RUT duplicado");
        
        // LIMPIAR
        limpiarConductorPrueba();
    }
    
    @Test
    public void testModificarConductor() {
        System.out.println("✏️ Probando modificarConductor()");
        
        // Arrange - Registrar primero
        conductoDao.registrarConductor(conductorPrueba);
        
        // Obtener el ID asignado
        int id = obtenerIdConductorPrueba();
        if (id == -1) {
            fail("No se pudo obtener el ID del conductor registrado");
        }
        
        // Act - Modificar
        conductorPrueba.setIdConductor(id);
        conductorPrueba.setNombre("Conductor Modificado JUnit");
        conductorPrueba.setTelefono("+56977777777");
        boolean resultado = conductoDao.modificarConductor(conductorPrueba);
        
        // Assert
        assertTrue(resultado, "La modificación debería ser exitosa");
        
        // Verificar cambios
        List<Conductor> conductores = conductoDao.listarConductores();
        boolean modificadoCorrectamente = false;
        for (Conductor c : conductores) {
            if (c.getIdConductor() == id) {
                assertEquals("Conductor Modificado JUnit", c.getNombre());
                assertEquals("+56977777777", c.getTelefono());
                modificadoCorrectamente = true;
                break;
            }
        }
        assertTrue(modificadoCorrectamente, "El conductor no se modificó correctamente");
        
        // LIMPIAR
        limpiarConductorPrueba();
    }
    
    @Test
    public void testEliminarConductor() {
        System.out.println("🗑️ Probando eliminarConductor()");
        
        // Arrange - Registrar primero
        conductoDao.registrarConductor(conductorPrueba);
        
        // Obtener ID
        int id = obtenerIdConductorPrueba();
        if (id == -1) {
            fail("No se pudo obtener el ID del conductor registrado");
        }
        
        // Act - Eliminar
        boolean resultado = conductoDao.eliminarConductor(id);
        
        // Assert
        assertTrue(resultado, "Eliminación debería ser exitosa");
        
        // Verificar que ya no existe
        List<Conductor> conductores = conductoDao.listarConductores();
        boolean existe = false;
        for (Conductor c : conductores) {
            if (c.getIdConductor() == id) {
                existe = true;
                break;
            }
        }
        assertFalse(existe, "El conductor no debería existir después de eliminar");
    }
    
    @Test
    public void testListarConductoresCbm() {
        System.out.println("📋 Probando listarConductoresCbm()");
        
        // Act
        List<Conductor> conductores = conductoDao.listarConductoresCbm();
        
        // Assert
        assertNotNull(conductores, "La lista no debería ser null");
        System.out.println("📊 Total conductores para CBM: " + conductores.size());
        
        // Verificar que cada conductor tiene al menos RUT y nombre
        for (Conductor c : conductores) {
            assertNotNull(c.getRut(), "El RUT no debería ser null");
            assertNotNull(c.getNombre(), "El nombre no debería ser null");
        }
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    /**
     * Obtiene el ID del conductor de prueba (RUT: 99.999.999-9)
     */
    private int obtenerIdConductorPrueba() {
        List<Conductor> conductores = conductoDao.listarConductores();
        for (Conductor c : conductores) {
            if (c.getRut() != null && c.getRut().equals("99.999.999-9")) {
                return c.getIdConductor();
            }
        }
        return -1;
    }
    
    /**
     * Limpia el conductor de prueba de la BD
     */
    private void limpiarConductorPrueba() {
        int id = obtenerIdConductorPrueba();
        if (id != -1) {
            conductoDao.eliminarConductor(id);
            System.out.println("🧹 Conductor de prueba eliminado (ID: " + id + ")");
        }
    }
}