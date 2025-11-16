package es.juanito.institutos.nombre.exceptions;

public class NombreNotFoundException extends RuntimeException {
    public NombreNotFoundException(Long id) {
        super("Nombre con id=" + id + " no encontrado.");
    }

    public NombreNotFoundException(String mensaje) {
        super(mensaje);
    }
}

