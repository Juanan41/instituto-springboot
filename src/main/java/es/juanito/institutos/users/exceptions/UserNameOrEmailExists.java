package es.juanito.institutos.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Código 409
public class UserNameOrEmailExists extends RuntimeException { // Nombre CORREGIDO
    public UserNameOrEmailExists(String message) {
        super(message);
    }
}