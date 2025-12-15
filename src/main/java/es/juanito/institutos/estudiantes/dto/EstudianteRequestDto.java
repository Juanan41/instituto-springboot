package es.juanito.institutos.estudiantes.dto;

import es.juanito.institutos.institutos.validators.InstitutoCode;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor; // Necesario para compatibilidad con @Builder
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor; // Necesario para Jackson (deserialización)
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteRequestDto {

    // --- CAMPOS DE SALIDA / IDENTIFICACIÓN (Se eliminó 'final') ---
    private Long id;
    private UUID uuid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    // --- CAMPOS DE AUTENTICACIÓN (NUEVOS) ---

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Length(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Length(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    // NOTA: Para POST/Creación, se recomienda tener un campo 'passwordComprobacion'
    private String password;

    // --- Campos de Búsqueda/Relación ---
    @NotBlank(message = "El código del instituto no puede estar vacío")
    @InstitutoCode
    private String codigoInstituto;

    // --- Campos de Datos del Estudiante (Se eliminó 'final') ---
    @NotBlank(message = "El nombre no puede estar vacío")
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Length(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
    private String apellidos;

    @Email(message = "El correo no tiene un formato válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El DNI/NIE es obligatorio")
    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "El DNI/NIE debe tener 8 números y 1 letra (ej: 12345678A)")
    private String dni;
}