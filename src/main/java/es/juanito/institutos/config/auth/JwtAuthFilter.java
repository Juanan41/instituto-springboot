package es.juanito.institutos.config.auth;

import es.juanito.institutos.auth.services.jwt.JwtService; // Adaptado
import es.juanito.institutos.auth.services.users.AuthUsersService; // Adaptado
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta las peticiones para validar el token JWT en la cabecera
 * "Authorization" y autenticar al usuario si el token es válido.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Inyectamos el servicio JWT y el servicio de detalles de usuario
    private final JwtService jwtService;
    private final AuthUsersService authUsersService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Iniciando el filtro de autenticación");
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        UserDetails userDetails = null;
        String userName = null;

        // 1. Verificar cabecera: No hay cabecera o no empieza por "Bearer "
        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            log.info("No se ha encontrado cabecera de autenticación JWT.");
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer JWT
        log.info("Se ha encontrado cabecera de autenticación, se procesa.");
        jwt = authHeader.substring(7);

        // 3. Extraer usuario del token y manejar posibles fallos (ej. expiración)
        try {
            userName = jwtService.extractUserName(jwt);
        } catch (Exception e) {
            log.warn("Token no válido o expirado: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no autorizado o no válido");
            return;
        }

        log.info("Usuario del token: {}", userName);

        // 4. Autenticar si hay un nombre de usuario y no está ya autenticado en el contexto
        if (StringUtils.hasText(userName)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            log.info("Comprobando usuario y token en el servicio...");

            // Buscar UserDetails
            try {
                userDetails = authUsersService.loadUserByUsername(userName);
            } catch (Exception e) {
                log.warn("Usuario no encontrado en la base de datos: {}", userName);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autorizado");
                return;
            }

            // 5. Validar token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("JWT válido. Estableciendo autenticación para: {}", userName);

                // 6. Crear y establecer la autenticación en el SecurityContext
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            } else {
                log.warn("Token no válido para el usuario: {}", userName);
            }
        }

        // 7. Continuar con la cadena de filtros de Spring Security
        filterChain.doFilter(request, response);
    }
}