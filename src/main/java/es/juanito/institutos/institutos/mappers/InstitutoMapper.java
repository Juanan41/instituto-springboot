package es.juanito.institutos.institutos.mappers;

import es.juanito.institutos.estudiante.models.Estudiante;
import es.juanito.institutos.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.institutos.models.Instituto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class InstitutoMapper {
    public Instituto toInstituto(InstitutoCreateDto institutoCreateDto, Estudiante estudiante) {
        return Instituto.builder()
                .id(null)
                .nombre(institutoCreateDto.getNombre())
                .ciudad(institutoCreateDto.getCiudad())
                .direccion(institutoCreateDto.getDireccion())
                .telefono(institutoCreateDto.getTelefono())
                .email(institutoCreateDto.getEmail())
                .estudiantes(estudiantes)
                .numeroProfesores(institutoCreateDto.getNumeroProfesores())
                .tipo(institutoCreateDto.getTipo())
                .anioFundacion(institutoCreateDto.getAnioFundacion())
                .codigoInstituto(institutoCreateDto.getCodigoInstituto())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .build();


    }


    public Instituto toInstituto(InstitutoUpdateDto institutoUpdateDto, Instituto instituto) {
        return Instituto.builder()
                .id(instituto.getId())
                .nombre(institutoUpdateDto.getNombre() != null ? institutoUpdateDto.getNombre() : instituto.getNombre())
                .ciudad(institutoUpdateDto.getCiudad() != null ? institutoUpdateDto.getCiudad() : instituto.getCiudad())
                .direccion(institutoUpdateDto.getDireccion() != null ? institutoUpdateDto.getDireccion() : instituto.getDireccion())
                .telefono(institutoUpdateDto.getTelefono() != null ? institutoUpdateDto.getTelefono() : instituto.getTelefono())
                .email(institutoUpdateDto.getEmail() != null ? institutoUpdateDto.getEmail() : instituto.getEmail())
                .estudiantes(instituto.getEstudiantes())
                .numeroProfesores(institutoUpdateDto.getNumeroProfesores() != null ? institutoUpdateDto.getNumeroProfesores() : instituto.getNumeroProfesores())
                .tipo(institutoUpdateDto.getTipo() != null ? institutoUpdateDto.getTipo() : instituto.getTipo())
                .anioFundacion(institutoUpdateDto.getAnioFundacion() != null ? institutoUpdateDto.getAnioFundacion() : instituto.getAnioFundacion())
                .codigoInstituto(institutoUpdateDto.getCodigoInstituto() != null ? institutoUpdateDto.getCodigoInstituto() : instituto.getCodigoInstituto())
                .createdAt(instituto.getCreatedAt())
                .updateAt(LocalDateTime.now())
                .uuid(instituto.getUuid())
                .build();
    }


    public InstitutoResponseDto toInstitutoResponseDto(Instituto instituto) {
        return InstitutoResponseDto.builder()
                .id(instituto.getId())
                .nombre(instituto.getNombre())
                .ciudad(instituto.getCiudad())
                .direccion(instituto.getDireccion())
                .telefono(instituto.getTelefono())
                .estudiantes(
                        instituto.getEstudiantes().stream()
                                .map(Estudiante::getNombre)
                                .toList()
                )
                .numeroProfesores(instituto.getNumeroProfesores())
                .tipo(instituto.getTipo())
                .anioFundacion(instituto.getAnioFundacion())
                .codigoInstituto(instituto.getCodigoInstituto())
                .createdAt(instituto.getCreatedAt())
                .updatedAt(instituto.getUpdateAt())
                .uuid(instituto.getUuid())
                .build();
    }

    // Mappeamos de modelo a DTO (lista)
    public List<InstitutoResponseDto> toResponseDtoList(List<Instituto> institutos) {
        return institutos.stream()
                .map(this::toInstitutoResponseDto)
                .toList();
    }
}
