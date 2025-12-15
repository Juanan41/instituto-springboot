package es.juanito.institutos.estudiantes.controllers;

import es.juanito.institutos.estudiantes.dto.EstudianteInfoResponseDto;
import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.dto.EstudianteResponseDto;
import es.juanito.institutos.estudiantes.exceptions.EstudianteNotFoundException;
import es.juanito.institutos.estudiantes.services.EstudianteService;
import es.juanito.institutos.pagination.utils.PageResponse;
import es.juanito.institutos.pagination.utils.PaginationLinksUtils;
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
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/${api.version}/estudiantes")
public class EstudiantesRestController {

    private final EstudianteService estudiantesService;
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Obtiene todos los estudiantes con paginación y filtros opcionales.
     */
    @GetMapping
    public ResponseEntity<PageResponse<EstudianteResponseDto>> getAll(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            UriComponentsBuilder uriBuilder
    ) {
        log.info("Buscando estudiantes paginados con filtros. username={}, email={}, page={}, size={}", username, email, page, size);

        // Pageable
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        PageRequest pageable = PageRequest.of(page, size, sort);

        // Llamada al servicio usando Optional
        Page<EstudianteResponseDto> pageDto = estudiantesService.findAll(
                Optional.ofNullable(username),
                Optional.ofNullable(email),
                Optional.ofNullable(isDeleted),
                pageable
        );

        // Link header
        String linkHeader = paginationLinksUtils.createLinkHeader(pageDto, uriBuilder);

        // PageResponse
        PageResponse<EstudianteResponseDto> responseBody = PageResponse.of(pageDto, sortBy, direction);

        return ResponseEntity.ok()
                .header("Link", linkHeader)
                .body(responseBody);
    }

    /**
     * Obtiene un estudiante por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstudianteInfoResponseDto> getById(@PathVariable Long id) {
        log.info("Buscando estudiante por id={}", id);
        EstudianteInfoResponseDto estudiante = estudiantesService.findById(id);
        return ResponseEntity.ok(estudiante);
    }

    /**
     * Crea un nuevo estudiante.
     */
    @PostMapping
    public ResponseEntity<EstudianteResponseDto> create(@Valid @RequestBody EstudianteRequestDto estudianteRequestDto) {
        log.info("Creando estudiante: {}", estudianteRequestDto);
        EstudianteResponseDto created = estudiantesService.save(estudianteRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualiza un estudiante existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody EstudianteRequestDto estudianteRequestDto
    ) {
        log.info("Actualizando estudiante id={} con datos={}", id, estudianteRequestDto);
        EstudianteResponseDto updated = estudiantesService.update(id, estudianteRequestDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Actualización parcial (PATCH).
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EstudianteResponseDto> updatePartial(
            @PathVariable Long id,
            @Valid @RequestBody EstudianteRequestDto estudianteRequestDto
    ) {
        log.info("Actualizando parcialmente estudiante id={} con datos={}", id, estudianteRequestDto);
        EstudianteResponseDto updated = estudiantesService.update(id, estudianteRequestDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Borra un estudiante (soft delete).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando estudiante id={}", id);
        estudiantesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Manejo de excepciones de validación (400 Bad Request).
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        BindingResult result = ex.getBindingResult();
        problemDetail.setDetail("Falló la validación para el objeto='" + result.getObjectName() + "'. Núm. errores: " + result.getErrorCount());

        Map<String, String> errores = new HashMap<>();
        result.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });

        problemDetail.setProperty("errores", errores);
        return problemDetail;
    }
}
