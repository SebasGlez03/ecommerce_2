/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package BO;

import Docs.Usuario;
import Excepciones.NegocioException;

/**
 *
 * @author Edu
 */
public interface IUsuarioBO {

    public Usuario crearUsuario(Usuario usuario)throws NegocioException;

    public Usuario obtenerUsuarioPorCredenciales(String email, String contrasenia)throws NegocioException;
}
