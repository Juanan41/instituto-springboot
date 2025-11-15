package es.juanito.institutos.nombre.models.exceptions;

public class NombreNotFoundException extends RuntimeException {
    public NombreNotFoundException(String message) {
        super(message);
    }
}
