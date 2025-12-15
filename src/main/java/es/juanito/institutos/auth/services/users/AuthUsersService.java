package es.juanito.institutos.auth.services.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Interfaz que define el servicio para la carga de usuarios por nombre de usuario.
 * Extiende UserDetailsService, una interfaz clave de Spring Security.
 */
public interface AuthUsersService extends UserDetailsService {

    /**
     * Localiza al usuario basado en el nombre de usuario.
     * Es el método principal utilizado por Spring Security para la autenticación.
     * * @param username El nombre de usuario (o email) con el que se intenta autenticar.
     * @return UserDetails que contiene la información del usuario (roles, password, etc.).
     */
    @Override
    UserDetails loadUserByUsername(String username);
}