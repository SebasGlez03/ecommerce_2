/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercedominio.dtos;

import itson.ecommercedominio.DetalleCompra;
import itson.ecommercedominio.enumeradores.EstadoCompra;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Beto_
 */
public class CompraDTO {
    private ObjectId id;
    private ObjectId usuarioId;
    private Date fechaCompra;
    private EstadoCompra estado;
    private Double total;
    private String direccionEnvio;
    private String metodoPago;
    private List<DetalleCompraDTO> detalles;

    public CompraDTO() {
    }

    public CompraDTO(ObjectId usuarioId, Date fechaCompra, EstadoCompra estado, Double total, String direccionEnvio, String metodoPago, List<DetalleCompraDTO> detalles) {
        this.usuarioId = usuarioId;
        this.fechaCompra = fechaCompra;
        this.estado = estado;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
        this.detalles = detalles;
    }
    
    public CompraDTO (ObjectId usuarioId, String direccionEnvio, String metodoPago, Double total, List<DetalleCompraDTO> detalles) {
        this.usuarioId = usuarioId;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
        this.total = total;
        this.detalles = detalles;

        // Inicializados aquí
        this.fechaCompra = new Date(); 
        this.estado = EstadoCompra.PENDIENTE; 
    } 

    public CompraDTO(ObjectId id, ObjectId usuarioId, Date fechaCompra, EstadoCompra estado, Double total, String direccionEnvio, String metodoPago, List<DetalleCompraDTO> detalles) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fechaCompra = fechaCompra;
        this.estado = estado;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
        this.detalles = detalles;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(ObjectId usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleCompraDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompraDTO> detalles) {
        this.detalles = detalles;
    }
}
