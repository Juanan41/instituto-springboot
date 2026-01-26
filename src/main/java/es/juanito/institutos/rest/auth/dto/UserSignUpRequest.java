package es.juanito.institutos.rest.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DTO (Data Transfer Object) para la petición de registro de un nuevo usuario.
 * Incluye validaciones básicas para asegurar la calidad de los datos de entrada.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequest {

    @NotBlank(message = "El Nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Los Apellidos no pueden estar vacíos")
    private String apellidos;

    @NotBlank(message = "El Username no puede estar vacío")
    private String username;

    // Se utiliza una expresión regular básica, aunque el validador @Email ya es potente
    @Email(regexp = ".*@.*\\..*", message = "El Email debe ser válido")
    @NotBlank(message = "El Email no puede estar vacío")
    private String email;

    @NotBlank(message = "La Contraseña no puede estar vacía")
    @Length(min = 5, message = "La Contraseña debe tener al menos 5 caracteres")
    private String password;

    @NotBlank(message = "La Contraseña de comprobación no puede estar vacía")
    @Length(min = 5, message = "La Contraseña de comprobación debe tener al menos 5 caracteres")
    private String passwordComprobacion;

    // NOTA: Para una validación estricta de que password y passwordComprobacion coincidan,
    // se recomienda usar una anotación de validación a nivel de clase o validación manual
    // en el servicio.
}