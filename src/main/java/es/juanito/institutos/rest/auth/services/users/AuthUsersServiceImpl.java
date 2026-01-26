package es.juanito.institutos.rest.auth.services.users;

import es.juanito.institutos.rest.auth.repositories.AuthUsersRepository; // Adaptado
import es.juanito.institutos.rest.users.exceptions.UserNotFound; // Asumo esta ruta para la excepción
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de carga de usuarios (UserDetailsService) para Spring Security.
 * Recupera los detalles de un usuario a partir del repositorio.
 */
@RequiredArgsConstructor
@Service("userDetailsService") // Nombre de bean usado por Spring Security
public class AuthUsersServiceImpl implements AuthUsersService {

 private final AuthUsersRepository authUsersRepository;

 /*
* Carga un usuario a partir de su nombre de usuario (o email) para Spring Security.
* @param username El nombre de usuario.
* @return Los detalles del usuario (User, que debe implementar UserDetails).
* @throws UserNotFound Si el usuario no es encontrado.
*/
@Override
public UserDetails loadUserByUsername(String username) throws UserNotFound {
     // Busca el usuario en la base de datos por el nombre de usuario
     return authUsersRepository.findByUsername(username)
     .orElseThrow(() -> new UserNotFound("Usuario con username " + username + " no encontrado"));
    }
}