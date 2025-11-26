/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercenegocio;

import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercenegocio.excepciones.NegocioException;

/**
 *
 * @author LENOVO
 */
public interface IUsuariosBO {
    
    public UsuarioDTO crearUsuario(Usuario nuevoUsuario) throws NegocioException;
    
    public UsuarioDTO obtenerUsuarioPorCredenciales(String email, String contrasenia) throws NegocioException;
}
