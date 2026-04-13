/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.transporteshirata.Datos.CamionDao;
import com.mycompany.transporteshirata.Logica.Camion;
import com.mycompany.transporteshirata.Logica.Conductor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Pruebas unitarias para CamionDao
 * @author danie
 */
public class TestCamionDao {
    
    private static CamionDao camionDao;
    private Camion camionPrueba;
    
    @BeforeAll
    public static void setUpClass() {
        // Se ejecuta UNA vez antes de todas las pruebas
        camionDao = new CamionDao();
        System.out.println("🔧 Iniciando pruebas de CamionDao...");
    }
    
    @AfterAll
    public static void tearDownClass() {
        // Se ejecuta UNA vez después de todas las pruebas
        System.out.println("✅ Pruebas de CamionDao finalizadas");
        camionDao = null;
    }
    
    @BeforeEach
    public void setUp() {
        // Se ejecuta ANTES de cada prueba
        camionPrueba = new Camion();
        camionPrueba.setPatente("TEST-001");
        camionPrueba.setMarca("Scania");
        camionPrueba.setModelo("R500");
        camionPrueba.setAnio(2023);
        camionPrueba.setKilometrajeActual(0);
        
        // Crear conductor de prueba (asegúrate que exista en tu BD)
        Conductor conductor = new Conductor();
        conductor.setIdConductor(1);
        conductor.setNombre("Juan Pérez");
        camionPrueba.setConductor(conductor);
        
        System.out.println("📝 Configurando prueba...");
    }
    
    @AfterEach
    public void tearDown() {
        // Se ejecuta DESPUÉS de cada prueba
        System.out.println("🧹 Limpiando después de prueba...");
        camionPrueba = null;
    }
    
    // ==================== PRUEBAS ====================
    
    @Test
    public void testRegistrarCamion() {
        System.out.println("📝 Probando registrarCamion()");
        
        // Act - Ejecutar el método
        boolean resultado = camionDao.registrarCamion(camionPrueba);
        
        // Assert - Verificar el resultado
        assertTrue(resultado, "El camión debería registrarse correctamente");
        
        // Verificar que se guardó en la BD
        List<Camion> camiones = camionDao.listarCamiones();
        assertFalse(camiones.isEmpty(), "La lista no debería estar vacía");
        
        // Buscar el camión registrado
        boolean encontrado = false;
        for (Camion c : camiones) {
            if (c.getPatente().equals("TEST-001")) {
                encontrado = true;
                break;
            }
        }
        assertTrue(encontrado, "El camión debería estar en la lista");
    }
    
    @Test
    public void testListarCamiones() {
        System.out.println("📋 Probando listarCamiones()");
        
        // Act
        List<Camion> camiones = camionDao.listarCamiones();
        
        // Assert
        assertNotNull(camiones, "La lista no debería ser null");
        // No verificamos tamaño porque podría estar vacía o tener datos
        System.out.println("📊 Total camiones encontrados: " + camiones.size());
    }
    
    @Test
    public void testModificarCamion() {
        System.out.println("✏️ Probando modificarCamion()");
        
        // Arrange - Registrar primero
        camionDao.registrarCamion(camionPrueba);
        
        // Obtener el ID asignado
        List<Camion> camiones = camionDao.listarCamiones();
        int id = -1;
        for (Camion c : camiones) {
            if (c.getPatente().equals("TEST-001")) {
                id = c.getIdCamion();
                break;
            }
        }
        
        if (id == -1) {
            fail("No se pudo obtener el ID del camión registrado");
        }
        
        // Act - Modificar
        camionPrueba.setIdCamion(id);
        camionPrueba.setMarca("Volvo Modificado");
        camionPrueba.setModelo("FH500 Modificado");
        boolean resultado = camionDao.modificarCamion(camionPrueba);
        
        // Assert
        assertTrue(resultado, "La modificación debería ser exitosa");
        
        // Verificar cambios
        List<Camion> camionesActualizados = camionDao.listarCamiones();
        boolean modificadoCorrectamente = false;
        for (Camion c : camionesActualizados) {
            if (c.getIdCamion() == id) {
                assertEquals("Volvo Modificado", c.getMarca(), "La marca no se actualizó correctamente");
                assertEquals("FH500 Modificado", c.getModelo(), "El modelo no se actualizó correctamente");
                modificadoCorrectamente = true;
                break;
            }
        }
        assertTrue(modificadoCorrectamente, "No se encontró el camión modificado");
    }
    
