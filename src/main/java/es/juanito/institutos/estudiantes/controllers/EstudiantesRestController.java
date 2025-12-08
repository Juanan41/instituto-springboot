package es.juanito.institutos.estudiantes.controllers;

import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.exceptions.EstudianteNotFoundException;
import es.juanito.institutos.estudiantes.services.EstudianteService;
import es.juanito.institutos.pagination.utils.PaginationLinksUtils;
import es.juanito.institutos.pagination.utils.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de estudiantes del tipo RestController
 * Fijamos la ruta de acceso a este controlador
 * Usamos el servicio de estudiantes y lo inyectamos en el constructor con RequiredArgsConstructor

 * #@RequiredArgsConstructor es una anotación Lombok que nos permite inyectar dependencias basadas
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
    // AÑADIMOS la inyección de la utilidad de Links
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Obtiene todos los estudiantes con paginación y filtros opcionales.
     *
     * #@param codigoInstituto Código del instituto (opcional)
     * #@param nombre Nombre del estudiante (opcional)
     * #@param "page" Número de página (default 0)
     * #@param size Tamaño de la página (default 10)
     * #@param sortBy Campo de ordenación (default nombre)
     * #@param direction Dirección de ordenación (default asc)
     * #@param uriBuilder Constructor de URI, inyectado por Spring.
     * @return ResponseEntity con PageResponse<EstudianteRequestDto> en el cuerpo y Link Header.
     */
    @GetMapping()
    public ResponseEntity<PageResponse<EstudianteRequestDto>> getAll(
            @RequestParam(required = false) String codigoInstituto,
            @RequestParam(required = false) String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            UriComponentsBuilder uriBuilder
    ) {
        log.info("Buscando estudiantes paginados con filtros. codigoInstituto={}, nombre={}, page={}, size={}", codigoInstituto, nombre, page, size);

        // 1. Preparamos el objeto Pageable
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        PageRequest pageable = PageRequest.of(page, size, sort);

        // 2. Llamamos al servicio (¡El servicio debe devolver Page<EstudianteRequestDto>!)
        Page<EstudianteRequestDto> pageDto = estudiantesService.findAll(codigoInstituto, nombre, pageable);

        // 3. Creamos el Link Header
        String linkHeader = paginationLinksUtils.createLinkHeader(pageDto, uriBuilder);

        // 4. Creamos el cuerpo de la respuesta usando tu PageResponse
        PageResponse<EstudianteRequestDto> responseBody =
                PageResponse.of(pageDto, sortBy, direction);

        // 5. Devolvemos la respuesta con el Header y el cuerpo
        return ResponseEntity.ok()
                .header("Link", linkHeader) // <- ¡Aquí se añade el Header!
                .body(responseBody);
    }
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