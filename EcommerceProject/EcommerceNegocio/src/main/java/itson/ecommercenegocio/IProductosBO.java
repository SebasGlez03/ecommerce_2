/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercenegocio;

import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.enumeradores.Categoria;
import itson.ecommercenegocio.excepciones.NegocioException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public interface IProductosBO {
    public ProductoDTO agregarProducto(ProductoDTO producto) throws NegocioException;
    public ProductoDTO obtenerProductoPorId(ObjectId id) throws NegocioException ;
    public List<ProductoDTO> obtenerTodos() throws NegocioException;
    public List<ProductoDTO> buscarProductos(String nombre, Categoria categoria, Double precioMin, Double precioMax) throws NegocioException;
    void actualizarProducto(ProductoDTO producto) throws NegocioException;
    void eliminarProducto(ObjectId id) throws NegocioException;
}
