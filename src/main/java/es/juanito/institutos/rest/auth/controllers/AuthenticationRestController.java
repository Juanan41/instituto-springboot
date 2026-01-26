package es.juanito.institutos.rest.auth.controllers;

import es.juanito.institutos.rest.auth.dto.JwtAuthResponse;
import es.juanito.institutos.rest.auth.dto.UserSignInRequest;
import es.juanito.institutos.rest.auth.dto.UserSignUpRequest;
import es.juanito.institutos.rest.auth.services.authentication.AuthenticationService;
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
import java.util.Map;

/**
 * Controlador REST para manejar las operaciones de autenticación (registro e inicio de sesión).
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/${api.version}/auth") // Ruta base: ej. /api/v1/auth
public class AuthenticationRestController {
 private final AuthenticationService authenticationService;

 /*
    * Registra un usuario (Estudiante/Instituto).
    *
    * @param request datos del usuario
    * @return Token de autenticación
*/
@PostMapping("/signup")
public ResponseEntity<JwtAuthResponse> signUp(@Valid @RequestBody UserSignUpRequest request) {
 // Se recomienda registrar el email en lugar del objeto completo para evitar exponer la contraseña en logs
     log.info("PETICIÓN: Registrando usuario con email: {}", request.getEmail());
     return ResponseEntity.ok(authenticationService.signUp(request));
}

/*
 * Inicia sesión de un usuario
 *
 * @param request datos del usuario
 * @return Token de autenticación
 */
@PostMapping("/signin")
public ResponseEntity<JwtAuthResponse> signIn(@Valid @RequestBody UserSignInRequest request) {
    log.info("PETICIÓN: Iniciando sesión de usuario: {}", request.getUsername()); // CORREGIDO
    return ResponseEntity.ok(authenticationService.signIn(request));
}

    /*
* Manejador de excepciones de Validación: 400 Bad Request
* Transforma el error de validación (MethodArgumentNotValidException) a un ProblemDetail.
*
* @param ex excepción
* @return ProblemDetail con los errores de campo y su mensaje
*/
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(MethodArgumentNotValidException.class)
public ProblemDetail handleValidationExceptions(
     MethodArgumentNotValidException ex) {

     ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

     BindingResult result = ex.getBindingResult();
     problemDetail.setDetail("Falló la validación para el objeto='" + result.getObjectName()
                     + "'. " + "Número de errores: " + result.getErrorCount());

     Map<String, String> errores = new HashMap<>();
     result.getAllErrors().forEach((error) -> {
     // Se usa FieldError para obtener el nombre del campo que falló
     if (error instanceof FieldError fieldError) {
            errores.put(fieldError.getField(), error.getDefaultMessage());
     } else {
         // Captura errores a nivel de clase si existen
             errores.put(error.getObjectName(), error.getDefaultMessage());
             }
     });

     problemDetail.setProperty("errores", errores);
     return problemDetail;
}
}