package es.juanito.institutos.institutos.dto;

import es.juanito.institutos.institutos.validators.InstitutoCode;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class InstitutoCreateDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private final String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    private final String ciudad;

    @NotBlank(message = "La dirección es obligatoria")
    private final String direccion;

    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{2}-\\d{2}", message = "El teléfono debe tener el formato 999-99-99-99")
    private final String telefono;

    @Email(message = "El correo no tiene un formato válido")
    @NotBlank(message = "El email es obligatorio")
    private final String email;

    @NotNull(message = "El número de profesores es obligatorio")
    private final Integer numeroProfesores;

    @NotBlank(message = "El tipo es obligatorio")
    private final String tipo;

    @NotNull(message = "El año de fundación es obligatorio")
    private final LocalDate anioFundacion;

    @InstitutoCode
    private final String codigoInstituto;
}
