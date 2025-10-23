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
public enum Categoria {
    
    AUDIFONOS(new ObjectId("60d5ec49e9e1e82a4c14b7e1"), "Audifonos"),
    MONITORES(new ObjectId("60d5ec49e9e1e82a4c14b7e2"), "Monitores"),
    TECLADOS(new ObjectId("60d5ec49e9e1e82a4c14b7e3"), "Teclados");

    private final ObjectId id;
    private final String nombreCat;

    private Categoria(ObjectId id, String nombreCat) {
        this.id = id;
        this.nombreCat = nombreCat;
    }

    public ObjectId getId() {
        return id;
    }

    public String getNombre() {
        return nombreCat;
    }

    @Override
    public String toString() {
        return "Categoria{" + "name=" + name() + ", id=" + id + ", nombreCat=" + nombreCat + '}';
    }
}
    

