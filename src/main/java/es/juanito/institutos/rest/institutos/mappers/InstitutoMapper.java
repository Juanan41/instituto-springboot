package es.juanito.institutos.rest.institutos.mappers;

import es.juanito.institutos.rest.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.rest.institutos.models.Instituto;
import es.juanito.institutos.rest.estudiantes.models.Estudiante;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set; // Importar Set
import java.util.UUID;

@Component
public class InstitutoMapper {

    public Instituto toInstituto(InstitutoCreateDto dto) {
        return Instituto.builder()
                .id(null)
                .nombre(dto.getNombre())
                .ciudad(dto.getCiudad())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .numeroProfesores(dto.getNumeroProfesores())
                .tipo(dto.getTipo())
                .anioFundacion(dto.getAnioFundacion())
                .codigoInstituto(dto.getCodigoInstituto())
                .uuid(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .estudiantes(new HashSet<>()) // CORREGIDO: Usar HashSet en lugar de Collections.emptyList()
                .build();
    }

    public Instituto toInstituto(InstitutoUpdateDto dto, Instituto instituto) {
        // La lógica de actualización es correcta, solo aseguramos que 'estudiantes' sea un Set
        Set<Estudiante> estudiantesSet = instituto.getEstudiantes() != null ? instituto.getEstudiantes() : new HashSet<>();

        return Instituto.builder()
                .id(instituto.getId())
                .nombre(dto.getNombre() != null ? dto.getNombre() : instituto.getNombre())
                .ciudad(dto.getCiudad() != null ? dto.getCiudad() : instituto.getCiudad())
                .direccion(dto.getDireccion() != null ? dto.getDireccion() : instituto.getDireccion())
                .telefono(dto.getTelefono() != null ? dto.getTelefono() : instituto.getTelefono())
                .email(dto.getEmail() != null ? dto.getEmail() : instituto.getEmail())
                .numeroProfesores(dto.getNumeroProfesores() != null ? dto.getNumeroProfesores() : instituto.getNumeroProfesores())
                .tipo(dto.getTipo() != null ? dto.getTipo() : instituto.getTipo())
                .anioFundacion(dto.getAnioFundacion() != null ? dto.getAnioFundacion() : instituto.getAnioFundacion())
                .codigoInstituto(dto.getCodigoInstituto() != null ? dto.getCodigoInstituto() : instituto.getCodigoInstituto())
                .estudiantes(estudiantesSet) // Mantenemos el Set existente
                .createdAt(instituto.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .uuid(instituto.getUuid())
                .isDeleted(instituto.getIsDeleted())
                .build();
    }

    public InstitutoResponseDto toInstitutoResponseDto(Instituto instituto) {
        // Mapeamos los nombres, asegurando que trabajamos con un Set
        List<String> estudiantesNombres = instituto.getEstudiantes() != null ?
                instituto.getEstudiantes().stream().map(Estudiante::getNombre).toList() :
                Collections.emptyList();

        return InstitutoResponseDto.builder()
                .id(instituto.getId())
                .nombre(instituto.getNombre())
                .ciudad(instituto.getCiudad())
                .direccion(instituto.getDireccion())
                .telefono(instituto.getTelefono())
                .email(instituto.getEmail())
                .numeroProfesores(instituto.getNumeroProfesores())
                .tipo(instituto.getTipo())
                .anioFundacion(instituto.getAnioFundacion())
                .codigoInstituto(instituto.getCodigoInstituto())
                .createdAt(instituto.getCreatedAt())
                .updatedAt(instituto.getUpdatedAt())
                .uuid(instituto.getUuid())
                .estudiantes(estudiantesNombres) // El DTO de respuesta usa List<String>
                .build();
    }

    public List<InstitutoResponseDto> toResponseDtoList(List<Instituto> institutos) {
        return institutos.stream()
                .map(this::toInstitutoResponseDto)
                .toList();
    }
}