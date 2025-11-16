package es.juanito.institutos.nombre.controllers;

import es.juanito.institutos.nombre.dto.NombreRequestDto;
import es.juanito.institutos.nombre.exceptions.NombreException;
import es.juanito.institutos.nombre.models.Nombre;
import es.juanito.institutos.nombre.services.NombreService;
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
@RequestMapping("api/${api.version}/nombres")
public class NombreRestController {

    private final NombreService nombreService;

    // --- GET all nombres ---
    @GetMapping
    public ResponseEntity<List<Nombre>> getAll(@RequestParam(required = false) String codigoInstituto) {
        log.info("Buscando todos los nombres con codigoInstituto: {}", codigoInstituto);
        return ResponseEntity.ok(nombreService.findAll(codigoInstituto));
    }

    // --- GET by id ---
    @GetMapping("/{id}")
    public ResponseEntity<Nombre> getById(@PathVariable Long id) {
        log.info("Buscando nombre por id={}", id);
        return ResponseEntity.ok(nombreService.findById(id));
    }

    // --- POST create ---
    @PostMapping
    public ResponseEntity<Nombre> create(@Valid @RequestBody NombreRequestDto nombreRequestDto) {
        log.info("Creando nombre: {}", nombreRequestDto);
        Nombre saved = nombreService.save(nombreRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // --- PUT update ---
    @PutMapping("/{id}")
    public ResponseEntity<Nombre> update(@PathVariable Long id, @Valid @RequestBody NombreRequestDto nombreRequestDto) {
        log.info("Actualizando nombre id={} con datos={}", id, nombreRequestDto);
        return ResponseEntity.ok(nombreService.update(id, nombreRequestDto));
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando nombre por id: {}", id);
        nombreService.deleteById(id);
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
    @ExceptionHandler(NombreException.class)
    public ResponseEntity<ProblemDetail> handleNombreException(NombreException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
}