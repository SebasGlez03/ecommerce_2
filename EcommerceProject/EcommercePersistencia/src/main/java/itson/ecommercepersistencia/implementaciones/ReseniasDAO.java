package itson.ecommercepersistencia.implementaciones;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import itson.ecommercedominio.Resenia;
import itson.ecommercedominio.dtos.ReseniaDTO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import org.bson.types.ObjectId;
import itson.ecommercepersistencia.IReseniasDAO;
import java.util.ArrayList;
import java.util.List;

public class ReseniasDAO implements IReseniasDAO{
    private final MongoCollection<Resenia> coleccion;

    public ReseniasDAO(IConexionBD conexion) {
        this.coleccion = conexion.getCollection("resenias", Resenia.class);
    }

    @Override
    public void agregarResenia(ReseniaDTO dto) throws PersistenciaException {
        // Convertir DTO a Entidad
        Resenia r = new Resenia(
            new ObjectId(), 
            dto.getIdProducto(),
            dto.getIdUsuario(),
            dto.getComentario(), 
            dto.getCalificacion(), 
            dto.getFecha()
        );
        
        try {
            coleccion.insertOne(r);
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar la reseña.", e);
        }
    }
    
    @Override
    public List<ReseniaDTO> obtenerReseniasPorProducto(ObjectId idProducto) throws PersistenciaException {
        try {
            List<Resenia> listaEntidades = new ArrayList<>();
            // Buscamos todas las reseñas que coincidan con el idProducto
            coleccion.find(Filters.eq("idProducto", idProducto)).into(listaEntidades);

            List<ReseniaDTO> listaDTOs = new ArrayList<>();
            for (Resenia r : listaEntidades) {
                // Mapeamos manualmente o con constructor
                ReseniaDTO dto = new ReseniaDTO();
                dto.setId(r.getId());
                dto.setIdUsuario(r.getIdUsuario());
                dto.setIdProducto(r.getIdProducto());
                dto.setComentario(r.getComentario());
                dto.setCalificacion(r.getCalificacion());
                dto.setFecha(r.getFecha());
                listaDTOs.add(dto);
            }
            return listaDTOs;
        } catch (Exception e) {
            throw new PersistenciaException("Error al listar reseñas del producto.", e);
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
    public ReseniaDTO obtenerReseniaPorId(ObjectId idResenia) throws PersistenciaException {
        try {
            Resenia resenia = coleccion.find(Filters.eq("_id", idResenia)).first();
            if (resenia == null) return null;
            return new ReseniaDTO(resenia); // Asegúrate que tu DTO tenga este constructor que recibe Entidad
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar la reseña por ID.", e);
        }
    }
    
    /**
     * Este nomas para q no truenen las pruebas, no lo vamos a usar jeje
     * @param idUsuario
     * @return
     * @throws PersistenciaException 
     */
    @Override
    public ReseniaDTO obtenerReseniaPorUsuario(ObjectId idUsuario) throws PersistenciaException {
        try {
            Resenia resenia = coleccion.find(Filters.eq("idUsuario", idUsuario)).first();
            if (resenia == null) return null;
            return new ReseniaDTO(resenia);
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar la reseña por ID.", e);
        }
    }
    
    @Override
    public List<ReseniaDTO> obtenerReseniasPorUsuario(ObjectId idUsuario) throws PersistenciaException {
        try {
            List<Resenia> listaEntidades = new ArrayList<>();
            
            // Buscamos todas las reseñas donde 'idUsuario' coincida
            coleccion.find(Filters.eq("idUsuario", idUsuario)).into(listaEntidades);

            // Convertimos a DTOs
            List<ReseniaDTO> listaDTOs = new ArrayList<>();
            for (Resenia r : listaEntidades) {
                listaDTOs.add(new ReseniaDTO(r));
            }
            
            return listaDTOs;
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener las reseñas del usuario.", e);
        }
    }
}