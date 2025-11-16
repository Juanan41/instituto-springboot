package es.juanito.institutos.nombre.dto;


import es.juanito.institutos.institutos.validators.InstitutoCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class NombreRequestDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @InstitutoCode
    private final String codigoInstituto;
    private final Boolean isDeleted;
}
