package es.juanito.institutos.rest.auth.services.authentication;

import es.juanito.institutos.rest.auth.dto.JwtAuthResponse;
import es.juanito.institutos.rest.auth.dto.UserSignInRequest;
import es.juanito.institutos.rest.auth.dto.UserSignUpRequest;
import es.juanito.institutos.rest.auth.exceptions.AuthDifferentPasswords;
import es.juanito.institutos.rest.auth.exceptions.AuthExistingUsernameOrEmail;
import es.juanito.institutos.rest.auth.exceptions.AuthSignInNotValid;
import es.juanito.institutos.rest.auth.repositories.AuthUsersRepository;
import es.juanito.institutos.rest.auth.services.jwt.JwtService;
import es.juanito.institutos.rest.users.models.Role; // Adaptado
import es.juanito.institutos.rest.users.models.User; // Adaptado
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Implementación del servicio de autenticación. Contiene la lógica para el registro
 * de usuarios y la generación de tokens JWT.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
     private final AuthUsersRepository authUsersRepository;
     private final PasswordEncoder passwordEncoder;
     private final JwtService jwtService;
     private final AuthenticationManager authenticationManager;
 /*
* Registra un usuario
*
* @param request datos del usuario
* @return Token de autenticación
*/

@Override
public JwtAuthResponse signUp(UserSignUpRequest request) {
     log.info("PETICIÓN DE REGISTRO: Creando usuario: {}", request.getUsername());

     // 1. Verificar coincidencia de contraseñas
     if (!request.getPassword().equals(request.getPasswordComprobacion())) {
     throw new AuthDifferentPasswords("Las contraseñas no coinciden");
     }

 // 2. Crear objeto User y codificar la contraseña
     User user = User.builder()
         .username(request.getUsername())
         .password(passwordEncoder.encode(request.getPassword()))
         .email(request.getEmail())
         .nombre(request.getNombre())
         .apellidos(request.getApellidos())
         // Asignar rol por defecto, si es necesario (ej. Role.USER)
             .roles(Set.of(Role.USER)) // Usamos Set.of() en lugar de Stream.of().collect()
             .build();

 // 3. Guardar el usuario y manejar errores de unicidad (username/email)
    try {
     var userStored = authUsersRepository.save(user);
     // 4. Generar y devolver el token JWT
     return JwtAuthResponse.builder().token(jwtService.generateToken(userStored)).build();
     } catch (DataIntegrityViolationException ex) {
     // Se lanza esta excepción si el username o email ya existen
     throw new AuthExistingUsernameOrEmail("El usuario con username o email ya existe");
     }
}

/**
* Autentica un usuario
*
* @param request datos del usuario
* @return Token de autenticación
*/
@Override
public JwtAuthResponse signIn(UserSignInRequest request) {
 log.info("PETICIÓN DE LOGIN: Autenticando usuario: {}", request.getUsername());

 try {
     // 1. Autenticar el usuario mediante Spring Security
     authenticationManager.authenticate(
         new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
 }   catch (Exception ex) {
     // Si la autenticación falla (credenciales incorrectas), lanzamos una excepción 404
     throw new AuthSignInNotValid("Usuario o contraseña incorrectos");
 }

 // 2. Si la autenticación es exitosa, buscamos el objeto User para generar el token
 var user = authUsersRepository.findByUsername(request.getUsername())
    .orElseThrow(() -> new AuthSignInNotValid("Usuario o contraseña incorrectos"));

 // 3. Generar y devolver el token
 var jwt = jwtService.generateToken(user);
 return JwtAuthResponse.builder().token(jwt).build();
 }
}