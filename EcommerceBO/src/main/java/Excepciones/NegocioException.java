
package Excepciones;

/**
 *
 * @author Equipo 2
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
