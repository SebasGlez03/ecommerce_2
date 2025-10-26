/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import DAO.UsuarioDAO;
import Docs.Usuario;
import Excepciones.NegocioException;
import excepciones.PersistenciaException;

/**
 *
 * @author Edu
 */
public class UsuarioBO implements IUsuarioBO {

    private UsuarioDAO usuarioDAO;

    @Override
    public Usuario crearUsuario(Usuario usuario) throws NegocioException {
        //Validacion del nombre
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new NegocioException("El nombre es obligatorio");
        }

        try {
            //Verificamos si ya existe el correo
            Usuario usuarioExistente = usuarioDAO.obtenerUsuarioPorCredenciales(usuario.getEmail());
            if (usuarioExistente != null) {
                throw new NegocioException("Ya existe un usuario registrado con este correo electronico");
            }
            return usuarioDAO.crearUsuario(usuario);

        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la DAO al registrar el usuario", e);
        }
    }

    @Override
    public Usuario obtenerUsuarioPorCredenciales(String email, String contrasenia) throws NegocioException {
        //Validamos que tengamos contra y correo validos
        if (email == null || contrasenia == null || email.trim().isEmpty() || contrasenia.trim().isEmpty()) {
            throw new NegocioException("Correo y contraseña son obligatorios.");
        }

        try {
            //Buscamos al usuario
            Usuario usuarioEncontrado = usuarioDAO.obtenerUsuarioPorCredenciales(email);

            //Por si no se encuentra el usuario en la base de datos
            if (usuarioEncontrado == null) {
                throw new NegocioException("Credenciales incorrectas.");
            }
            //Verificamos que sea una cuenta activa
            if (!usuarioEncontrado.getEsActivo()) {
                throw new NegocioException("La cuenta de usuario se encuentra inactiva.");
            }
            //Verificamos que sea la contra correcta
            if (!usuarioEncontrado.getContrasenia().equals(contrasenia)) {
                throw new NegocioException("Credenciales incorrectas.");
            }

            return usuarioEncontrado;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la DAO al intentar iniciar sesión.", e);
        }
    }

}
