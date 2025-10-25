/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 *
 * @author SantiagoSanchez
 */
public class PersistenciaException extends Exception {

    /**
     * Constructor vacío.
     */
    public PersistenciaException() {
    }

    /**
     * Constructor que establece el mensaje de la excepción.
     *
     * @param message de la excepción.
     */
    public PersistenciaException(String message) {
        super(message);
    }

    /**
     * Constructor que establece el mensaje y la causa de la excepción.
     *
     * @param message de la excepción.
     * @param cause de la excepción.
     */
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}
