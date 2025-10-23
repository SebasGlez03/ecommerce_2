/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Docs;

import org.bson.types.ObjectId;

/**
 *
 * @author santi
 */
public enum EstadoPedido {
    
    PROCESANDO(new ObjectId("671abb8b7e2d3f4a5b6c7d8e"), "Procesando"),
    ENENTREGA(new ObjectId("671abb8b7e2d3f4a5b6c7d8f"), "En entrega"),
    ENTREGADO(new ObjectId("671abb8b7e2d3f4a5b6c7d90"), "Entregado");
    
    private final ObjectId id;
    private final String nombre;

    private EstadoPedido(ObjectId id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public ObjectId getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "EstadoPedido{" + "ordinal=" + ordinal() + ", name=" + name() + ", id=" + id + ", nombre=" + nombre + '}';
    }
    
}
