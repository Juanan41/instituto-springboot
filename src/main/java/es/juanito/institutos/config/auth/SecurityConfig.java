package es.juanito.institutos.config.auth;

import es.juanito.institutos.config.auth.SecurityConfig;
import es.juanito.institutos.auth.services.users.AuthUsersService;
import es.juanito.institutos.auth.services.users.AuthUsersServiceImpl; // Usamos la implementación del servicio
import es.juanito.institutos.users.exceptions.UserNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad principal para la aplicación.
 * Define el filtro JWT, las reglas de acceso y los beans de seguridad.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Permite @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // Inyectamos el servicio de usuarios para que Spring sepa dónde buscar usuarios
    private final AuthUsersServiceImpl userDetailsService;

    /**
     * Define la cadena de filtros de seguridad.
     * Configura el manejo de sesiones como STATELESS y añade el filtro JWT.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Rutas públicas: Autenticación (login, registro)
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Rutas privadas: Todas las demás requieren autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // REST API, sin sesiones
                )
                .authenticationProvider(authenticationProvider())
                // Añadimos el filtro JWT antes del filtro de usuario/contraseña de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Define el proveedor de autenticación:
     * Indica a Spring que use nuestro UserDetailsService y el PasswordEncoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Define el codificador de contraseñas (BCrypt es el estándar actual).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define el gestor de autenticación, necesario para el proceso de signIn.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}