package es.juanito.institutos.estudiante.dto;


import es.juanito.institutos.institutos.validators.InstitutoCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class EstudianteRequestDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @InstitutoCode
    private final String codigoInstituto;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 50 caracteres")
    private final String nombre;
    private final Boolean isDeleted;
}
