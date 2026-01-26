package es.juanito.institutos.rest.auth.exceptions;

/**
 * Clase base abstracta para todas las excepciones de autenticación personalizadas.
 * Permite capturar y manejar de forma genérica todos los errores relacionados con Auth.
 */
public abstract class AuthException extends RuntimeException {

    // Se recomienda añadir un serialVersionUID para clases serializables
    private static final long serialVersionUID = 1L;

    /**
     * Constructor que acepta un mensaje de error.
     * @param message Mensaje que describe la causa del error.
     */
    public AuthException(String message) {
        super(message);
    }
}