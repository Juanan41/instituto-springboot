package es.juanito.institutos.estudiantes.exceptions;

// Clase base que no lleva @ResponseStatus, ya que es genérica.
public class EstudianteException extends RuntimeException {
    public EstudianteException(String mensaje) {
        super(mensaje);
    }
}
