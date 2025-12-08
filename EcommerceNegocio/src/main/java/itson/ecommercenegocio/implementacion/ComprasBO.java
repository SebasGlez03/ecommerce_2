/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercenegocio.implementacion;

import itson.ecommercedominio.dtos.CompraDTO;
import itson.ecommercedominio.dtos.DetalleCompraDTO;
import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.enumeradores.EstadoCompra;
import itson.ecommercenegocio.IComprasBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercepersistencia.IComprasDAO;
import itson.ecommercepersistencia.IProductosDAO;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public class ComprasBO implements IComprasBO{
    private final IComprasDAO comprasDAO;
    //Vamos a ocupar ProductosDAO para verificar y descontar stock
    private final IProductosDAO productosDAO; 

    public ComprasBO(IComprasDAO comprasDAO, IProductosDAO productosDAO) {
        this.comprasDAO = comprasDAO;
        this.productosDAO = productosDAO;
    }

    @Override
    public CompraDTO realizarCompra(CompraDTO compra) throws NegocioException {
        // 1. Validaciones generales
        if (compra == null || compra.getDetalles() == null || compra.getDetalles().isEmpty()) {
            throw new NegocioException("El carrito de compras está vacío.");
        }
        if (compra.getUsuarioId() == null) {
            throw new NegocioException("Se requiere un usuario para procesar la compra.");
        }

        try {
            // 2. VERIFICACIÓN Y ACTUALIZACIÓN DE STOCK
            // Recorremos cada producto que se quiere comprar
            for (DetalleCompraDTO detalle : compra.getDetalles()) {
                
                // Buscamos el producto en la BD
                ProductoDTO productoEnBD = productosDAO.obtenerPorId(detalle.getProductoId());

                // Validamos que exista
                if (productoEnBD == null) {
                    throw new NegocioException("El producto '" + detalle.getNombreProducto() + "' ya no está disponible.");
                }

                // Validamos que alcance el stock
                if (productoEnBD.getStock() < detalle.getCantidad()) {
                    throw new NegocioException("Stock insuficiente para: " + productoEnBD.getNombre() + 
                                               ". Disponible: " + productoEnBD.getStock());
                }

                // Descontamos el stock
                int nuevoStock = productoEnBD.getStock() - detalle.getCantidad();
                productoEnBD.setStock(nuevoStock);

                // Actualizamos el producto en la BD
                productosDAO.actualizar(productoEnBD);
            }

            // 3. Asignar estado inicial si no viene
            if (compra.getEstado() == null) {
                compra.setEstado(EstadoCompra.PENDIENTE);
            }

            // 4. Guardar la compra (Una vez asegurado el stock)
            return comprasDAO.crear(compra);

        } catch (PersistenciaException e) {
            // Nota: En un entorno real sin transacciones ACID multidocumento, 
            // aquí habría riesgo de inconsistencia si falla el 'crear' después de actualizar stock.
            // Para fines académicos, este bloque try-catch es aceptable.
            throw new NegocioException("Error al procesar la compra: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<CompraDTO> obtenerTodasLasCompras() throws NegocioException {
        try {
            // Requiere que agregues el método en IComprasDAO y ComprasDAO
            return comprasDAO.obtenerTodas(); 
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al consultar todas las compras", e);
        }
    }

    @Override
    public void actualizarEstado(ObjectId idCompra, EstadoCompra estado) throws NegocioException {
        if(idCompra == null || estado == null) throw new NegocioException("Datos inválidos");
        try {
            comprasDAO.actualizarEstado(idCompra, estado);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al actualizar estado", e);
        }
    }

    @Override
    public List<CompraDTO> obtenerHistorialUsuario(ObjectId usuarioId) throws NegocioException {
        if (usuarioId == null) {
            throw new NegocioException("El ID de usuario es requerido.");
        }

        try {
            return comprasDAO.obtenerPorUsuario(usuarioId);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al recuperar el historial de compras.", e);
        }
    }
}
