/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.transporteshirata.Datos.MantenimientoDao;
import com.mycompany.transporteshirata.Datos.CamionDao;
import com.mycompany.transporteshirata.Logica.Mantenimiento;
import com.mycompany.transporteshirata.Logica.Camion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.time.LocalDate;

/**
 * Pruebas unitarias para MantenimientoDao
 * @author danie
 */
public class MantenimientoDaoTest {
    
    private static MantenimientoDao mantenimientoDao;
    private static CamionDao camionDao;
    private Mantenimiento mantenimientoPrueba;
    private static Camion camionExistente; // Usar un camión que YA existe en BD
    private static int idCamionExistente = 1; // CAMBIA ESTE VALOR según tu BD
    
    @BeforeAll
    public static void setUpClass() {
        mantenimientoDao = new MantenimientoDao();
        camionDao = new CamionDao();
        System.out.println("🔧 Iniciando pruebas de MantenimientoDao...");
        System.out.println("⚠️ ATENCIÓN: Se modificarán datos en la BD REAL");
        
        // Verificar que existe un camión para asociar mantenimientos
        List<Camion> camiones = camionDao.listarCamiones();
        if (!camiones.isEmpty()) {
            camionExistente = camiones.get(0);
            idCamionExistente = camionExistente.getIdCamion();
            System.out.println("📌 Usando camión existente ID: " + idCamionExistente + 
                             " - Patente: " + camionExistente.getPatente());
        } else {
            System.out.println("❌ No hay camiones en la BD. Las pruebas pueden fallar.");
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("✅ Pruebas de MantenimientoDao finalizadas");
        mantenimientoDao = null;
        camionDao = null;
    }
    
    @BeforeEach
    public void setUp() {
        mantenimientoPrueba = new Mantenimiento();
        mantenimientoPrueba.setFecha(LocalDate.now());
        mantenimientoPrueba.setTipo("PREVENTIVO");
        mantenimientoPrueba.setDescripcion("Mantenimiento de prueba JUnit");
        mantenimientoPrueba.setKilometrajeMantenimiento(10000);
        
        // Asociar a un camión existente
        Camion camion = new Camion();
        camion.setIdCamion(idCamionExistente);
        mantenimientoPrueba.setCamion(camion);
        
        System.out.println("📝 Configurando prueba de mantenimiento para camión ID: " + idCamionExistente);
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("🧹 Limpiando después de prueba...");
        mantenimientoPrueba = null;
    }
    
    // ==================== PRUEBAS ====================
    
    @Test
    public void testListarMantenimientos() {
        System.out.println("📋 Probando listarMantenimientos()");
        
        // Act
        List<Mantenimiento> mantenimientos = mantenimientoDao.listarMantenimientos();
        
        // Assert
        assertNotNull(mantenimientos, "La lista no debería ser null");
        System.out.println("📊 Total mantenimientos encontrados: " + mantenimientos.size());
        
        // Mostrar algunos mantenimientos (opcional)
        for (Mantenimiento m : mantenimientos) {
            System.out.println("   - ID: " + m.getIdMantenimiento() + 
                             " | Tipo: " + m.getTipo() + 
                             " | Camión: " + m.getCamion().getPatente());
        }
    }
    
    @Test
    public void testRegistrarMantenimiento() {
        System.out.println("📝 Probando registrarMantenimiento()");
        
        // Act
        boolean resultado = mantenimientoDao.registrarMantenimiento(mantenimientoPrueba);
        
        // Assert
        assertTrue(resultado, "El mantenimiento debería registrarse correctamente");
        
        // Verificar que existe en la BD
        List<Mantenimiento> mantenimientos = mantenimientoDao.listarMantenimientos();
        boolean encontrado = false;
        for (Mantenimiento m : mantenimientos) {
            if (m.getDescripcion() != null && m.getDescripcion().equals("Mantenimiento de prueba JUnit")) {
                encontrado = true;
                assertEquals("PREVENTIVO", m.getTipo());
                assertEquals(10000, m.getKilometrajeMantenimiento());
                // Guardar ID para limpiar después
                mantenimientoPrueba.setIdMantenimiento(m.getIdMantenimiento());
                break;
            }
        }
        assertTrue(encontrado, "El mantenimiento debería estar en la lista después de registrar");
        
        // LIMPIAR: Eliminar el mantenimiento de prueba
        limpiarMantenimientoPrueba();
    }
    
    @Test
    public void testModificarMantenimiento() {
        System.out.println("✏️ Probando modificarMantenimiento()");
        
        // Arrange - Registrar primero
        mantenimientoDao.registrarMantenimiento(mantenimientoPrueba);
        
        // Obtener el ID asignado
        int id = obtenerIdMantenimientoPrueba();
        if (id == -1) {
            fail("No se pudo obtener el ID del mantenimiento registrado");
        }
        
        // Act - Modificar
        mantenimientoPrueba.setIdMantenimiento(id);
        mantenimientoPrueba.setTipo("CORRECTIVO");
        mantenimientoPrueba.setDescripcion("Mantenimiento modificado JUnit");
        mantenimientoPrueba.setKilometrajeMantenimiento(20000);
        boolean resultado = mantenimientoDao.modificarMantenimiento(mantenimientoPrueba);
        
        // Assert
        assertTrue(resultado, "La modificación debería ser exitosa");
        
        // Verificar cambios
        List<Mantenimiento> mantenimientos = mantenimientoDao.listarMantenimientos();
        boolean modificadoCorrectamente = false;
        for (Mantenimiento m : mantenimientos) {
            if (m.getIdMantenimiento() == id) {
                assertEquals("CORRECTIVO", m.getTipo());
                assertEquals("Mantenimiento modificado JUnit", m.getDescripcion());
                assertEquals(20000, m.getKilometrajeMantenimiento());
                modificadoCorrectamente = true;
                break;
            }
        }
        assertTrue(modificadoCorrectamente, "El mantenimiento no se modificó correctamente");
        
        // LIMPIAR
        limpiarMantenimientoPrueba();
    }
    
    @Test
    public void testEliminarMantenimiento() {
        System.out.println("🗑️ Probando eliminarMantenimiento()");
        
        // Arrange - Registrar primero
        mantenimientoDao.registrarMantenimiento(mantenimientoPrueba);
        
        // Obtener ID
        int id = obtenerIdMantenimientoPrueba();
        if (id == -1) {
            fail("No se pudo obtener el ID del mantenimiento registrado");
        }
        
        // Act - Eliminar
        boolean resultado = mantenimientoDao.eliminarMantenimiento(id);
        
        // Assert
        assertTrue(resultado, "Eliminación debería ser exitosa");
        
        // Verificar que ya no existe
        List<Mantenimiento> mantenimientos = mantenimientoDao.listarMantenimientos();
        boolean existe = false;
        for (Mantenimiento m : mantenimientos) {
            if (m.getIdMantenimiento() == id) {
                existe = true;
                break;
            }
        }
        assertFalse(existe, "El mantenimiento no debería existir después de eliminar");
    }
    
    @Test
    public void testObtenerUltimoKilometrajeMantenimiento() {
        System.out.println("🔧 Probando obtenerUltimoKilometrajeMantenimiento()");
        
        // Arrange - Registrar dos mantenimientos con diferentes kilometrajes
        Mantenimiento m1 = new Mantenimiento();
        m1.setFecha(LocalDate.now());
        m1.setTipo("PREVENTIVO");
        m1.setDescripcion("Primer mantenimiento");
        m1.setKilometrajeMantenimiento(5000);
        Camion camion = new Camion();
        camion.setIdCamion(idCamionExistente);
        m1.setCamion(camion);
        mantenimientoDao.registrarMantenimiento(m1);
        
        Mantenimiento m2 = new Mantenimiento();
        m2.setFecha(LocalDate.now());
        m2.setTipo("PREVENTIVO");
        m2.setDescripcion("Segundo mantenimiento");
        m2.setKilometrajeMantenimiento(15000);
        m2.setCamion(camion);
        mantenimientoDao.registrarMantenimiento(m2);
        
        // Act
        int ultimoKm = mantenimientoDao.obtenerUltimoKilometrajeMantenimiento(idCamionExistente);
        
        // Assert
        // El último debería ser el mayor (15000)
        assertTrue(ultimoKm >= 15000, "El último kilometraje debería ser al menos 15000");
        System.out.println("📊 Último kilometraje para camión ID " + idCamionExistente + ": " + ultimoKm);
        
        // LIMPIAR: Eliminar los mantenimientos de prueba
        limpiarMantenimientoPorDescripcion("Primer mantenimiento");
        limpiarMantenimientoPorDescripcion("Segundo mantenimiento");
    }
    
    @Test
    public void testModificarDetallesMantenimiento() {
        System.out.println("🔧 Probando modificarDetallesMantenimiento()");
        
        // Arrange - Registrar primero
        mantenimientoDao.registrarMantenimiento(mantenimientoPrueba);
        
        // Obtener el ID asignado
        int id = obtenerIdMantenimientoPrueba();
        if (id == -1) {
            fail("No se pudo obtener el ID del mantenimiento registrado");
        }
        
        // Act - Modificar solo detalles (sin cambiar el camión)
        mantenimientoPrueba.setIdMantenimiento(id);
        mantenimientoPrueba.setTipo("PREVENTIVO MODIFICADO");
        mantenimientoPrueba.setDescripcion("Detalles modificados");
        mantenimientoPrueba.setKilometrajeMantenimiento(25000);
        boolean resultado = mantenimientoDao.modificarDetallesMantenimiento(mantenimientoPrueba);
        
        // Assert
        assertTrue(resultado, "La modificación de detalles debería ser exitosa");
        
        // Verificar cambios
        List<Mantenimiento> mantenimientos = mantenimientoDao.listarMantenimientos();
        boolean modificadoCorrectamente = false;
        for (Mantenimiento m : mantenimientos) {
            if (m.getIdMantenimiento() == id) {
                assertEquals("PREVENTIVO MODIFICADO", m.getTipo());
                assertEquals("Detalles modificados", m.getDescripcion());
                assertEquals(25000, m.getKilometrajeMantenimiento());
                modificadoCorrectamente = true;
                break;
            }
        }
        assertTrue(modificadoCorrectamente, "Los detalles del mantenimiento no se modificaron correctamente");
        
        // LIMPIAR
        limpiarMantenimientoPrueba();
    }
    
    @Test
    public void testRegistrarMantenimientoSinCamion() {
        System.out.println("⚠️ Probando registrar mantenimiento sin camión");
        
        // Arrange - Crear mantenimiento sin camión
        Mantenimiento m = new Mantenimiento();
        m.setFecha(LocalDate.now());
        m.setTipo("PREVENTIVO");
        m.setDescripcion("Mantenimiento sin camión");
        m.setKilometrajeMantenimiento(5000);
        m.setCamion(null); // Sin camión
        
        // Act & Assert - Debería lanzar excepción o fallar
        try {
            boolean resultado = mantenimientoDao.registrarMantenimiento(m);
            // Si llegó aquí, verificamos que sea false
            assertFalse(resultado, "No debería registrar mantenimiento sin camión");
        } catch (NullPointerException e) {
            // También es válido que lance excepción
            System.out.println("✅ Se lanzó excepción por falta de camión (comportamiento esperado)");
        }
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    /**
     * Obtiene el ID del mantenimiento de prueba
     */
    private int obtenerIdMantenimientoPrueba() {
        List<Mantenimiento> mantenimientos = mantenimientoDao.listarMantenimientos();
        for (Mantenimiento m : mantenimientos) {
            if (m.getDescripcion() != null && m.getDescripcion().equals("Mantenimiento de prueba JUnit")) {
                return m.getIdMantenimiento();
            }
        }
        return -1;
    }
    
    /**
     * Limpia el mantenimiento de prueba de la BD
     */
    private void limpiarMantenimientoPrueba() {
        int id = obtenerIdMantenimientoPrueba();
        if (id != -1) {
            mantenimientoDao.eliminarMantenimiento(id);
            System.out.println("🧹 Mantenimiento de prueba eliminado (ID: " + id + ")");
        }
    }
    
    /**
     * Limpia mantenimientos por descripción
     */
    private void limpiarMantenimientoPorDescripcion(String descripcion) {
        List<Mantenimiento> mantenimientos = mantenimientoDao.listarMantenimientos();
        for (Mantenimiento m : mantenimientos) {
            if (m.getDescripcion() != null && m.getDescripcion().equals(descripcion)) {
                mantenimientoDao.eliminarMantenimiento(m.getIdMantenimiento());
                System.out.println("🧹 Mantenimiento eliminado: " + descripcion);
                break;
            }
        }
    }
}