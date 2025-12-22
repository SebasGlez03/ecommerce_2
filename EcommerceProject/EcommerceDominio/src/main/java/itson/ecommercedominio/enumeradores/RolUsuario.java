/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercedominio.enumeradores;

/**
 *
 * @author santi
 */
import org.bson.types.ObjectId;

public enum RolUsuario {

    ADMIN(new ObjectId("60d5ec49e9e1e82a4c14b7eA"), "Administrador"),
    CLIENTE(new ObjectId("60d5ec49e9e1e82a4c14b7eB"), "Cliente"),
    VISITANTE(new ObjectId("60d5ec49e9e1e82a4c14b7eC"), "Visitante");

    private final ObjectId id;
    private final String nombreRol;


    private RolUsuario(ObjectId id, String nombreRol) {
        this.id = id;
        this.nombreRol = nombreRol;
    }


    public ObjectId getId() {
        return id;
    }

    public String getNombre() {
        return nombreRol;
    }

    public String getRol() {
        return name();
    }

    @Override
    public String toString() {
        return "RolUsuario{" + "rol=" + name() + ", id=" + id + ", nombreRol=" + nombreRol + '}';
    }
}

