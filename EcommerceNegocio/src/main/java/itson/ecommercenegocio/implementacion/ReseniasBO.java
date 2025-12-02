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
public void eliminarResenia(String idReseniaString, UsuarioDTO usuarioSolicitante) throws NegocioException {
    
    if (usuarioSolicitante == null) {
        throw new NegocioException("Debes iniciar sesión para realizar esta acción.");
    }
    
    ObjectId idResenia;
    try {
        idResenia = new ObjectId(idReseniaString);
    } catch (IllegalArgumentException e) {
        throw new NegocioException("ID de reseña inválido.");
    }

    try {
        ReseniaDTO resenia = reseniasDAO.obtenerReseniaPorId(idResenia);
        
        if (resenia == null) {
            throw new NegocioException("La reseña que intentas eliminar no existe.");
        }

        boolean esAdmin = usuarioSolicitante.getRolUsuario() == RolUsuario.ADMIN;
        boolean esPropietario = usuarioSolicitante.getId().equals(resenia.getIdUsuario());

        if (!esAdmin && !esPropietario) {
            throw new NegocioException("No tienes permiso para eliminar esta reseña.");
        }

        reseniasDAO.eliminarResenia(idResenia);

    } catch (PersistenciaException e) {
        throw new NegocioException("Error en la base de datos al eliminar la reseña: " + e.getMessage(), e);
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