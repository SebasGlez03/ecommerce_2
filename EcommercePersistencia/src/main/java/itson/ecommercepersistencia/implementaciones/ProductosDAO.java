/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercepersistencia.implementaciones;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import itson.ecommercedominio.Producto;
import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.enumeradores.Categoria;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.IProductosDAO;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public class ProductosDAO implements IProductosDAO{
    private final MongoCollection<Producto> coleccion;

    public ProductosDAO(IConexionBD conexion) {
        this.coleccion = conexion.getCollection("productos", Producto.class);
    }

    @Override
    public ProductoDTO agregar(ProductoDTO productoDTO) throws PersistenciaException {
        try {
            // 1. Convertir DTO a Entidad para guardar en BD
            Producto p = new Producto(
                productoDTO.getNombre(),
                productoDTO.getDescripcion(),
                productoDTO.getPrecio(),
                productoDTO.getStock(),
                productoDTO.getCategoria(),
                productoDTO.getImagenUrl()
            );
            
            // 2. Insertar en MongoDB
            coleccion.insertOne(p);

            // 3. Devolver el DTO actualizado con el nuevo ID generado
            return convertirADTO(p);
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductoDTO obtenerPorId(ObjectId id) throws PersistenciaException {
        try {
            // 1. Buscar Entidad
            Producto p = coleccion.find(Filters.eq("_id", id)).first();
            
            // 2. Si no existe, retornamos null
            if (p == null) {
                return null;
            }

            // 3. Convertir a DTO y retornar
            return convertirADTO(p);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener producto por ID.", e);
        }
    }

    @Override
    public List<ProductoDTO> obtenerTodos() throws PersistenciaException {
        try {
            List<Producto> listaEntidades = new ArrayList<>();
            coleccion.find().into(listaEntidades);

            // Convertimos la lista de Entidades a lista de DTOs usando Streams
            return listaEntidades.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new PersistenciaException("Error al listar productos.", e);
        }
    }

    @Override
    public List<ProductoDTO> buscar(String nombre, Categoria categoria, Double precioMin, Double precioMax) throws PersistenciaException {
        try {
            List<Bson> filtros = new ArrayList<>();

            if (nombre != null && !nombre.trim().isEmpty()) {
                filtros.add(Filters.regex("nombre", ".*" + nombre + ".*", "i"));
            }
            if (categoria != null) {
                filtros.add(Filters.eq("categoria", categoria.name()));
            }
            if (precioMin != null) {
                filtros.add(Filters.gte("precio", precioMin));
            }
            if (precioMax != null) {
                filtros.add(Filters.lte("precio", precioMax));
            }

            Bson filtroFinal = filtros.isEmpty() ? new Document() : Filters.and(filtros);

            List<Producto> listaEntidades = new ArrayList<>();
            coleccion.find(filtroFinal).into(listaEntidades);

            // Convertir a DTOs
            return listaEntidades.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new PersistenciaException("Error en la búsqueda de productos.", e);
        }
    }

    @Override
    public boolean actualizar(ProductoDTO p) throws PersistenciaException {
        try {
            Bson filtro = Filters.eq("_id", p.getId());
            
            String catStr = (p.getCategoria() != null) ? p.getCategoria().name() : null;
            
            Bson updates = Updates.combine(
                Updates.set("nombre", p.getNombre()),
                Updates.set("descripcion", p.getDescripcion()),
                Updates.set("precio", p.getPrecio()),
                Updates.set("stock", p.getStock()),
                Updates.set("categoria", catStr),
                Updates.set("imagenUrl", p.getImagenUrl())
            );

            // updateOne devuelve un resultado que nos dice si encontró y modificó algo
            UpdateResult resultado = coleccion.updateOne(filtro, updates);

            // getMatchedCount > 0 significa que el ID existía y se intentó actualizar
            return resultado.getMatchedCount() > 0;

        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar producto.", e);
        }
    }

    @Override
    public boolean eliminar(ObjectId id) throws PersistenciaException {
        try {
            DeleteResult resultado = coleccion.deleteOne(Filters.eq("_id", id));
            
            // getDeletedCount > 0 significa que se borró un documento
            return resultado.getDeletedCount() > 0;
            
        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar producto.", e);
        }
    }

    /**
     * Método auxiliar para convertir de Entidad (DB) a DTO (Vista).
     * Centraliza la conversión para evitar errores y código duplicado.
     */
    private ProductoDTO convertirADTO(Producto p) {
        // Asumiendo que ProductoDTO tiene un constructor que recibe todos los campos
        // O usando el constructor vacío y setters
        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setCategoria(p.getCategoria());
        dto.setImagenUrl(p.getImagenUrl());
        return dto;
    }
    
}
