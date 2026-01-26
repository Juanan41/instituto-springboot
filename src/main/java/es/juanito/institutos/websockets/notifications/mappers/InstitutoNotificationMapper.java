package es.juanito.institutos.websockets.notifications.mappers;

import es.juanito.institutos.rest.institutos.models.Instituto;
import es.juanito.institutos.websockets.notifications.dto.InstitutoNotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class InstitutoNotificationMapper {

    /**
     * Convierte una entidad Instituto a su DTO de respuesta para notificaciones.
     * @param instituto La entidad Instituto a mapear.
     * @return El DTO de notificación simplificado (Java Record).
     */
    public InstitutoNotificationResponse toInstitutoNotificationDto(Instituto instituto) {

        // Usamos el constructor compacto del Java Record
        return new InstitutoNotificationResponse(
                // 🔑 Identificadores
                instituto.getId(),
                instituto.getNombre(),
                instituto.getCodigoInstituto(),
                instituto.getTipo(),

                // 🕒 Metadatos de Auditoría
                instituto.getCreatedAt(),
                instituto.getUpdatedAt(),
                instituto.getUuid(),
                instituto.getIsDeleted()
        );
    }
}