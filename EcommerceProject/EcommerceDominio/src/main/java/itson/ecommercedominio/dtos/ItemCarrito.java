/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercedominio.dtos;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public class ItemCarrito {
    private ObjectId productoId;
    private String nombre;
    private String imagenUrl;
    private Double precioUnitario;
    private Integer cantidad;

    public ItemCarrito() {
    }

    public ItemCarrito(ObjectId productoId, String nombre, String imagenUrl, Double precioUnitario, Integer cantidad) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }
    
    public Double getSubtotal() {
        return this.precioUnitario * this.cantidad;
    }

    public ObjectId getProductoId() {
        return productoId;
    }

    public void setProductoId(ObjectId productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
