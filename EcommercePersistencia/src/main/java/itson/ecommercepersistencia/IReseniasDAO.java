/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercepersistencia;

import itson.ecommercedominio.dtos.ReseniaDTO;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import org.bson.types.ObjectId;

/**
 *
 * @author santi
 */
public interface IReseniasDAO {
    
    public void agregarResenia(ReseniaDTO resenia) throws PersistenciaException;
    public ReseniaDTO obtenerReseniaPorUsuario(ObjectId idUsuario) throws PersistenciaException;
    public ReseniaDTO obtenerReseniaPorProducto(ObjectId idUsuario) throws PersistenciaException;
    public void eliminarResenia(ObjectId resenia) throws PersistenciaException ;
    
    
}
