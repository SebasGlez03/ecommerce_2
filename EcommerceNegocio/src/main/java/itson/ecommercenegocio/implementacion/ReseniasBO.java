package itson.ecommercenegocio.implementacion;

import itson.ecommercedominio.dtos.ReseniaDTO;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercedominio.enumeradores.RolUsuario;
import itson.ecommercenegocio.IReseniaBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercepersistencia.IReseniasDAO;
import itson.ecommercepersistencia.IUsuariosDAO;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

public class ReseniasBO implements IReseniaBO {

    private final IReseniasDAO reseniasDAO;
    private final IUsuariosDAO usuariosDAO;

    // Inyección de dependencias por constructor
    public ReseniasBO(IReseniasDAO reseniasDAO, IUsuariosDAO usuariosDAO) {
        this.reseniasDAO = reseniasDAO;
        this.usuariosDAO = usuariosDAO;
    }

    @Override
    public void crearResenia(ReseniaDTO reseniaDTO) throws NegocioException {
        // 1. Validaciones de datos
        if (reseniaDTO == null) {
            throw new NegocioException("La información de la reseña no puede ser nula.");
        }
        if (reseniaDTO.getIdProducto() == null) {
            throw new NegocioException("La reseña debe estar asociada a un producto.");
        }
        if (reseniaDTO.getIdUsuario() == null) {
            throw new NegocioException("La reseña debe estar asociada a un usuario.");
        }
        if (reseniaDTO.getComentario() == null || reseniaDTO.getComentario().trim().isEmpty()) {
            throw new NegocioException("El comentario es obligatorio.");
        }
        // Validación de rango de estrellas (1 a 5)
        if (reseniaDTO.getCalificacion() < 1 || reseniaDTO.getCalificacion() > 5) {
            throw new NegocioException("La calificación debe ser un valor entre 1 y 5.");
        }

        try {
            // 2. Llamada a la persistencia (El DAO convierte el DTO a Entidad)
            reseniasDAO.agregarResenia(reseniaDTO);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al guardar la reseña en el sistema.", e);
        }
    }

    @Override
    public void eliminarResenia(String idReseniaString, UsuarioDTO usuarioSolicitante) throws NegocioException {
        // 1. Validar que haya un usuario intentando la acción
        if (usuarioSolicitante == null) {
            throw new NegocioException("Debes iniciar sesión para eliminar una reseña.");
        }

        // 2. Validar formato del ID
        ObjectId idResenia;
        try {
            idResenia = new ObjectId(idReseniaString);
        } catch (IllegalArgumentException e) {
            throw new NegocioException("El ID de la reseña proporcionado no es válido.");
        }

        try {
            // 3. Buscar la reseña para verificar que existe y verificar dueños
            ReseniaDTO resenia = reseniasDAO.obtenerReseniaPorId(idResenia);

            if (resenia == null) {
                throw new NegocioException("La reseña que intentas eliminar no existe.");
            }

            // 4. Regla de Negocio: Solo el dueño de la reseña o un ADMIN pueden eliminarla
            boolean esAdmin = usuarioSolicitante.getRolUsuario() == RolUsuario.ADMIN;
            boolean esPropietario = usuarioSolicitante.getId().equals(resenia.getIdUsuario());

            if (!esAdmin && !esPropietario) {
                throw new NegocioException("No tienes permisos para eliminar esta reseña.");
            }

            // 5. Eliminar
            reseniasDAO.eliminarResenia(idResenia);

        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la base de datos al intentar eliminar la reseña.", e);
        }
    }

    @Override
    public List<ReseniaDTO> obtenerReseniasPorProducto(ObjectId idProducto) throws NegocioException {
        if (idProducto == null) {
            throw new NegocioException("El ID del producto es requerido.");
        }

        try {
            List<ReseniaDTO> resenias = reseniasDAO.obtenerReseniasPorProducto(idProducto);

            // --- NUEVA LÓGICA: Llenar los nombres ---
            for (ReseniaDTO r : resenias) {
                if (r.getIdUsuario() != null) {
                    UsuarioDTO u = usuariosDAO.obtenerUsuarioPorId(r.getIdUsuario());
                    if (u != null) {
                        r.setNombreUsuario(u.getNombre());
                    } else {
                        r.setNombreUsuario("Usuario Eliminado");
                    }
                } else {
                    r.setNombreUsuario("Anónimo");
                }
            }
            // ----------------------------------------

            return resenias;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al consultar las reseñas del producto.", e);
        }
    }

    @Override
    public List<ReseniaDTO> obtenerReseniasPorUsuario(ObjectId idUsuario) throws NegocioException {
        if (idUsuario == null) {
            throw new NegocioException("El ID del usuario es requerido.");
        }

        try {
            return reseniasDAO.obtenerReseniasPorUsuario(idUsuario);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al consultar el historial de reseñas del usuario.", e);
        }
    }

    // Método opcional si lo definiste en la interfaz para validar si ya comentó
    @Override
    public ReseniaDTO obtenerPrimeraReseniaPorUsuario(ObjectId idUsuario) throws NegocioException {
        if (idUsuario == null) {
            return null;
        }
        try {
            return reseniasDAO.obtenerReseniaPorUsuario(idUsuario);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar reseña.", e);
        }
    }

}
