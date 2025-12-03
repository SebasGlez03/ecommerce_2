package itson.ecommercepersistencia.implementaciones;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import itson.ecommercedominio.Resenia;
import itson.ecommercedominio.dtos.ReseniaDTO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import org.bson.types.ObjectId;
import itson.ecommercepersistencia.IReseniasDAO;

public class ReseniasDAO implements IReseniasDAO{
    
    private final MongoCollection<Resenia> coleccion;

    public ReseniasDAO(IConexionBD conexion) {
        this.coleccion = conexion.getCollection("resenias", Resenia.class);
    }

    @Override
    public void agregarResenia(ReseniaDTO resenia) throws PersistenciaException{
        
        Resenia r = new Resenia(resenia.getId(), resenia.getIdUsuario(), resenia.getComentario(), resenia.getCalificacion(), resenia.getFecha());
        
        try {
            coleccion.insertOne(r);
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar la reseña.", e);
        }
    }
    
    @Override
    public void eliminarResenia(ObjectId idResenia) throws PersistenciaException {
        try {
            coleccion.deleteOne(Filters.eq("_id", idResenia));
        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar la reseña.", e);
        }
    }
    
    @Override
    public ReseniaDTO obtenerReseniaPorUsuario(ObjectId usuarioId) throws PersistenciaException {
        try {
            Resenia reseniaEncontrada = coleccion.find(Filters.eq("idUsuario", usuarioId)).first();
            
            if (reseniaEncontrada == null) {
                return null;
            }

            return new ReseniaDTO(reseniaEncontrada);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener la reseña por usuario.", e);
        }
    }
    
    @Override
    public ReseniaDTO obtenerReseniaPorProducto(ObjectId productoId) throws PersistenciaException {
        try {
            Resenia reseniaEncontrada = coleccion.find(Filters.eq("idUsuario", productoId)).first();
            
            if (reseniaEncontrada == null) {
                return null;
            }

            return new ReseniaDTO(reseniaEncontrada);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener la reseña por producto.", e);
        } 
    }
    
    @Override
    public ReseniaDTO obtenerReseniaPorId(ObjectId idResenia) throws PersistenciaException {
        try {
            Resenia resenia = coleccion.find(Filters.eq("_id", idResenia)).first();
            if (resenia == null) {
                return null;
            }
            return new ReseniaDTO(resenia);
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar la reseña por ID.", e);
        }
    }
    
}