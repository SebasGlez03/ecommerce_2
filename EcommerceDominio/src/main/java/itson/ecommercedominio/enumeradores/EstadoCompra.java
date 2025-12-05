/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package itson.ecommercedominio.enumeradores;

/**
 *
 * @author Beto_
 */
public enum EstadoCompra {
    PENDIENTE("Pendiente de Envío"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String valor;

    EstadoCompra(String valor) {
        this.valor = valor;
    }

    /**
     * Este método lo usaremos para el JSP para que se va bonito
     * @return el texto bonito
     */
    public String getValor() {
        return valor;
    }
}
