package es.juanito.institutos.users.services; // PAQUETE ADAPTADO (en la carpeta services raíz o users.services)

import es.juanito.institutos.users.dto.UserInfoResponseDto; // DTOs adaptados
import es.juanito.institutos.users.dto.UserRequestDto;      // DTOs adaptados
import es.juanito.institutos.users.dto.UserResponseDto;     // DTOs adaptados
import es.juanito.institutos.users.models.User; // Entidad User
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsersService {

    /**
     * Busca todos los usuarios paginados, aplicando filtros opcionales (username, email, isDeleted).
     */
    Page<UserResponseDto> findAll(
            Optional<String> username,
            Optional<String> email,
            Optional<Boolean> isDeleted,
            Pageable pageable
    );

    /**
     * Busca un usuario por ID. Devuelve la información detallada (InfoResponseDto).
     */
    UserInfoResponseDto findById(Long id);

    /**
     * Crea un nuevo usuario.
     */
    UserResponseDto save(UserRequestDto userRequest);

    /**
     * Actualiza un usuario existente.
     */
    UserResponseDto update(Long id, UserRequestDto userRequest);

    /**
     * Borrado lógico de un usuario por ID.
     */
    void deleteById(Long id);

    /**
     * Devuelve una lista de todos los usuarios no borrados (isDeleted=false).
     * Útil para utilidades internas o validación.
     */
    List<User> findAllActiveUsers();

}