/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercedominio.dtos;

import itson.ecommercedominio.Usuario;
import itson.ecommercedominio.enumeradores.RolUsuario;
import org.bson.types.ObjectId;

/**
 *
 * @author LENOVO
 */
public class UsuarioDTO {
    
    private ObjectId id;
    private String nombre;
    private String email;
    private String contrasenia;
    private String direccion;
    private String telefono;
    private Boolean esActivo;
    private RolUsuario rolUsuario;

    public UsuarioDTO(ObjectId id, String nombre, String email, String contrasenia, String direccion, String telefono, Boolean esActivo, RolUsuario rolUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.direccion = direccion;
        this.telefono = telefono;
        this.esActivo = esActivo;
        this.rolUsuario = rolUsuario;
    }
    
    public UsuarioDTO(Usuario usuario){
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.contrasenia = usuario.getContrasenia();
        this.direccion = usuario.getDireccion();
        this.telefono = usuario.getTelefono();
        this.esActivo = usuario.getEsActivo();
        this.rolUsuario = usuario.getRolUsuario();
    }

    public ObjectId getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public Boolean getEsActivo() {
        return esActivo;
    }

    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" + "id=" + id + ", nombre=" + nombre + ", email=" + email + ", contrasenia=" + contrasenia + ", direccion=" + direccion + ", telefono=" + telefono + ", esActivo=" + esActivo + ", rolUsuario=" + rolUsuario + '}';
    }
    
    
    
}
