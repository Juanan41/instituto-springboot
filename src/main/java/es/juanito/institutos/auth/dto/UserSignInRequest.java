package es.juanito.institutos.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email; // Añadida por si se usa email para el login

/**
 * DTO (Data Transfer Object) para la petición de inicio de sesión.
 * Contiene el nombre de usuario/email y la contraseña.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInRequest {

    // Si usas el email como nombre de usuario, es mejor usar @Email
    // Si usas un campo 'username' separado, puedes quitar @Email
    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;
}
