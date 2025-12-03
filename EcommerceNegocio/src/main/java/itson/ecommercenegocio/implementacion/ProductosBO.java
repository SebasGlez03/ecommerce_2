/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercenegocio.implementacion;

import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.enumeradores.Categoria;
import itson.ecommercenegocio.IProductosBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercepersistencia.IProductosDAO;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public class ProductosBO implements IProductosBO{
    private final IProductosDAO productosDAO;

    public ProductosBO(IProductosDAO productosDAO) {
        this.productosDAO = productosDAO;
    }

    @Override
    public ProductoDTO agregarProducto(ProductoDTO producto) throws NegocioException {
        // 1. Validaciones de Negocio
        validarProducto(producto);

        try {
            // 2. Llamada al DAO
            return productosDAO.agregar(producto);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la capa de datos al agregar el producto.", e);
        }
    }

    @Override
    public ProductoDTO obtenerProductoPorId(ObjectId id) throws NegocioException {
        if (id == null) {
            throw new NegocioException("El ID del producto es requerido.");
        }

        try {
            ProductoDTO producto = productosDAO.obtenerPorId(id);
            if (producto == null) {
                throw new NegocioException("No se encontró ningún producto con el ID especificado.");
            }
            return producto;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar el producto.", e);
        }
    }

    @Override
    public List<ProductoDTO> obtenerTodos() throws NegocioException {
        try {
            return productosDAO.obtenerTodos();
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al recuperar el catálogo de productos.", e);
        }
    }

    @Override
    public List<ProductoDTO> buscarProductos(String nombre, Categoria categoria, Double precioMin, Double precioMax) throws NegocioException {
        // Validación lógica de precios
        if (precioMin != null && precioMax != null && precioMin > precioMax) {
            throw new NegocioException("El precio mínimo no puede ser mayor al precio máximo.");
        }

        try {
            return productosDAO.buscar(nombre, categoria, precioMin, precioMax);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al filtrar productos.", e);
        }
    }

    @Override
    public void actualizarProducto(ProductoDTO producto) throws NegocioException {
        validarProducto(producto);
        if (producto.getId() == null) {
            throw new NegocioException("No se puede actualizar un producto sin ID.");
        }

        try {
            boolean exito = productosDAO.actualizar(producto);
            if (!exito) {
                throw new NegocioException("No se pudo actualizar. El producto tal vez ya no existe.");
            }
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al actualizar el producto.", e);
        }
    }

    @Override
    public void eliminarProducto(ObjectId id) throws NegocioException {
        if (id == null) {
            throw new NegocioException("El ID es requerido para eliminar.");
        }

        try {
            boolean exito = productosDAO.eliminar(id);
            if (!exito) {
                throw new NegocioException("No se pudo eliminar. El producto no existe.");
            }
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar el producto.", e);
        }
    }

    // --- Método auxiliar para no repetir código de validación ---
    private void validarProducto(ProductoDTO p) throws NegocioException {
        if (p == null) {
            throw new NegocioException("La información del producto no puede ser nula.");
        }
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new NegocioException("El nombre del producto es obligatorio.");
        }
        if (p.getPrecio() == null || p.getPrecio() <= 0) {
            throw new NegocioException("El precio debe ser mayor a 0.");
        }
        if (p.getStock() == null || p.getStock() < 0) {
            throw new NegocioException("El stock no puede ser negativo.");
        }
        if (p.getCategoria() == null) {
            throw new NegocioException("Debe seleccionar una categoría.");
        }
    }
}
