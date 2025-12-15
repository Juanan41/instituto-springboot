package es.juanito.institutos.estudiantes.mappers;

import es.juanito.institutos.estudiantes.dto.EstudianteInfoResponseDto; // DTO de Salida Detallado
import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.dto.EstudianteResponseDto; // DTO de Salida Resumido
import es.juanito.institutos.estudiantes.models.Estudiante;
import es.juanito.institutos.institutos.models.Instituto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstudianteMapper {

    // --- 1. Mapeo de Creación (DTO -> Entidad) ---
    // El password y roles se asignan en el servicio
    public Estudiante toEstudiante(EstudianteRequestDto dto, Instituto instituto) {
        return Estudiante.builder()
                .id(null)
                .username(dto.getUsername()) // AÑADIDO: Campo de seguridad
                .password(dto.getPassword()) // Se pasa sin codificar (el servicio lo codificará)
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .dni(dto.getDni())
                .fechaNacimiento(dto.getFechaNacimiento())
                .instituto(instituto)
                .isDeleted(false) // Siempre false en la creación
                .build();
    }

    // --- 2. Mapeo de Actualización (DTO + Entidad Actual -> Entidad Actualizada) ---

    // Retorna la entidad existente para que el servicio la guarde.
    public Estudiante toEstudiante(EstudianteRequestDto dto, Estudiante estudiante, Instituto nuevoInstituto) {

        // No usamos el constructor builder aquí, sino los setters en la entidad existente
        if (dto.getNombre() != null) estudiante.setNombre(dto.getNombre());
        if (dto.getApellidos() != null) estudiante.setApellidos(dto.getApellidos());
        if (dto.getEmail() != null) estudiante.setEmail(dto.getEmail());
        if (dto.getUsername() != null) estudiante.setUsername(dto.getUsername()); // Actualizar username
        if (dto.getDni() != null) estudiante.setDni(dto.getDni());
        if (dto.getFechaNacimiento() != null) estudiante.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getIsDeleted() != null) estudiante.setIsDeleted(dto.getIsDeleted());

        // Si el DTO trae password (no nulo/vacío), el servicio lo codificará y lo asignará.

        // El instituto (FK) siempre se actualiza si viene en el DTO (porque lo validamos)
        estudiante.setInstituto(nuevoInstituto);

        return estudiante;
    }

    // --- 3. Mapeo de Salida Individual (Entidad -> DTO Resumido) ---

    // Para listas y respuestas de SAVE/UPDATE
    public EstudianteResponseDto toResponseDto(Estudiante estudiante) {
        return EstudianteResponseDto.builder()
                .id(estudiante.getId())
                .uuid(estudiante.getUuid())
                .username(estudiante.getUsername())
                .email(estudiante.getEmail())
                .nombre(estudiante.getNombre())
                .apellidos(estudiante.getApellidos())
                .dni(estudiante.getDni())
                .fechaNacimiento(estudiante.getFechaNacimiento())
                .isDeleted(estudiante.getIsDeleted())
                .institutoId(estudiante.getInstituto() != null ? estudiante.getInstituto().getId() : null)
                .build();
    }

    // --- 4. Mapeo de Salida Individual (Entidad -> DTO Detallado) ---

    // Para findById y /me/profile
    public EstudianteInfoResponseDto toInfoResponseDto(Estudiante estudiante) {
        return EstudianteInfoResponseDto.builder()
                .id(estudiante.getId())
                .uuid(estudiante.getUuid())
                .username(estudiante.getUsername())
                .email(estudiante.getEmail())
                .nombre(estudiante.getNombre())
                .apellidos(estudiante.getApellidos())
                .dni(estudiante.getDni())
                .fechaNacimiento(estudiante.getFechaNacimiento())
                .isDeleted(estudiante.getIsDeleted())
                .createdAt(estudiante.getCreatedAt())
                .updatedAt(estudiante.getUpdatedAt())
                .institutoId(estudiante.getInstituto() != null ? estudiante.getInstituto().getId() : null)
                .build();
    }

    // --- 5. Mapeo de Salida de Lista (Lista de Entidades -> Lista de DTOs Resumidos) ---

    public List<EstudianteResponseDto> toResponseDtoList(List<Estudiante> estudiantes) {
        return estudiantes.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}