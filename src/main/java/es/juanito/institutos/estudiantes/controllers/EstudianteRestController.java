package es.juanito.institutos.estudiante.controllers;

import es.juanito.institutos.estudiante.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiante.exceptions.EstudianteException;
import es.juanito.institutos.estudiante.models.Estudiante;
import es.juanito.institutos.estudiante.services.EstudianteService;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/${api.version}/estudiante")
public class EstudianteRestController {

    private final EstudianteService estudianteService;

    // --- GET all estudiante ---
    @GetMapping
    public ResponseEntity<List<Estudiante>> getAll(@RequestParam(required = false) String codigoInstituto) {
        log.info("Buscando todos los estudiantes con codigoInstituto: {}", codigoInstituto);
        return ResponseEntity.ok(estudianteService.findAll(codigoInstituto));
    }

    // --- GET by id ---
    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> getById(@PathVariable Long id) {
        log.info("Buscando nombre por id={}", id);
        return ResponseEntity.ok(estudianteService.findById(id));
    }

    // --- POST create ---
    @PostMapping
    public ResponseEntity<Estudiante> create(@Valid @RequestBody EstudianteRequestDto estudianteRequestDto) {
        log.info("Creando estudiante: {}", estudianteRequestDto);
        Estudiante saved = estudianteService.save(estudianteRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // --- PUT update ---
    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> update(@PathVariable Long id, @Valid @RequestBody EstudianteRequestDto estudianteRequestDto) {
        log.info("Actualizando estudiante id={} con datos={}", id, estudianteRequestDto);
        return ResponseEntity.ok(estudianteService.update(id, estudianteRequestDto));
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando estudiante por id: {}", id);
        estudianteService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // --- Manejo de validaciones ---
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        BindingResult result = ex.getBindingResult();
        problemDetail.setDetail("Falló la validación para el objeto='" + result.getObjectName()
                + "'. Número de errores: " + result.getErrorCount());

        Map<String, String> errores = new HashMap<>();
        result.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });

        problemDetail.setProperty("errores", errores);
        return problemDetail;
    }

    // --- Manejo de excepciones de negocio ---
    @ExceptionHandler(EstudianteException.class)
    public ResponseEntity<ProblemDetail> handleEstudianteException(EstudianteException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
}