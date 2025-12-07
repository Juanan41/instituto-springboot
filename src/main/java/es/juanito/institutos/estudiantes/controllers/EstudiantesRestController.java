package es.juanito.institutos.estudiantes.controllers;

import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.exceptions.EstudianteNotFoundException;
import es.juanito.institutos.estudiantes.services.EstudianteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de estudiantes del tipo RestController
 * Fijamos la ruta de acceso a este controlador
 * Usamos el servicio de estudiantes y lo inyectamos en el constructor con RequiredArgsConstructor
 *
 * @RequiredArgsConstructor es una anotación Lombok que nos permite inyectar dependencias basadas
 * en las anotaciones @Controller, @Service, @Component, etc.
 * y que se encuentren en nuestro contenedor de Spring
 * con solo declarar las dependencias como final ya que el constructor lo genera Lombok
 */
@Slf4j
@RequiredArgsConstructor
@RestController // Es un controlador Rest
@RequestMapping("api/${api.version}/estudiantes") // Es la ruta del controlador
public class EstudiantesRestController {
    // Servicio de estudiantes
    private final EstudianteService estudiantesService;

    /**
     * Obtiene todos los estudiantes
     *
     * @param codigoInstituto Código del instituto
     * @param nombre Nombre del estudiante
     * @return Lista de estudiantes
     */
    @GetMapping()
    public ResponseEntity<List<EstudianteRequestDto>> getAll(@RequestParam(required = false) String codigoInstituto,
                                                              @RequestParam(required = false) String nombre) {
        log.info("Buscando estudiantes por codigoInstituto={}, nombre={}", codigoInstituto, nombre);
        return ResponseEntity.ok(estudiantesService.findAll(codigoInstituto, nombre));
    }

    /**
     * Obtiene un estudiante por su id
     *
     * @param id del estudiante, se pasa como parámetro de la URL /{id}
     * @return EstudianteResponseDto si existe
     * @throws EstudianteNotFoundException si no existe el estudiante (404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstudianteRequestDto> getById(@PathVariable Long id) {
        log.info("Buscando estudiante por id={}", id);
        return ResponseEntity.ok(estudiantesService.findById(id));
    }

    /**
     * Crear un estudiante
     *
     * @param estudianteRequestDto a crear
     * @return EstudianteResponseDto creado
     */
    @PostMapping()
    public ResponseEntity<EstudianteRequestDto> create(@Valid @RequestBody EstudianteRequestDto estudianteRequestDto) {
        log.info("Creando estudiante : {}", estudianteRequestDto);
        var saved = estudiantesService.save(estudianteRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Actualiza un estudiante
     *
     * @param id del estudiante a actualizar
     * @param estudianteRequestDto con los datos a actualizar
     * @return EstudianteResponseDto actualizado
     * @throws EstudianteNotFoundException si no existe el estudiante (404)

     */
    @PutMapping("/{id}")
    public ResponseEntity<EstudianteRequestDto> update(@PathVariable Long id, @Valid @RequestBody EstudianteRequestDto estudianteRequestDto) {
        log.info("Actualizando estudiante id={} con estudiante={}", id, estudianteRequestDto);
        return ResponseEntity.ok(estudiantesService.update(id, estudianteRequestDto));
    }

    /**
     * Actualiza parcialmente un estudiante
     *
     * @param id del estudiante a actualizar
     * @param estudianteRequestDto con los datos a actualizar
     * @return EstudianteResponseDto actualizado
     * @throws EstudianteNotFoundException si no existe el estudiante (404)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EstudianteRequestDto> updatePartial(@PathVariable Long id, @Valid @RequestBody EstudianteRequestDto estudianteRequestDto) {
        log.info("Actualizando parcialmente estudiante con id={} con estudiante={}", id, estudianteRequestDto);
        // NOTA: Depende del servicio si el DTO de Request puede manejar actualizaciones parciales,
        // pero la firma del controlador se mantiene idéntica a PUT.
        return ResponseEntity.ok(estudiantesService.update(id, estudianteRequestDto));
    }

    /**
     * Borra un estudiante por su id
     *
     * @param id del estudiante a borrar
     * @return ResponseEntity con status 204 No Content si se ha conseguido borrar
     * @throws EstudianteNotFoundException si no existe el estudiante (404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando estudiante por id: {}", id);
        estudiantesService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * Manejador de excepciones de Validación: 400 Bad Request
     *
     * @param ex excepción
     * @return Mapa de errores de validación con el campo y el mensaje
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        BindingResult result = ex.getBindingResult();
        problemDetail.setDetail("Falló la validación para el objeto='" + result.getObjectName()
                + "'. " + "Núm. errores: " + result.getErrorCount());

        Map<String, String> errores = new HashMap<>();
        result.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });

        problemDetail.setProperty("errores", errores);
        return problemDetail;
    }
}