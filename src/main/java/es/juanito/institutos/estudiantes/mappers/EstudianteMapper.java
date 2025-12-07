package es.juanito.institutos.estudiantes.mappers;

import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.models.Estudiante;
import es.juanito.institutos.institutos.models.Instituto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EstudianteMapper {

    // --- 1. Mapeo de Creación (DTO -> Entidad) ---

    public Estudiante toEstudiante(EstudianteRequestDto dto, Instituto instituto) {
        return Estudiante.builder()
                .id(null) // ID es null y se genera por la BD
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .fechaNacimiento(dto.getFechaNacimiento())
                .dni(dto.getDni())
                .isDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : false) // Mapeo de isDeleted
                .instituto(instituto) // Asignamos la relación (FK)
                // Omitimos UUID, createdAt, updatedAt para que los gestione la Entidad/Hibernate
                .build();
    }

    // --- 2. Mapeo de Actualización (DTO + Entidad Actual -> Entidad Actualizada) ---

    /**
     * Mapea un DTO de petición a una entidad Estudiante existente.
     * Requiere la entidad Instituto NUEVA (o la misma) para actualizar la FK.
     */
    // 🛑 CORRECCIÓN DE FALLO 1: La firma debe incluir el parámetro 'Instituto nuevoInstituto'.
    public Estudiante toEstudiante(EstudianteRequestDto dto, Estudiante estudiante, Instituto nuevoInstituto) {

        return Estudiante.builder()
                .id(estudiante.getId())
                .uuid(estudiante.getUuid()) // Preservamos el UUID
                .createdAt(estudiante.getCreatedAt()) // Preservamos la fecha de creación
                .updatedAt(LocalDateTime.now()) // Actualización manual

                // Mapeo de Campos de Negocio (Lógica de no nulos)
                .nombre(dto.getNombre() != null ? dto.getNombre() : estudiante.getNombre())
                .apellidos(dto.getApellidos() != null ? dto.getApellidos() : estudiante.getApellidos())
                .email(dto.getEmail() != null ? dto.getEmail() : estudiante.getEmail())
                .fechaNacimiento(dto.getFechaNacimiento() != null ? dto.getFechaNacimiento() : estudiante.getFechaNacimiento())
                .dni(dto.getDni() != null ? dto.getDni() : estudiante.getDni())

                // Actualización de Metadatos y Relación
                .isDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : estudiante.getIsDeleted())
                // 🛑 CORRECCIÓN DE FALLO 2: Asignar la relación Instituto (Clave Foránea)
                .instituto(nuevoInstituto)
                .build();
    }

    // --- 3. Mapeo de Salida Individual (Entidad -> DTO Único) ---

    /**
     * Mapea una única entidad a EstudianteRequestDto (para findById, save, update).
     * 🛑 CORRECCIÓN DE FALLO 3: Este método es requerido por el método toRequestDtoList.
     */
    public EstudianteRequestDto toEstudianteRequestDto(Estudiante estudiante) {
        return EstudianteRequestDto.builder()
                // Mapeando metadatos (Asumiendo que EstudianteRequestDto tiene id, uuid, createdAt, etc.)
                .id(estudiante.getId())
                .uuid(estudiante.getUuid())
                .createdAt(estudiante.getCreatedAt())
                .updatedAt(estudiante.getUpdatedAt())

                // Mapeando campos de negocio
                .nombre(estudiante.getNombre())
                .apellidos(estudiante.getApellidos())
                .email(estudiante.getEmail())
                .fechaNacimiento(estudiante.getFechaNacimiento())
                .dni(estudiante.getDni())
                // Obtenemos el código del Instituto desde la relación para el DTO
                .codigoInstituto(estudiante.getInstituto() != null ? estudiante.getInstituto().getCodigoInstituto() : null)
                .isDeleted(estudiante.getIsDeleted())
                .build();
    }

    // --- 4. Mapeo de Salida de Lista (Lista de Entidades -> Lista de DTO Único) ---

    public List<EstudianteRequestDto> toRequestDtoList(List<Estudiante> estudiantes) {
        return estudiantes.stream()
                // Usa el método que mapea una sola entidad
                .map(this::toEstudianteRequestDto)
                .collect(Collectors.toList());
    }
}