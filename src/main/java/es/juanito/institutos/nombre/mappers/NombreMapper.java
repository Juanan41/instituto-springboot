package es.juanito.institutos.nombre.mappers;

import es.juanito.institutos.nombre.dto.NombreRequestDto;
import es.juanito.institutos.nombre.models.Nombre;
import org.springframework.stereotype.Component;

@Component
public class NombreMapper {

    public Nombre toNombre(NombreRequestDto dto) {
        return Nombre.builder()
                .id(null)
                .codigoInstituto(dto.getCodigoInstituto())
                .isDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : false)
                .build();
    }

    public Nombre toNombre(NombreRequestDto dto, Nombre nombre) {
        return Nombre.builder()
                .id(nombre.getId())
                .codigoInstituto(dto.getCodigoInstituto() != null ? dto.getCodigoInstituto() : nombre.getCodigoInstituto())
                .createdAt(nombre.getCreatedAt())
                .updatedAt(nombre.getUpdatedAt())
                .isDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : nombre.getIsDeleted())
                .build();
    }
}
