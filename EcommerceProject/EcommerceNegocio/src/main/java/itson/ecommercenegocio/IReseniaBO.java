/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercenegocio;

import itson.ecommercedominio.Resenia;
import itson.ecommercedominio.dtos.ReseniaDTO;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author santi
 */
public interface IReseniaBO {
    void crearResenia(ReseniaDTO reseniaDTO) throws NegocioException;
    void eliminarResenia(String idReseniaString, UsuarioDTO usuarioSolicitante) throws NegocioException;
    List<ReseniaDTO> obtenerReseniasPorProducto(ObjectId idProducto) throws NegocioException;
    List<ReseniaDTO> obtenerReseniasPorUsuario(ObjectId idUsuario) throws NegocioException;
    ReseniaDTO obtenerPrimeraReseniaPorUsuario(ObjectId idUsuario) throws NegocioException;
}
