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
public class UserInfoResponseDto {

    // Campos de Identificación
    private Long id;
    private UUID uuid;

    // Campos de Información Personal
    private String nombre;
    private String apellidos;
    private String username;
    private String email;

    // Campos de Seguridad / Control
    private Set<Role> roles;
    private Boolean isDeleted;

    // Metadatos de Auditoría
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}