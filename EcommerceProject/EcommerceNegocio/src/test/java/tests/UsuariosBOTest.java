/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tests;

import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercedominio.enumeradores.RolUsuario;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercenegocio.implementacion.UsuariosBO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.IUsuariosDAO;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.implementaciones.UsuariosDAO;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author LENOVO
 */
public class UsuariosBOTest {
    
    private IConexionBD conexion;
    private IUsuariosDAO usuariosDAO;
    private UsuariosBO usuariosBO;
    
    public UsuariosBOTest() {
    }
    
    @BeforeEach
    public void setUp(){
        // Conexion a la base de datos de prueba
        conexion = new ConexionBD(true);
        
        conexion.getDatabase().getCollection("usuarios").drop();
        
        usuariosDAO = new UsuariosDAO(conexion);
        
        usuariosBO = new UsuariosBO(usuariosDAO);
    }
    
    @AfterEach
    public void tearDown(){
        if (conexion != null){
            conexion.close();
        }
    }
    
    @Test
    public void testCrearUsuarioExitoso() throws NegocioException{
        System.out.println("TEST: Crear Usuario Exitoso");
        
        Usuario usuario = new Usuario();
        usuario.setNombre("Sebas Student");
        usuario.setEmail("sebas@escuela.com");
        usuario.setDireccion("Manzana #123");
        usuario.setTelefono("6441121212");
        usuario.setRolUsuario(RolUsuario.ADMIN);
        usuario.setContrasenia("pass123"); // Contraseña plana
        usuario.setEsActivo(true);

        try {
            // Acción: Crear usuario
            UsuarioDTO resultado = usuariosBO.crearUsuario(usuario);

            // Verificación
            assertNotNull(resultado.getId(), "El ID no debería ser nulo");
            assertEquals("Sebas Student", resultado.getNombre());
            
            // Verificamos que la contraseña NO sea "pass123" (debe estar encriptada)
            assertNotEquals("pass123", resultado.getContrasenia());
            
        } catch (NegocioException e) {
            // --- CÓDIGO DE DEBUG ---
            System.err.println("\n\n================ REPORTE DE ERROR ================");
            System.err.println("Mensaje de Negocio: " + e.getMessage());
            
            if (e.getCause() != null) {
                System.err.println("--- CAUSA REAL (Lo que falló abajo) ---");
                System.err.println("Clase del error: " + e.getCause().getClass().getName());
                System.err.println("Mensaje real: " + e.getCause().getMessage());
                e.getCause().printStackTrace(); // Esto imprimirá las líneas exactas
            } else {
                e.printStackTrace();
            }
            System.err.println("==================================================\n\n");
            // -----------------------
            
            fail("La prueba falló debido a una excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testValidacionCorreoDuplicado() throws NegocioException{
        System.out.println("TEST: Validación Correo Duplicado");
        
        Usuario u1 = new Usuario();
        u1.setNombre("Usuario Original");
        u1.setEmail("unico@test.com");
        u1.setContrasenia("123");
        u1.setEsActivo(true);
        
        Usuario u2 = new Usuario();
        u2.setNombre("Usuario Copia");
        u2.setEmail("unico@test.com"); // Mismo correo
        u2.setContrasenia("abc");

        try {
            // Guardamos el primero
            usuariosBO.crearUsuario(u1);
            
            // Intentamos guardar el segundo y esperamos la excepción
            NegocioException excepcion = assertThrows(NegocioException.class, () -> {
                usuariosBO.crearUsuario(u2);
            });
            
            assertEquals("Ya existe un usuario registrado con este correo electronico", excepcion.getMessage());

        } catch (NegocioException e) {
            throw new NegocioException("El primer usuario debio guardarse bien: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoginExitoso() {
        System.out.println("TEST: Login Exitoso");

        // 1. PREPARACIÓN: Creamos un usuario real en la BD primero
        String email = "login@test.com";
        String passwordPlana = "SuperSecreto123";
        
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Login");
        usuario.setEmail(email);
        usuario.setContrasenia(passwordPlana); // El BO se encarga de hashear esto al crear
        usuario.setEsActivo(true); // Importante: Debe estar activo

        try {
            // Guardamos el usuario usando el BO para asegurar que la contraseña se encripte correctamente
            usuariosBO.crearUsuario(usuario);

            // 2. EJECUCIÓN: Intentamos loguearnos con las credenciales
            UsuarioDTO usuarioLogueado = usuariosBO.obtenerUsuarioPorCredenciales(email, passwordPlana);

            // 3. VERIFICACIÓN
            assertNotNull(usuarioLogueado, "El usuario retornado no debería ser nulo");
            assertEquals(email, usuarioLogueado.getEmail(), "El email debería coincidir");
            assertEquals("Usuario Login", usuarioLogueado.getNombre());
            System.out.println("Login correcto para: " + usuarioLogueado.getNombre());

        } catch (NegocioException e) {
            fail("El login debió ser exitoso, pero falló: " + e.getMessage());
        }
    }
    
}
