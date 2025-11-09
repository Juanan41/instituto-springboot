package es.juanito.institutos.institutos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Excepción de tarjeta no encontrada
 * Status 404
 */

public class InstitutoNotFoundException extends InstitutoException {

    public InstitutoNotFoundException(Long id) {
        super("Instituto con id " + id + " no encontrado");
    }

    public InstitutoNotFoundException(UUID uuid) {
        super("Instituto con id " + uuid + " no encontrado");
    }
}


