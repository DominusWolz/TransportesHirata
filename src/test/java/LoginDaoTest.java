/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.transporteshirata.Datos.LoginDao;
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
 * Pruebas unitarias para LoginDao
 * @author danie
 */
public class LoginDaoTest {
    
    private static LoginDao loginDao;
    private static ConductoDao conductoDao;
    private static Conductor conductorPrueba;
    private static String rutPrueba = "99.888.777-6";
    private static String clavePrueba = "clave123";
    
    @BeforeAll
    public static void setUpClass() {
        loginDao = new LoginDao();
        conductoDao = new ConductoDao();
        System.out.println("🔧 Iniciando pruebas de LoginDao...");
        System.out.println("⚠️ ATENCIÓN: Se modificarán datos en la BD REAL");
        
        // Crear un conductor de prueba para las pruebas de login
        conductorPrueba = new Conductor();
        conductorPrueba.setRut(rutPrueba);
        conductorPrueba.setNombre("Usuario Login Test");
        conductorPrueba.setLicencia("LOGIN-001");
        conductorPrueba.setTelefono("+56900000000");
        conductorPrueba.setClave(clavePrueba);
        
        // Registrar el conductor de prueba (si no existe)
        boolean registrado = conductoDao.registrarConductor(conductorPrueba);
        if (registrado) {
            System.out.println("✅ Conductor de prueba creado: " + rutPrueba);
        } else {
            System.out.println("⚠️ El conductor de prueba ya existe o hubo error");
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
        // Limpiar: eliminar el conductor de prueba
        List<Conductor> conductores = conductoDao.listarConductores();
        for (Conductor c : conductores) {
            if (c.getRut() != null && c.getRut().equals(rutPrueba)) {
                conductoDao.eliminarConductor(c.getIdConductor());
                System.out.println("🧹 Conductor de prueba eliminado: " + rutPrueba);
                break;
            }
        }
        System.out.println("✅ Pruebas de LoginDao finalizadas");
        loginDao = null;
        conductoDao = null;
    }
    
    @BeforeEach
    public void setUp() {
        System.out.println("📝 Configurando prueba de login...");
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("🧹 Limpiando después de prueba...");
    }
    
    // ==================== PRUEBAS ====================
    
    @Test
    public void testValidarCredencialesCorrectas() {
        System.out.println("✅ Probando validarCredenciales() con credenciales CORRECTAS");
        
        // Act
        boolean resultado = loginDao.validarCredenciales(rutPrueba, clavePrueba);
        
        // Assert
        assertTrue(resultado, "Debería validar credenciales correctas");
        System.out.println("✅ Credenciales correctas validadas exitosamente");
    }
    
    @Test
    public void testValidarCredencialesRutIncorrecto() {
        System.out.println("❌ Probando validarCredenciales() con RUT incorrecto");
        
        // Act
        boolean resultado = loginDao.validarCredenciales("11.111.111-1", clavePrueba);
        
        // Assert
        assertFalse(resultado, "No debería validar con RUT incorrecto");
        System.out.println("✅ RUT incorrecto rechazado correctamente");
    }
    
    @Test
    public void testValidarCredencialesClaveIncorrecta() {
        System.out.println("❌ Probando validarCredenciales() con clave incorrecta");
        
        // Act
        boolean resultado = loginDao.validarCredenciales(rutPrueba, "clave-incorrecta");
        
        // Assert
        assertFalse(resultado, "No debería validar con clave incorrecta");
        System.out.println("✅ Clave incorrecta rechazada correctamente");
    }
    
    @Test
    public void testValidarCredencialesRutVacio() {
        System.out.println("❌ Probando validarCredenciales() con RUT vacío");
        
        // Act
        boolean resultado = loginDao.validarCredenciales("", clavePrueba);
        
        // Assert
        assertFalse(resultado, "No debería validar con RUT vacío");
        System.out.println("✅ RUT vacío rechazado correctamente");
    }
    
    @Test
    public void testValidarCredencialesClaveVacia() {
        System.out.println("❌ Probando validarCredenciales() con clave vacía");
        
        // Act
        boolean resultado = loginDao.validarCredenciales(rutPrueba, "");
        
        // Assert
        assertFalse(resultado, "No debería validar con clave vacía");
        System.out.println("✅ Clave vacía rechazada correctamente");
    }
    
    @Test
    public void testValidarCredencialesRutNull() {
        System.out.println("❌ Probando validarCredenciales() con RUT null");
        
        // Act
        boolean resultado = loginDao.validarCredenciales(null, clavePrueba);
        
        // Assert
        assertFalse(resultado, "No debería validar con RUT null");
        System.out.println("✅ RUT null rechazado correctamente");
    }
    
    @Test
    public void testValidarCredencialesClaveNull() {
        System.out.println("❌ Probando validarCredenciales() con clave null");
        
        // Act
        boolean resultado = loginDao.validarCredenciales(rutPrueba, null);
        
        // Assert
        assertFalse(resultado, "No debería validar con clave null");
        System.out.println("✅ Clave null rechazada correctamente");
    }
    
    @Test
    public void testValidarCredencialesRutNoExistente() {
        System.out.println("❌ Probando validarCredenciales() con RUT que no existe en BD");
        
        // Act
        boolean resultado = loginDao.validarCredenciales("00.000.000-0", "cualquierclave");
        
        // Assert
        assertFalse(resultado, "No debería validar con RUT que no existe");
        System.out.println("✅ RUT inexistente rechazado correctamente");
    }
    
    @Test
    public void testValidarCredencialesMayusculasMinusculas() {
        System.out.println("🔍 Probando validarCredenciales() con sensibilidad de mayúsculas");
        
        // Act - Probar con clave en mayúsculas (si la original es minúsculas)
        boolean resultado = loginDao.validarCredenciales(rutPrueba, clavePrueba.toUpperCase());
        
        // Assert - Depende de si tu sistema es sensible a mayúsculas
        // Normalmente las claves DEBEN ser sensibles a mayúsculas
        System.out.println("📝 Clave original: " + clavePrueba);
        System.out.println("📝 Clave probada: " + clavePrueba.toUpperCase());
        System.out.println("📝 Resultado: " + resultado);
        
        // Si tu sistema es sensible a mayúsculas, esto debería ser false
        // Si no es sensible, puede ser true (ajusta según tu lógica)
        assertFalse(resultado, "Las claves deberían ser sensibles a mayúsculas/minúsculas");
    }
    
    @Test
    public void testValidarCredencialesConEspacios() {
        System.out.println("🔍 Probando validarCredenciales() con espacios en blanco");
        
        // Act - Probar clave con espacios alrededor
        boolean resultado = loginDao.validarCredenciales(rutPrueba, " " + clavePrueba + " ");
        
        // Assert - Depende de si tu sistema limpia los espacios
        System.out.println("📝 Clave con espacios: ' " + clavePrueba + " '");
        System.out.println("📝 Resultado: " + resultado);
        
        // Idealmente deberías limpiar espacios antes de comparar
        // Por ahora, asumimos que no valida con espacios extras
    }
    
    @Test
    public void testValidarCredencialesMultipleIntentos() {
        System.out.println("🔍 Probando múltiples intentos de validación");
        
        // Act - Varios intentos con el mismo usuario
        boolean intento1 = loginDao.validarCredenciales(rutPrueba, clavePrueba);
        boolean intento2 = loginDao.validarCredenciales(rutPrueba, clavePrueba);
        boolean intento3 = loginDao.validarCredenciales(rutPrueba, clavePrueba);
        
        // Assert
        assertTrue(intento1, "Primer intento debería ser exitoso");
        assertTrue(intento2, "Segundo intento debería ser exitoso");
        assertTrue(intento3, "Tercer intento debería ser exitoso");
        System.out.println("✅ Múltiples intentos funcionan correctamente");
    }
    
    @Test
    public void testValidarCredencialesRutConFormatoDiferente() {
        System.out.println("🔍 Probando validarCredenciales() con RUT en diferentes formatos");
        
        // El RUT en BD está como "99.888.777-6"
        String rutSinPuntos = "998887776";
        String rutConGuion = "99.888.777-6";
        String rutSinFormato = "99888777-6";
        
        System.out.println("📝 Probando RUT sin puntos: " + rutSinPuntos);
        boolean resultado1 = loginDao.validarCredenciales(rutSinPuntos, clavePrueba);
        
        System.out.println("📝 Probando RUT con formato correcto: " + rutConGuion);
        boolean resultado2 = loginDao.validarCredenciales(rutConGuion, clavePrueba);
        
        System.out.println("📝 Resultado sin formato: " + resultado1);
        System.out.println("📝 Resultado con formato: " + resultado2);
        
        // Depende de cómo almacenes y compares los RUTs
        // Si almacenas con formato, solo funcionará el formato exacto
    }
}