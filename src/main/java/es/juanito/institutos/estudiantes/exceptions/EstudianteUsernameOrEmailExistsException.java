package es.juanito.institutos.estudiantes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando el nombre de usuario o el email ya existen (código HTTP 400).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EstudianteUsernameOrEmailExistsException extends RuntimeException {

    // Constructor para mensajes personalizados
    public EstudianteUsernameOrEmailExistsException(String mensaje) {
        super(mensaje);
    }

    // Constructor para casos comunes
    public EstudianteUsernameOrEmailExistsException(String username, String email) {
        super("Ya existe un estudiante con username: " + username + " o email: " + email);
    }
}