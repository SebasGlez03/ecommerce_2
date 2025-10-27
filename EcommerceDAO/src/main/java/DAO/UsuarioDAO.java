package DAO;

import Docs.Usuario;
import Interfaces.IConexionDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import excepciones.PersistenciaException;
import org.bson.conversions.Bson;

/**
 *
 * @author santi
 */
public class UsuarioDAO {

    private final MongoCollection<Usuario> coleccion;
    
    public UsuarioDAO(IConexionDB conexionDB) {
    
        MongoDatabase database = conexionDB.getDatabase();  
        this.coleccion = database.getCollection("Usuarios", Usuario.class);
        
    }
    
    public Usuario crearUsuario(Usuario usuario) throws PersistenciaException {
        try {
            coleccion.insertOne(usuario); 
            return usuario;
        } catch (Exception e) {
            throw new PersistenciaException("Error al crear el usuario en la base de datos", e);
        }
    }
    
    public Usuario obtenerUsuarioPorCredenciales(String correoElectronico) throws PersistenciaException {
        try {
            Bson filtro = Filters.regex("email", "^" + correoElectronico + "$", "i"); 
            Usuario documentoUsuario = coleccion.find(filtro).first(); 

            return documentoUsuario;  
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener el usuario por credenciales", e);
        }
    }
    
}