    @Test
    public void testActualizarKilometraje() {
        System.out.println("🔧 Probando actualizarKilometraje()");
        
        // Arrange - Registrar primero
        camionPrueba.setKilometrajeActual(10000);
        camionDao.registrarCamion(camionPrueba);
        
        // Obtener ID
        List<Camion> camiones = camionDao.listarCamiones();
        int id = -1;
        for (Camion c : camiones) {
            if (c.getPatente().equals("TEST-001")) {
                id = c.getIdCamion();
                break;
            }
        }
        
        if (id == -1) {
            fail("No se pudo obtener el ID del camión registrado");
        }
        
        // Act - Actualizar kilometraje
        boolean resultado = camionDao.actualizarKilometraje(id, 15000);
        
        // Assert
        assertTrue(resultado, "Actualización de kilometraje debería ser exitosa");
        
        // Verificar
        List<Camion> camionesActualizados = camionDao.listarCamiones();
        boolean kilometrajeActualizado = false;
        for (Camion c : camionesActualizados) {
            if (c.getIdCamion() == id) {
                assertEquals(15000, c.getKilometrajeActual(), "El kilometraje no se actualizó correctamente");
                kilometrajeActualizado = true;
                break;
            }
        }
        assertTrue(kilometrajeActualizado, "No se encontró el camión para verificar kilometraje");
    }
    
    @Test
    public void testEliminarCamion() {
        System.out.println("🗑️ Probando eliminarCamion()");
        
        // Arrange - Registrar primero
        camionDao.registrarCamion(camionPrueba);
        
        // Obtener ID
        List<Camion> camiones = camionDao.listarCamiones();
        int id = -1;
        for (Camion c : camiones) {
            if (c.getPatente().equals("TEST-001")) {
                id = c.getIdCamion();
                break;
            }
        }
        
        if (id == -1) {
            fail("No se pudo obtener el ID del camión registrado");
        }
        
        // Act - Eliminar
        boolean resultado = camionDao.eliminarCamion(id);
        
        // Assert
        assertTrue(resultado, "Eliminación debería ser exitosa");
        
        // Verificar que ya no existe
        List<Camion> camionesRestantes = camionDao.listarCamiones();
        boolean existe = false;
        for (Camion c : camionesRestantes) {
            if (c.getIdCamion() == id) {
                existe = true;
                break;
            }
        }
        assertFalse(existe, "El camión no debería existir después de eliminar");
    }
    
    @Test
    public void testRegistrarCamionSinConductor() {
        System.out.println("👤 Probando registrarCamion sin conductor");
        
        // Arrange - Crear camión sin conductor
        Camion camionSinConductor = new Camion();
        camionSinConductor.setPatente("TEST-SIN-CONDUCTOR");
        camionSinConductor.setMarca("Mercedes");
        camionSinConductor.setModelo("Actros");
        camionSinConductor.setAnio(2024);
        camionSinConductor.setKilometrajeActual(0);
        
        Conductor conductorVacio = new Conductor();
        conductorVacio.setIdConductor(0);
        camionSinConductor.setConductor(conductorVacio);
        
        // Act
        boolean resultado = camionDao.registrarCamion(camionSinConductor);
        
        // Assert
        assertTrue(resultado, "Debería registrar camión sin conductor");
        
        // Limpiar después (opcional)
        List<Camion> camiones = camionDao.listarCamiones();
        for (Camion c : camiones) {
            if (c.getPatente().equals("TEST-SIN-CONDUCTOR")) {
                camionDao.eliminarCamion(c.getIdCamion());
                break;
            }
        }
    }
    
    @Test
    public void testRegistrarCamionPatenteDuplicada() {
        System.out.println("⚠️ Probando registrar camión con patente duplicada");
        
        // Arrange - Registrar primero
        boolean primerRegistro = camionDao.registrarCamion(camionPrueba);
        assertTrue(primerRegistro, "Primer registro debería ser exitoso");
        
        // Intentar registrar otro con misma patente
        Camion camionDuplicado = new Camion();
        camionDuplicado.setPatente("TEST-001"); // Misma patente
        camionDuplicado.setMarca("Otra Marca");
        camionDuplicado.setModelo("Otro Modelo");
        camionDuplicado.setAnio(2022);
        camionDuplicado.setKilometrajeActual(0);
        
        Conductor conductor = new Conductor();
        conductor.setIdConductor(1);
        camionDuplicado.setConductor(conductor);
        
        // Act
        boolean resultado = camionDao.registrarCamion(camionDuplicado);
        
        // Assert - Debería fallar por patente duplicada
        assertFalse(resultado, "No debería registrar camión con patente duplicada");
    }
}