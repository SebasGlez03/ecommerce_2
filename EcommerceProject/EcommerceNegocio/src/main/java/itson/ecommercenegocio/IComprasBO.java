/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercenegocio;

import itson.ecommercedominio.dtos.CompraDTO;
import itson.ecommercedominio.enumeradores.EstadoCompra;
import itson.ecommercenegocio.excepciones.NegocioException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public interface IComprasBO {
    CompraDTO realizarCompra(CompraDTO compra) throws NegocioException;
    void actualizarEstado(ObjectId idCompra, EstadoCompra estado) throws NegocioException;
    List<CompraDTO> obtenerTodasLasCompras() throws NegocioException;
    List<CompraDTO> obtenerHistorialUsuario(ObjectId usuarioId) throws NegocioException;
}
