package es.juanito.institutos.auth.services.authentication;

import es.juanito.institutos.auth.dto.JwtAuthResponse; // Importación adaptada
import es.juanito.institutos.auth.dto.UserSignInRequest; // Importación adaptada
import es.juanito.institutos.auth.dto.UserSignUpRequest; // Importación adaptada

/**
 * Interfaz de servicio que define las operaciones disponibles para la autenticación
 * (registro e inicio de sesión).
 */
public interface AuthenticationService {

    /**
     * Registra un nuevo usuario en el sistema.
     * @param request Datos de registro del usuario.
     * @return El token JWT de autenticación.
     */
    JwtAuthResponse signUp(UserSignUpRequest request);

    /**
     * Inicia sesión para un usuario existente.
     * @param request Credenciales del usuario.
     * @return El token JWT de autenticación.
     */
    JwtAuthResponse signIn(UserSignInRequest request);
}