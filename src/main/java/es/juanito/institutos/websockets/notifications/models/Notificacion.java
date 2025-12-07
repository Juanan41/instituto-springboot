package es.juanito.institutos.websockets.notifications.models;

import java.time.LocalDateTime;

/**
 * Representa el envoltorio universal para todas las notificaciones WebSocket.
 * La clase genérica 'T' contendrá el DTO específico (ej: InstitutoNotificationResponse).
 */
public record Notificacion<T>(
        String entity, // Nombre de la entidad afectada (ej: "Instituto", "Estudiante")
        Tipo type,     // Tipo de operación CRUD (CREATE, UPDATE, DELETE)
        T data,        // El cuerpo de la notificación (DTO específico)
        LocalDateTime createdAt
) {
    /**
     * Enum que define el tipo de operación CRUD.
     */
    public enum Tipo {CREATE, UPDATE, DELETE}
}