package es.juanito.institutos.rest.institutos.controllers;

import es.juanito.institutos.rest.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.rest.institutos.exceptions.InstitutoBadRequestException;
import es.juanito.institutos.rest.institutos.exceptions.InstitutoNotFoundException;
import es.juanito.institutos.rest.institutos.services.InstitutosService;
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
 * Controlador de institutos del tipo RestController
 * Fijamos la ruta de acceso a este controlador
 * Usamos el servicio de institutos y lo inyectamos en el constructor con RequiredArgsConstructor
 *
 * @RequiredArgsConstructor es una anotación Lombok que nos permite inyectar dependencias basadas
 * en las anotaciones @Controller, @Service, @Component, etc.
 * y que se encuentren en nuestro contenedor de Spring
 * con solo declarar las dependencias como final ya que el constructor lo genera Lombok
 */
@Slf4j
@RequiredArgsConstructor
@RestController // Es un controlador Rest
// Se ajusta la variable del path para que coincida con el estilo de la referencia
@RequestMapping("api/${api.version}/institutos")
public class InstitutosRestController {
    // Servicio de institutos
    private final InstitutosService institutosService;

    /**
     * Obtiene todos los institutos
     *
     * @param ciudad Ciudad del instituto
     * @param nombre Nombre del instituto
     * @return Lista de institutos
     */
    @GetMapping()
    public ResponseEntity<List<InstitutoResponseDto>> getAll(@RequestParam(required = false) String ciudad,
                                                             @RequestParam(required = false) String nombre) {
        log.info("Buscando institutos por ciudad={}, nombre={}", ciudad, nombre);
        return ResponseEntity.ok(institutosService.findAll(ciudad, nombre));
    }

    /**
     * Obtiene un instituto por su id
     *
     * @param id del instituto, se pasa como parámetro de la URL /{id}
     * @return InstitutoResponseDto si existe
     * @throws InstitutoNotFoundException si no existe el instituto (404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<InstitutoResponseDto> getById(@PathVariable Long id) {
        log.info("Buscando instituto por id={}", id);
        return ResponseEntity.ok(institutosService.findById(id));
    }

    /**
     * Crear un instituto
     *
     * @param institutoCreateDto a crear
     * @return InstitutoResponseDto creado
     * @throws InstitutoBadRequestException si el instituto no es correcto (400)
     */
    @PostMapping()
    public ResponseEntity<InstitutoResponseDto> create(@Valid @RequestBody InstitutoCreateDto institutoCreateDto) {
        log.info("Creando instituto : {}", institutoCreateDto);
        var saved = institutosService.save(institutoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Actualiza un instituto
     *
     * @param id del instituto a actualizar
     * @param institutoUpdateDto con los datos a actualizar
     * @return InstitutoResponseDto actualizado
     * @throws InstitutoNotFoundException si no existe el instituto (404)
     * @throws InstitutoBadRequestException si el instituto no es correcto (400)
     */
    @PutMapping("/{id}")
    public ResponseEntity<InstitutoResponseDto> update(@PathVariable Long id, @Valid @RequestBody InstitutoUpdateDto institutoUpdateDto) {
        log.info("Actualizando instituto id={} con instituto={}", id, institutoUpdateDto);
        return ResponseEntity.ok(institutosService.update(id, institutoUpdateDto));
    }

    /**
     * Actualiza parcialmente un instituto
     *
     * @param id del instituto a actualizar
     * @param institutoUpdateDto con los datos a actualizar
     * @return InstitutoResponseDto actualizado
     * @throws InstitutoNotFoundException si no existe el instituto (404)
     * @throws InstitutoBadRequestException si el instituto no es correcto (400)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<InstitutoResponseDto> updatePartial(@PathVariable Long id, @Valid @RequestBody InstitutoUpdateDto institutoUpdateDto) {
        log.info("Actualizando parcialmente instituto con id={} con instituto={}", id, institutoUpdateDto);
        return ResponseEntity.ok(institutosService.update(id, institutoUpdateDto));
    }

    /**
     * Borra un instituto por su id
     *
     * @param id del instituto a borrar
     * @return ResponseEntity con status 204 No Content si se ha conseguido borrar
     * @throws InstitutoNotFoundException si no existe el instituto (404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Borrando instituto por id: {}", id);
        institutosService.deleteById(id);
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

    // Nota: Se elimina el manejador de InstitutoNotFoundException (404) para igualar
    // la estructura del controlador de referencia, que delega estas excepciones
    // al manejo global o al mapeo automático de Spring.

}
