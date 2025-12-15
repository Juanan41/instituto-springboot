package es.juanito.institutos.users.dto;

import es.juanito.institutos.users.models.Role; // Importación de la clase Role
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto { // Nombre DTO estandarizado

    // --- Campos de Información Personal ---

    @NotBlank(message = "Nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Apellidos no puede estar vacío")
    private String apellidos;

    // --- Campos de Autenticación / Acceso ---

    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Size(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password; // Se pasa sin codificar

    // --- Campos de Seguridad / Control ---

    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

    @Builder.Default
    private Boolean isDeleted = false;
}