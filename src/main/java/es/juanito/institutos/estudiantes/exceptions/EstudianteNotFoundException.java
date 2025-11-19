package es.juanito.institutos.estudiante.exceptions;

public class EstudianteNotFoundException extends RuntimeException {
    public EstudianteNotFoundException(Long id) {
        super("Estudiante con id=" + id + " no encontrado.");
    }

    public EstudianteNotFoundException(String mensaje) {
        super(mensaje);
    }
}

