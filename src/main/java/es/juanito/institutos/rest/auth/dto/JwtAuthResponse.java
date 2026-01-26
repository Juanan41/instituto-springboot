package es.juanito.institutos.rest.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la respuesta de autenticación.
 * Contiene el token JWT generado tras el inicio de sesión o registro exitoso.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {

    private String token;

    /**
     * Tipo de token de autenticación (generalmente "Bearer").
     */
    private String tipo = "Bearer";
}