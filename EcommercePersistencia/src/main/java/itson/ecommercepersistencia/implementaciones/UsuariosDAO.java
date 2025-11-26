/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercepersistencia.implementaciones;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercepersistencia.IUsuariosDAO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author LENOVO
 */
public class UsuariosDAO implements IUsuariosDAO{

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
 
}
