package es.juanito.institutos.rest.users.dto;

import es.juanito.institutos.rest.users.models.Role; // Importación de la clase Role
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto { // Nombre DTO estandarizado

    // Campos de Identificación
    private Long id;
    private UUID uuid; // Añadido para consistencia

    // Campos de Información Personal (Resumida)
    private String nombre;
    private String apellidos;
    private String username;
    private String email;

    // Campos de Seguridad / Control
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

    @Builder.Default
    private Boolean isDeleted = false;

    // Metadatos de Auditoría
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}