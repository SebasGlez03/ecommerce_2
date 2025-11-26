/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercepersistencia;

import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercepersistencia.excepciones.PersistenciaException;

/**
 *
 * @author LENOVO
 */
public interface IUsuariosDAO {
    
    public UsuarioDTO crearUsuario(UsuarioDTO nuevoUsuario) throws PersistenciaException;
    
    public UsuarioDTO obtenerUsuarioPorCredenciales(String correoElectronico) throws PersistenciaException;
}
