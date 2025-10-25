/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import ConexionDB.ConexionDB;
import DAO.UsuarioDAO;
import Docs.RolUsuario;
import Docs.Usuario;
import Interfaces.IConexionDB;
import excepciones.PersistenciaException;
import org.bson.types.ObjectId;

/**
 *
 * @author santi
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws PersistenciaException {
        // TODO code application logic here
        ObjectId id = new ObjectId();
        Usuario user = new Usuario(id, "Santiago", "santiagosanchez@gmail.com", "12345", "6442259443", Boolean.TRUE, RolUsuario.ADMIN);
        
        IConexionDB conexionDB = new ConexionDB("mongodb://localhost:27017", "Ecommerce");
        
        UsuarioDAO dao = new UsuarioDAO(conexionDB);
        dao.crearUsuario(user);
        
    }
    
}
