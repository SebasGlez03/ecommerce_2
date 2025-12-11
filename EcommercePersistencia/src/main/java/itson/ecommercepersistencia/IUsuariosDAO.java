/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercepersistencia;

import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author LENOVO
 */
public interface IUsuariosDAO {

    public UsuarioDTO crearUsuario(UsuarioDTO nuevoUsuario) throws PersistenciaException;

    public UsuarioDTO obtenerUsuarioPorCredenciales(String correoElectronico) throws PersistenciaException;

    public UsuarioDTO actualizarUsuario(UsuarioDTO usuario) throws PersistenciaException;

    public List<UsuarioDTO> obtenerTodos() throws PersistenciaException;

    public void actualizarEstado(ObjectId idUsuario, boolean esActivo) throws PersistenciaException;

    public UsuarioDTO obtenerUsuarioPorId(ObjectId id) throws PersistenciaException;
}
