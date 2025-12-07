package es.juanito.institutos.estudiantes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Mapea a 404 Not Found
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EstudianteNotFoundException extends EstudianteException {

    // Constructor para buscar por ID (el más usado en el servicio)
    public EstudianteNotFoundException(Long id) {
        super("Estudiante con id=" + id + " no encontrado.");
    }

    // Constructor para mensajes personalizados
    public EstudianteNotFoundException(String mensaje) {
        super(mensaje);
    }

    // Opcional: Constructor para UUIDs
    /*
    public EstudianteNotFoundException(UUID uuid) {
        super("Estudiante con UUID=" + uuid.toString() + " no encontrado.");
    }
    */
}