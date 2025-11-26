/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercenegocio.implementacion;

import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercenegocio.IUsuariosBO;
import itson.ecommercenegocio.excepciones.NegocioException;
import itson.ecommercepersistencia.IUsuariosDAO;
import itson.ecommercepersistencia.excepciones.PersistenciaException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author LENOVO
 */
public class UsuariosBO implements IUsuariosBO{

    private IUsuariosDAO usuariosDAO;

    public UsuariosBO(IUsuariosDAO usuariosDAO) {
        this.usuariosDAO = usuariosDAO;
    }
    
    @Override
    public UsuarioDTO crearUsuario(Usuario usuario) throws NegocioException {
        //Validacion del nombre
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new NegocioException("El nombre es obligatorio");
        }

        try {
            //Verificamos si ya existe el correo
            UsuarioDTO usuarioExistente = usuariosDAO.obtenerUsuarioPorCredenciales(usuario.getEmail());
            if (usuarioExistente != null) {
                throw new NegocioException("Ya existe un usuario registrado con este correo electronico");
            }

            //Encriptacion de contra
            String contraseniaHasheada = BCrypt.hashpw(usuario.getContrasenia(), BCrypt.gensalt());
            usuario.setContrasenia(contraseniaHasheada);
            
            UsuarioDTO nuevoUsuario = new UsuarioDTO(usuario);
            return usuariosDAO.crearUsuario(nuevoUsuario);

        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la DAO al registrar el usuario", e);
        }
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorCredenciales(String email, String contrasenia) throws NegocioException {
        //Validamos que tengamos contra y correo validos
        if (email == null || contrasenia == null || email.trim().isEmpty() || contrasenia.trim().isEmpty()) {
            throw new NegocioException("Correo y contraseña son obligatorios.");
        }

        try {
            //Buscamos al usuario
            UsuarioDTO usuarioEncontrado = usuariosDAO.obtenerUsuarioPorCredenciales(email);

            //Por si no se encuentra el usuario en la base de datos
            if (usuarioEncontrado == null) {
                throw new NegocioException("Credenciales incorrectas.");
            }
            //Verificamos que sea una cuenta activa
            if (!usuarioEncontrado.getEsActivo()) {
                throw new NegocioException("La cuenta de usuario se encuentra inactiva.");
            }
            //Verificacion de la conta hasheada
            if (!BCrypt.checkpw(contrasenia, usuarioEncontrado.getContrasenia())) {
                throw new NegocioException("Credenciales incorrectas.");
            }
            
            return usuarioEncontrado;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la DAO al intentar iniciar sesión.", e);
        }
    }
    
}
