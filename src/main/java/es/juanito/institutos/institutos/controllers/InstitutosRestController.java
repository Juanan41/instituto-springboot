package es.juanito.institutos.institutos.controllers;

import es.juanito.institutos.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.institutos.exceptions.InstitutoNotFoundException;
import es.juanito.institutos.institutos.services.InstitutosService;
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
@RequestMapping("api/${API_VERSION:v1}/institutos")

public class InstitutosRestController {
    // Servicio de institutos
    private final InstitutosService institutosService;



    /**
     - Obtiene todas las tarjetas
     - @param numero    Número de la tarjeta
     - @param titular   Titular de la tarjeta
     - @return Lista de tarjetas
     */

    @GetMapping
    public ResponseEntity<List<InstitutoResponseDto>> getAll(@RequestParam(required = false) String ciudad,
                                                            @RequestParam(required = false) String nombre) {
        log.info("Buscando institutos por ciudad={}, nombre={}", ciudad, nombre);
        return ResponseEntity.ok(institutosService.findAll(ciudad, nombre));
    }

    /**
     * Obtiene una instituto por su id

     param id del instituto, se pasa como parámetro de la URL /{id}
     * return InstitutoResponseDto si existe
     * throws Instituto si no existe el instituto (404)
     */

    @GetMapping("/{id}")
    public ResponseEntity<InstitutoResponseDto> getById(@PathVariable Long id) {
        log.info("Buscando Instituto por id {}", id);
        return ResponseEntity.ok(institutosService.findById(id));

    }
    /**
     * Crear un instituto

     * param institutoCreateDto a crear
     * return InstitutoResponseDto creada
     * throws InstitutoBadRequestException si el instituto no es correcto (400)
     */


    @PostMapping
    public ResponseEntity<InstitutoResponseDto> crearInstituto(@Valid @RequestBody InstitutoCreateDto institutoCreateDto) {
        log.info("Creando Instituto {}", institutoCreateDto);
        var saved= institutosService.save(institutoCreateDto); // Llama al método existente
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Actualiza un instituto

     * param id      del instituto a actualizar
     * param institutoUpdateDto con los datos a actualizar
     * return InstitutoResponseDto actualizado
     * throws InstitutoNotFoundException si no existe el instituto (404)
     * throws InstitutoBadRequestException si el instituto no es correcto (400)
     */


    @PutMapping("/{id}")
    public ResponseEntity<InstitutoResponseDto> update(@PathVariable Long id,@Valid @RequestBody InstitutoUpdateDto institutoUpdateDto) {
        log.info("Actualizando instituto id={} con instituto={}", id, institutoUpdateDto);
        return ResponseEntity.ok(institutosService.update(id, institutoUpdateDto));
    }
    /**
     * Actualiza parcialmente el instituto

     * param id      de la instituto a actualizar
     * param institutoUpdateDto con los datos a actualizar
     * return Instituto actualizada
     * throws InstitutoNotFoundException si no existe el instituto (404)
     * throws InstitutoBadRequestException si el instituto no es correcto (400)
     */

    @PatchMapping("/{id}")
    public ResponseEntity<InstitutoResponseDto> updatePartial(@PathVariable Long id,@Valid @RequestBody InstitutoUpdateDto institutoUpdateDto) {
        log.info("Actualizando parcialmente instituto con id={} con instituto={}",id, institutoUpdateDto);
        return ResponseEntity.ok(institutosService.update(id, institutoUpdateDto));
    }
    /**
     * Borra una tarjeta por su id

     * param id de el instituto a borrar
     * return ResponseEntity con status 204 No Content si se ha conseguido borrar
     * throws InstitutoNotFoundException si no existe el instituto (404)
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando instituto por id: {}", id);
        institutosService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

   /*

      -Manejador de excepciones de Validación: 400 Bad Request
      -param ex excepción
      -return Mapa de errores de validación con el campo y el mensaje
     */


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setDetail("Falló la validación para el objeto='" + result.getObjectName()
                + "'. Núm. errores: " + result.getErrorCount());

        Map<String, String> errores = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        problemDetail.setProperty("errores", errores);

        return problemDetail;
    }

    // Maneja excepciones de instituto no encontrado (404 Not Found)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InstitutoNotFoundException.class)
    public ProblemDetail handleInstitutoNotFoundException(InstitutoNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }




}
