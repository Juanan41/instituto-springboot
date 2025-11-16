package es.juanito.institutos.nombre.exceptions;

public class NombreConflictException extends RuntimeException {
    public NombreConflictException(String mensaje) {
        super(mensaje);
    }
}
