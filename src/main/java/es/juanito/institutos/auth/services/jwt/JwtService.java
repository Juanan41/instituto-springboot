package es.juanito.institutos.auth.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interfaz de servicio para la gestión de Tokens Web JSON (JWT).
 * Define los métodos necesarios para extraer información del token,
 * generarlo y validar su estado.
 */
public interface JwtService {

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     * @param token El token JWT.
     * @return El nombre de usuario (ej. email o username).
     */
    String extractUserName(String token);

    /**
     * Genera un nuevo token JWT para el usuario proporcionado.
     * @param userDetails Los detalles del usuario (debe extender UserDetails).
     * @return El token JWT generado.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Verifica si un token JWT es válido para un usuario específico.
     * @param token El token JWT a validar.
     * @param userDetails Los detalles del usuario.
     * @return true si el token es válido y no ha expirado.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}