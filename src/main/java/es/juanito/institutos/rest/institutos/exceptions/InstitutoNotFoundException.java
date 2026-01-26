package es.juanito.institutos.rest.institutos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Excepción de instituto no encontrado
 * Status 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class InstitutoNotFoundException extends InstitutoException {

    public InstitutoNotFoundException(Long id) {
        super("Instituto con id " + id + " no encontrado");
    }

    public InstitutoNotFoundException(UUID uuid) {
        super("Instituto con uuid " + uuid + " no encontrado");
    }

    // ✅ CONSTRUCTOR NECESARIO PARA MENSAJES PERSONALIZADOS
    public InstitutoNotFoundException(String message) {
        super(message);
    }
}



