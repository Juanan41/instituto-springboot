package es.juanito.institutos.rest.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para errores de autenticación
 * cuando el inicio de sesión falla (credenciales incorrectas).
 * Extiende de AuthException y devuelve un código de estado HTTP 404 (Not Found)
 * por razones de seguridad, para no revelar si el usuario existe o no.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthSignInNotValid extends AuthException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor que acepta un mensaje de error.
     * @param message Mensaje que describe la causa del error.
     */
    public AuthSignInNotValid(String message) {
        super(message);
    }
}