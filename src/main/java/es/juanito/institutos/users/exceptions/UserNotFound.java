package es.juanito.institutos.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Código 404
public class UserNotFound extends RuntimeException { // Nombre CORREGIDO

    // Constructor para buscar por ID
    public UserNotFound(Long id) {
        super("Usuario con ID " + id + " no encontrado");
    }

    // Constructor para buscar por username/email
    public UserNotFound(String search) {
        super("Usuario con username o email " + search + " no encontrado");
    }
}