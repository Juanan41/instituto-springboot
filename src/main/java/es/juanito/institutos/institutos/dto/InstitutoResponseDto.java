package es.juanito.institutos.institutos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutoResponseDto {
    private  Long id;
    private  String nombre;
    private  String ciudad;
    private  String direccion;
    private  String telefono;
    private  String email;
    private  Integer numeroEstudiantes;
    private  Integer numeroProfesores;
    private  String tipo;
    private  LocalDate anioFundacion;
    private  String codigoInstituto;

    private  LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
    private  UUID uuid;
}

