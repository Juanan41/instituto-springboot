package es.juanito.institutos.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para errores de autenticación
 * cuando las contraseñas no coinciden durante el registro.
 * Devuelve un código de estado HTTP 400 (Bad Request).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthDifferentPasswords extends RuntimeException {

    // Se recomienda añadir un serialVersionUID para clases serializables
    private static final long serialVersionUID = 1L;

    /**
     * Constructor que acepta un mensaje de error.
     * @param message Mensaje que describe la causa del error.
     */
    public AuthDifferentPasswords(String message) {
        super(message);
    }
}
