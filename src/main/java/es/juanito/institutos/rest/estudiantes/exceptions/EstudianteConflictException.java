package es.juanito.institutos.rest.estudiantes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Mapea a 409 Conflict
@ResponseStatus(HttpStatus.CONFLICT)
public class EstudianteConflictException extends EstudianteException {
    public EstudianteConflictException(String mensaje) {
        super(mensaje);
    }
}