package itson.ecommercenegocio.implementacion;

import itson.ecommercedominio.Resenia;
import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.ReseniaDTO;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercedominio.enumeradores.RolUsuario;
import itson.ecommercenegocio.IReseniaBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercepersistencia.implementaciones.ReseniasDAO; // Usa la interfaz preferiblemente
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

public class ReseniasBO  implements IReseniaBO{

    private ReseniasDAO reseniasDAO;

    public ReseniasBO(ReseniasDAO reseniasDAO){
        this.reseniasDAO = reseniasDAO;
    }

    @Override
    public void moderarResenia(String idReseniaString, UsuarioDTO usuarioSolicitante) throws NegocioException {

        if (usuarioSolicitante == null || usuarioSolicitante.getRolUsuario() != RolUsuario.ADMIN) {
            throw new NegocioException("Acceso denegado: Solo los administradores pueden moderar reseñas.");
        }

        try {
            ObjectId idResenia = new ObjectId(idReseniaString);
            reseniasDAO.eliminarResenia(idResenia);
        } catch (IllegalArgumentException e) {
             throw new NegocioException("ID de reseña inválido.");
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar la reseña: " + e.getMessage());
        }
    }
    
    @Override
    public void crearResenia(Resenia resenia) throws NegocioException {


        try {

            ReseniaDTO nuevaResenia = new ReseniaDTO(resenia);
            reseniasDAO.agregarResenia(nuevaResenia);

        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la DAO al agregar resenia", e);
        }
    }
}