/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercepersistencia;

import itson.ecommercedominio.Producto;
import itson.ecommercedominio.dtos.ProductoDTO;
import itson.ecommercedominio.enumeradores.Categoria;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public interface IProductosDAO {
    ProductoDTO obtenerPorId(ObjectId id) throws PersistenciaException;
    List<ProductoDTO> obtenerTodos() throws PersistenciaException;
    List<ProductoDTO> buscar(String nombre, Categoria categoria, Double precioMin, Double precioMax) throws PersistenciaException;
    ProductoDTO agregar(ProductoDTO producto) throws PersistenciaException;
    boolean actualizar(ProductoDTO producto) throws PersistenciaException;
    boolean eliminar(ObjectId id) throws PersistenciaException;
}
