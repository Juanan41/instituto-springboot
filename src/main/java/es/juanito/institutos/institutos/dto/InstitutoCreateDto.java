package es.juanito.institutos.institutos.dto;

import es.juanito.institutos.institutos.validators.InstitutoCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class InstitutoCreateDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private final String nombre;
    private final String ciudad;
    @NotBlank(message = "La dirección es obligatoria")
    private final String direccion;
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{2}-\\d{2}", message =
            "El teléfono debe tener 9 dígitos y  un guion separándolos,ejem:999-99-99-99")
    private final String telefono;
    @Email(message = "El correo no tiene un formato válido")
    private final String email;
    private final Integer numeroEstudiantes;
    private final Integer numeroProfesores;
    private final String tipo;
    private final LocalDate anioFundacion;
    @InstitutoCode
    private final String codigoInstituto; // Campo de validación
}

