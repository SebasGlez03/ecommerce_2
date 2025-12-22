/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.ecommercenegocio.excepciones;

/**
 *
 * @author LENOVO
 */
public class NegocioException extends Exception{
    /**
     * Construye una instancia de NegocioException sin un mensaje específico.
     */
    public NegocioException() {
    }

    public NegocioException(String mensaje) {
        super(mensaje);
    }

    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
