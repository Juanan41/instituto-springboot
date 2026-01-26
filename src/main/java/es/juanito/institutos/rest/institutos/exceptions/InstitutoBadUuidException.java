package es.juanito.institutos.rest.institutos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InstitutoBadUuidException extends InstitutoException {
    public InstitutoBadUuidException(String uuid) {
        // Se replica el mensaje de la referencia, adaptándolo.
        super("El UUID " + uuid + " no existe en la base de datos");
    }
}
