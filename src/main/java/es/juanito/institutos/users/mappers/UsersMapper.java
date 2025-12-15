package es.juanito.institutos.users.mappers;

import es.juanito.institutos.users.dto.UserInfoResponseDto; // DTOs corregidos
import es.juanito.institutos.users.dto.UserRequestDto;      // DTOs corregidos
import es.juanito.institutos.users.dto.UserResponseDto;     // DTOs corregidos
import es.juanito.institutos.users.models.User; // Modelo de usuario base
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsersMapper {

    // --- 1. Mapeo de Creación (DTO -> Entidad) ---
    // NOTA: El password se codifica y los metadatos se asignan en el servicio
    public User toUser(UserRequestDto request) {
        return User.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isDeleted(request.getIsDeleted() != null ? request.getIsDeleted() : false)
                // Se ignoran id, uuid, createdAt, updatedAt para que JPA los maneje
                .build();
    }

    // --- 2. Mapeo de Actualización (DTO + Entidad Actual -> Entidad Actualizada) ---

    /**
     * Mapea los campos del DTO a la entidad User existente.
     * Esto es más seguro que usar el builder para actualizar.
     */
    public void updateToUser(UserRequestDto request, User user) {
        if (request.getNombre() != null) user.setNombre(request.getNombre());
        if (request.getApellidos() != null) user.setApellidos(request.getApellidos());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        // El password se maneja y codifica en el servicio, solo lo actualizamos si no es nulo
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword()); // El servicio se encargará de codificarlo
        }
        if (request.getRoles() != null) user.setRoles(request.getRoles());
        if (request.getIsDeleted() != null) user.setIsDeleted(request.getIsDeleted());
        user.setUpdatedAt(LocalDateTime.now());
    }

    // --- 3. Mapeo de Salida Resumido (Entidad -> DTO Response) ---

    public UserResponseDto toUserResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .uuid(user.getUuid())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // --- 4. Mapeo de Salida Detallado (Entidad -> DTO InfoResponse) ---

    // NOTA: Se ha eliminado la dependencia List<String> tarjetas del argumento para simplificar.
    public UserInfoResponseDto toUserInfoResponse(User user) {
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .uuid(user.getUuid())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                // .tarjetas(tarjetas) // Si necesitas este campo, debes añadirlo como argumento
                .build();
    }

    // --- 5. Mapeo de Lista ---

    public List<UserResponseDto> toUserResponseList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}