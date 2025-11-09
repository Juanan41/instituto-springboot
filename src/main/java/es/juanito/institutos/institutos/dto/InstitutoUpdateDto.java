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
public class InstitutoUpdateDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
    private  String nombre;
    private  String ciudad;
    @NotBlank(message = "La dirección es obligatoria")
    private  String direccion;
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{2}-\\d{2}", message = "El teléfono debe tener 9 dígitos y  un guion separándolos,ejem:999-99-99-99")
    private  String telefono;
    @Email(message = "El correo no tiene un formato válido")
    private  String email;
    private  Integer numeroEstudiantes;
    private  Integer numeroProfesores;
    private  String tipo;
    private LocalDate anioFundacion;
    @InstitutoCode
    private  String codigoInstituto;

}

