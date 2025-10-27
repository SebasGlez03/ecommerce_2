package DAOTest;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
import ConexionDB.ConexionBD;
import DAO.UsuarioDAO;
import Docs.RolUsuario;
import Docs.Usuario;
import Interfaces.IConexionDB;
import excepciones.PersistenciaException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

/**
 *
 * @author PC
 */
public class UsuariosTest {

    public UsuariosTest() {
    }

    private IConexionDB conexionDB = new ConexionBD(true);
    private Usuario usuarioGuardado;
    private final UsuarioDAO dao = new UsuarioDAO(conexionDB);
    private ObjectId id = new ObjectId();

    @AfterEach
    public void limpiarDatos() throws PersistenciaException {
        if (usuarioGuardado != null && usuarioGuardado.getId() != null) {
            System.out.println("Eliminando usuario de prueba: " + usuarioGuardado.getId());
            eliminarUsuario(usuarioGuardado.getId());
        }
    }

    private void eliminarUsuario(Object id) throws PersistenciaException {
        try {
            MongoCollection<Usuario> coleccion = ((ConexionBD) conexionDB).getCollection("Usuarios", Usuario.class);
            coleccion.deleteOne(Filters.eq("_id", id));
        } catch (Exception ex) {
            throw new PersistenciaException("Error al eliminar usuario: " + ex.getMessage());
        }
    }

    @Test
    public void testRegistrarUsuarioCompletoOK() throws PersistenciaException {
        System.out.println("registrarUsuarioOK");
        Usuario nuevoUsuario = new Usuario(id, "Santiago",
                "santiagosanches@gmail.com",
                "12345",
                "Manzana #123",
                "6442122312",
                Boolean.TRUE,
                RolUsuario.ADMIN);
        usuarioGuardado = dao.crearUsuario(nuevoUsuario);
        assertNotNull(usuarioGuardado);
        assertEquals(nuevoUsuario.getId(), usuarioGuardado.getId());
        assertEquals(usuarioGuardado.getNombre(), usuarioGuardado.getNombre());
        assertEquals(nuevoUsuario.getEmail(), nuevoUsuario.getEmail());
        assertEquals(nuevoUsuario.getContrasenia(), nuevoUsuario.getContrasenia());
        assertEquals(nuevoUsuario.getTelefono(), nuevoUsuario.getTelefono());
        assertEquals(nuevoUsuario.getEsActivo(), nuevoUsuario.getEsActivo());
        assertEquals(nuevoUsuario.getRolUsuario(), nuevoUsuario.getRolUsuario());

    }

}
