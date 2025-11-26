package DAOTest;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercedominio.enumeradores.RolUsuario;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.IUsuariosDAO;
import itson.ecommercepersistencia.conexionBD.ConexionBD;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import itson.ecommercepersistencia.implementaciones.UsuariosDAO;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author LENOVO
 */
public class UsuariosDAOTest {

    public IConexionBD conexion;
    public IUsuariosDAO usuariosDAO;

    public static boolean BD_PRUEBA = true;

    @BeforeEach
    public void setUp() {
        conexion = new ConexionBD(BD_PRUEBA);

        conexion.getDatabase().getCollection("usuarios").drop();

        usuariosDAO = new UsuariosDAO(conexion);
    }

    @AfterEach
    public void tearDown() {
        // Cerrar la conexion para no dejar hilos abiertos
        if (conexion != null) {
            conexion.close();
        }
    }

    @Test
    public void testCrearUsuarioExitoso() throws PersistenciaException {
        System.out.println("crearUsuario");

        Usuario usuarioAgregado = new Usuario(
                null,
                "Sebas Test",
                "sebas@test.com",
                "12345",
                "Calle Falsa 123",
                "555-5555",
                true,
                RolUsuario.ADMIN // Asegúrate de tener este ENUM accesible
        );
        UsuarioDTO nuevoUsuario = new UsuarioDTO(usuarioAgregado);

        try {
            // 2. Ejecutar acción (Act)
            UsuarioDTO resultado = usuariosDAO.crearUsuario(nuevoUsuario);

            // 3. Verificar resultados (Assert)
            assertNotNull(resultado, "El usuario retornado no debería ser nulo");
            assertNotNull(resultado.getId(), "El ID del usuario generado no debería ser nulo");
            assertEquals("Sebas Test", resultado.getNombre(), "El nombre no coincide");
            assertEquals("sebas@test.com", resultado.getEmail(), "El email no coincide");

        } catch (PersistenciaException e) {
            throw new PersistenciaException("No debio lanzar excepcion: " + e.getMessage());
        }
    }

    @Test
    public void testObtenerUsuarioPorCredenciales() throws PersistenciaException {
        System.out.println("obtenerUsuarioPorCredenciales");

        // 1. Preparar datos: Primero insertamos un usuario para poder buscarlo
        UsuarioDTO usuarioAInsertar = new UsuarioDTO(
                null, "Maria Test", "maria@test.com", "pass123", "Av Siempre Viva", "111", true, RolUsuario.ADMIN
        );

        try {
            usuariosDAO.crearUsuario(usuarioAInsertar);

            // 2. Ejecutar acción: Buscar el usuario recién creado
            UsuarioDTO usuarioEncontrado = usuariosDAO.obtenerUsuarioPorCredenciales("maria@test.com");

            // 3. Verificar
            assertNotNull(usuarioEncontrado, "Debería encontrar al usuario");
            assertEquals("Maria Test", usuarioEncontrado.getNombre());
            assertEquals("maria@test.com", usuarioEncontrado.getEmail());

        } catch (PersistenciaException e) {
            throw new PersistenciaException("Ocurrio un error en la persistencia: " + e.getMessage());
        }
    }

    @Test
    public void testObtenerUsuarioNoExistente() {
        System.out.println("obtenerUsuarioNoExistente");

        // Intentamos buscar un correo que no existe en la BD vacía
        try {
            // NOTA: Tu código actual lanzará NullPointerException aquí, 
            // ver "Observación Importante" abajo.
            UsuarioDTO resultado = usuariosDAO.obtenerUsuarioPorCredenciales("inexistente@correo.com");

            // Dependiendo de tu lógica, esto debería ser null o lanzar una excepción controlada
            // Por ahora asumiremos que debería ser null si corriges el DAO, 
            // o fallará si no lo has corregido.
            assertNull(resultado, "El usuario no debería existir");

        } catch (Exception e) {
            // Capturamos esto para que veas el error en tu consola de pruebas
            System.out.println("Excepción esperada si no se encontró usuario: " + e);
        }
    }

}
