package es.juanito.institutos.rest.estudiantes.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteResponseDto {
    private Long id;
    private UUID uuid;
    private String username;
    private String email;
    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private Boolean isDeleted;
    private Long institutoId;
}