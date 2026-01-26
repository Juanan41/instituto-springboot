package es.juanito.institutos.rest.estudiantes.mappers;

import es.juanito.institutos.rest.estudiantes.dto.EstudianteInfoResponseDto;
import es.juanito.institutos.rest.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.rest.estudiantes.dto.EstudianteResponseDto;
import es.juanito.institutos.rest.estudiantes.models.Estudiante;
import es.juanito.institutos.rest.institutos.models.Instituto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstudianteMapper {

    // ---------- CREATE ----------
    public Estudiante toEstudiante(EstudianteRequestDto dto, Instituto instituto) {
        return Estudiante.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .dni(dto.getDni())
                .fechaNacimiento(dto.getFechaNacimiento())
                .instituto(instituto)
                .isDeleted(false)
                .build();
    }

    // ---------- UPDATE ----------
    public Estudiante toEstudiante(
            EstudianteRequestDto dto,
            Estudiante estudiante,
            Instituto instituto
    ) {
        if (dto.getNombre() != null) estudiante.setNombre(dto.getNombre());
        if (dto.getApellidos() != null) estudiante.setApellidos(dto.getApellidos());
        if (dto.getEmail() != null) estudiante.setEmail(dto.getEmail());
        if (dto.getUsername() != null) estudiante.setUsername(dto.getUsername());
        if (dto.getDni() != null) estudiante.setDni(dto.getDni());
        if (dto.getFechaNacimiento() != null) estudiante.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getIsDeleted() != null) estudiante.setIsDeleted(dto.getIsDeleted());

        estudiante.setInstituto(instituto);
        return estudiante;
    }

    // ---------- RESPONSE DTO ----------
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
                .institutoId(
                        estudiante.getInstituto() != null
                                ? estudiante.getInstituto().getId()
                                : null
                )
                .build();
    }

    // ---------- INFO RESPONSE DTO ----------
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
                .institutoId(
                        estudiante.getInstituto() != null
                                ? estudiante.getInstituto().getId()
                                : null
                )
                .build();
    }

    // ---------- LIST ----------
    public List<EstudianteResponseDto> toResponseDtoList(List<Estudiante> estudiantes) {
        return estudiantes.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
