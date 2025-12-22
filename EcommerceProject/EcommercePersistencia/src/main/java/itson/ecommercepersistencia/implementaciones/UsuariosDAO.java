/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercepersistencia.implementaciones;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercepersistencia.IUsuariosDAO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author LENOVO
 */
public class UsuariosDAO implements IUsuariosDAO {

    private final MongoCollection<Usuario> coleccion;

    public UsuariosDAO(IConexionBD conexion) {
        this.coleccion = conexion.getCollection("usuarios", Usuario.class);
    }

    @Override
    public UsuarioDTO crearUsuario(UsuarioDTO nuevoUsuario) throws PersistenciaException {

        Usuario usuario = new Usuario();
        usuario.setId(new ObjectId());
        usuario.setNombre(nuevoUsuario.getNombre());
        usuario.setEmail(nuevoUsuario.getEmail());
        usuario.setContrasenia(nuevoUsuario.getContrasenia());
        usuario.setDireccion(nuevoUsuario.getDireccion());
        usuario.setTelefono(nuevoUsuario.getTelefono());
        usuario.setEsActivo(nuevoUsuario.getEsActivo());
        usuario.setRolUsuario(nuevoUsuario.getRolUsuario());

        try {
            coleccion.insertOne(usuario);

            return new UsuarioDTO(usuario);
        } catch (Exception e) {
            throw new PersistenciaException("Error al crear el usuario en la base de datos", e);
        }
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorCredenciales(String correoElectronico) throws PersistenciaException {
        try {
            Bson filtro = Filters.regex("email", "^" + correoElectronico + "$", "i");
            Usuario documentoUsuario = coleccion.find(filtro).first();

            if (documentoUsuario == null) {
                return null; // Si no existe, regresamos null y no pasa nada malo
            }

            UsuarioDTO usuarioObtenido = new UsuarioDTO(documentoUsuario);

            return usuarioObtenido;
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener el usuario por credenciales", e);
        }
    }

    @Override
    public UsuarioDTO actualizarUsuario(UsuarioDTO usuario) throws PersistenciaException {
        try {
            List<Bson> actualizaciones = new ArrayList<>();

            // Agregamos los campos a actualizar
            actualizaciones.add(Updates.set("nombre", usuario.getNombre()));
            actualizaciones.add(Updates.set("email", usuario.getEmail()));
            actualizaciones.add(Updates.set("direccion", usuario.getDireccion()));
            actualizaciones.add(Updates.set("telefono", usuario.getTelefono()));

            // Solo actualizamos la contraseña si NO viene vacía (si el usuario la cambió)
            if (usuario.getContrasenia() != null && !usuario.getContrasenia().isEmpty()) {
                actualizaciones.add(Updates.set("contrasenia", usuario.getContrasenia()));
            }

            Bson filtro = Filters.eq("_id", usuario.getId());
            Bson updates = Updates.combine(actualizaciones);

            UpdateResult resultado = coleccion.updateOne(filtro, updates);

            if (resultado.getMatchedCount() > 0) {
                return usuario;
            } else {
                throw new PersistenciaException("No se encontró el usuario para actualizar.");
            }
        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar el usuario", e);
        }
    }

    @Override
    public List<UsuarioDTO> obtenerTodos() throws PersistenciaException {
        try {
            List<Usuario> listaEntidades = new ArrayList<>();
            coleccion.find().into(listaEntidades);

            List<UsuarioDTO> listaDTOs = new ArrayList<>();
            for (Usuario u : listaEntidades) {
                listaDTOs.add(new UsuarioDTO(u));
            }
            return listaDTOs;
        } catch (Exception e) {
            throw new PersistenciaException("Error al listar usuarios.", e);
        }
    }

    @Override
    public void actualizarEstado(ObjectId idUsuario, boolean esActivo) throws PersistenciaException {
        try {
            // Imprimimos en consola para depurar
            System.out.println("--- INTENTANDO ACTUALIZAR ESTADO ---");
            System.out.println("ID Usuario: " + idUsuario);
            System.out.println("Nuevo Estado: " + esActivo);

            Bson filtro = Filters.eq("_id", idUsuario);
            Bson update = Updates.set("esActivo", esActivo);

            UpdateResult resultado = coleccion.updateOne(filtro, update);

            System.out.println("Documentos encontrados: " + resultado.getMatchedCount());
            System.out.println("Documentos modificados: " + resultado.getModifiedCount());

            if (resultado.getMatchedCount() == 0) {
                System.out.println("¡ALERTA! No se encontró el usuario con ese ID.");
            }

        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar estado del usuario.", e);
        }
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorId(ObjectId id) throws PersistenciaException {
        try {
            Usuario usuario = coleccion.find(Filters.eq("_id", id)).first();
            if (usuario == null) {
                return null;
            }
            return new UsuarioDTO(usuario);
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar usuario por ID.", e);
        }
    }

}
