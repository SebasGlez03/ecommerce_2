/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercepersistencia;

import itson.ecommercedominio.dtos.CompraDTO;
import itson.ecommercedominio.enumeradores.EstadoCompra;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public interface IComprasDAO {
    CompraDTO crear(CompraDTO compra) throws PersistenciaException;
    List<CompraDTO> obtenerPorUsuario(ObjectId usuarioId) throws PersistenciaException;
    List<CompraDTO> obtenerTodas() throws PersistenciaException;
    void actualizarEstado(ObjectId idCompra, EstadoCompra estado) throws PersistenciaException;
}
