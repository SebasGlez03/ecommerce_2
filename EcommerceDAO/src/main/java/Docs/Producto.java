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
public class Producto {
    
    private ObjectId id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private int existencia;
    private String imagen;
    private String espTecnicas;
    private Categoria categoria;

    public Producto() {
    }

    public Producto( String nombre, String descripcion, Double precio, int existencia, String imagen, String espTecnicas, Categoria categoria) {
        this.id = new ObjectId();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.existencia = existencia;
        this.imagen = imagen;
        this.espTecnicas = espTecnicas;
        this.categoria = categoria;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getEspTecnicas() {
        return espTecnicas;
    }

    public void setEspTecnicas(String espTecnicas) {
        this.espTecnicas = espTecnicas;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio + ", existencia=" + existencia + ", imagen=" + imagen + ", espTecnicas=" + espTecnicas + ", categoria=" + categoria + '}';
    }
    
}
