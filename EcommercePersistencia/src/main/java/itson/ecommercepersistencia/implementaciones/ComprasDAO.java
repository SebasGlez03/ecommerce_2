/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercepersistencia.implementaciones;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import itson.ecommercedominio.Compra;
import itson.ecommercedominio.DetalleCompra;
import itson.ecommercedominio.dtos.CompraDTO;
import itson.ecommercedominio.dtos.DetalleCompraDTO;
import itson.ecommercedominio.enumeradores.EstadoCompra;
import itson.ecommercepersistencia.IComprasDAO;
import itson.ecommercepersistencia.IConexionBD;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public class ComprasDAO implements IComprasDAO{
    private final MongoCollection<Compra> coleccion;

    public ComprasDAO(IConexionBD conexion) {
        // Cambiamos el nombre de la colección a "compras"
        this.coleccion = conexion.getCollection("compras", Compra.class);
    }

    @Override
    public CompraDTO crear(CompraDTO compraDTO) throws PersistenciaException {
        try {
            // 1. Convertir DTO a Entidad
            compraDTO.setId(new ObjectId());
            Compra compra = convertirAEntidad(compraDTO);

            // 2. Insertar en MongoDB
            coleccion.insertOne(compra);

            // 3. Regresar DTO actualizado (con el ID generado por Mongo)
            return convertirADTO(compra);
        } catch (Exception e) {
            throw new PersistenciaException("Error al registrar la compra: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void actualizarEstado(ObjectId idCompra, EstadoCompra nuevoEstado) throws PersistenciaException {
        try {
            coleccion.updateOne(Filters.eq("_id", idCompra), Updates.set("estado", nuevoEstado));
        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar estado de la compra.", e);
        }
    }
    
    @Override
    public List<CompraDTO> obtenerTodas() throws PersistenciaException {
        try {
            List<Compra> listaEntidades = new ArrayList<>();
            // Sin filtro, ordena por fecha descendente
            coleccion.find()
                     .sort(Sorts.descending("fechaCompra"))
                     .into(listaEntidades);

            return listaEntidades.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener todas las compras.", e);
        }
    }

    @Override
    public List<CompraDTO> obtenerPorUsuario(ObjectId usuarioId) throws PersistenciaException {
        try {
            List<Compra> listaEntidades = new ArrayList<>();
            
            // Filtramos por usuario y ordenamos por fecha descendente (lo más nuevo primero)
            coleccion.find(Filters.eq("usuarioId", usuarioId))
                     .sort(Sorts.descending("fechaCompra"))
                     .into(listaEntidades);

            // Convertir lista de Entidades a lista de DTOs
            return listaEntidades.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener historial de compras.", e);
        }
    }
    
    private Compra convertirAEntidad(CompraDTO dto) {
        // Convertimos la lista de detalles (DTO -> Entidad)
        List<DetalleCompra> detallesEntidad = new ArrayList<>();
        
        if (dto.getDetalles() != null) {
            detallesEntidad = dto.getDetalles().stream()
                .map(d -> new DetalleCompra(d.getProductoId(), d.getNombreProducto(), d.getCantidad(), d.getPrecioUnitario()))
                .collect(Collectors.toList());
        }

        Compra compra = new Compra(
            dto.getUsuarioId(),
            dto.getDireccionEnvio(),
            dto.getMetodoPago(),
            dto.getTotal(),
            detallesEntidad
        );
        
        // Si el DTO ya tiene estado, lo asignamos; si no, el constructor de Compra debería poner PENDIENTE
        if(dto.getEstado() != null) {
            compra.setEstado(dto.getEstado());
        }
        
        // Si es una actualización o el DTO ya trae fecha, la respetamos
        if(dto.getFechaCompra() != null) {
            compra.setFechaCompra(dto.getFechaCompra());
        }
        
        // Si el DTO trae ID (ej. actualización), lo ponemos
        if(dto.getId() != null) {
            compra.setId(dto.getId());
        }
        
        return compra;
    }

    private CompraDTO convertirADTO(Compra entidad) {
        // Convertimos la lista de detalles (Entidad -> DTO)
        List<DetalleCompraDTO> detallesDTO = new ArrayList<>();
        
        if (entidad.getDetalles() != null) {
            detallesDTO = entidad.getDetalles().stream()
                .map(d -> new DetalleCompraDTO(d.getProductoId(), d.getNombreProducto(), d.getCantidad(), d.getPrecioUnitario()))
                .collect(Collectors.toList());
        }

        CompraDTO dto = new CompraDTO();
        dto.setId(entidad.getId());
        dto.setUsuarioId(entidad.getUsuarioId());
        dto.setFechaCompra(entidad.getFechaCompra());
        dto.setEstado(entidad.getEstado());
        dto.setTotal(entidad.getTotal());
        dto.setDireccionEnvio(entidad.getDireccionEnvio());
        dto.setMetodoPago(entidad.getMetodoPago());
        dto.setDetalles(detallesDTO);
        
        return dto;
    }
}
