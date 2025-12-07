package es.juanito.institutos.websockets.notifications.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa la respuesta de notificación enviada por WebSocket
 * tras una operación CRUD en un Instituto.
 * Es inmutable (Java Record).
 */
public record InstitutoNotificationResponse(
        // 🔑 Identificadores
        Long id,
        String nombre,
        String codigoInstituto,
        String tipo,

        // 🕒 Metadatos de Auditoría
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID uuid,
        Boolean isDeleted
) {
    // Nota: Los records tienen un constructor compacto y métodos
    // getters automáticos (ej. instituto.id(), instituto.nombre())
    // y no necesitan Lombok.
};