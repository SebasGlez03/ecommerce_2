/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Docs;

import java.time.LocalDate;
import org.bson.types.ObjectId;

/**
 *
 * @author santi
 */
public class Pedido {
    
    private ObjectId id;
    private LocalDate fechaCompra;
    private EstadoPedido estadoPedido;
    private String direccionEnvio;
    private double total;

    public Pedido() {
    }

    public Pedido(ObjectId id, LocalDate fechaCompra, EstadoPedido estadoPedido, String direccionEnvio, double total) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.estadoPedido = estadoPedido;
        this.direccionEnvio = direccionEnvio;
        this.total = total;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pedido{" + "id=" + id + ", fechaCompra=" + fechaCompra + ", estadoPedido=" + estadoPedido + ", direccionEnvio=" + direccionEnvio + ", total=" + total + '}';
    }
    
}
