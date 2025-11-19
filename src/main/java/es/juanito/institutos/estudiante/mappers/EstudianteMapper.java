package es.juanito.institutos.estudiante.mappers;

import es.juanito.institutos.estudiante.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiante.models.Estudiante;
import org.springframework.stereotype.Component;

@Component
public class EstudianteMapper {

    public Estudiante toEstudiante(EstudianteRequestDto dto) {
        return Estudiante.builder()
                .id(null)
                .nombre(dto.getNombre())
                .codigoInstituto(dto.getCodigoInstituto())
                .isDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : false)
                .build();
    }

    public Estudiante toEstudiante(EstudianteRequestDto dto, Estudiante estudiante) {
        return Estudiante.builder()
                .id(estudiante.getId())
                .nombre(dto.getNombre() != null ? dto.getNombre() : estudiante.getNombre())
                .codigoInstituto(dto.getCodigoInstituto() != null ? dto.getCodigoInstituto() : estudiante.getCodigoInstituto())
                .createdAt(estudiante.getCreatedAt())
                .updatedAt(estudiante.getUpdatedAt())
                .isDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : estudiante.getIsDeleted())
                .build();
    }
}
