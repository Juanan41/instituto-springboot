package es.juanito.institutos.rest.users.services; // PAQUETE ADAPTADO (en la carpeta services raíz o users.services)

import es.juanito.institutos.rest.users.dto.UserInfoResponseDto; // DTOs adaptados
import es.juanito.institutos.rest.users.dto.UserRequestDto;      // DTOs adaptados
import es.juanito.institutos.rest.users.dto.UserResponseDto;     // DTOs adaptados
import es.juanito.institutos.rest.users.exceptions.UserNotFound; // NUEVO NOMBRE
import es.juanito.institutos.rest.users.exceptions.UserNameOrEmailExists; // NUEVO NOMBRE
import es.juanito.institutos.rest.users.mappers.UsersMapper;
import es.juanito.institutos.rest.users.models.User;
import es.juanito.institutos.rest.users.repositories.UsersRepository; // Repositorio adaptado

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder; // Necesario para codificar la contraseña en SAVE/UPDATE
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"users"})
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder; // Inyección para seguridad
    // ELIMINADO: private final TarjetasRepository tarjetasRepository;

    @Override
    public Page<UserResponseDto> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando todos los usuarios con username: {} y borrados: {}", username, isDeleted);

        // --- Lógica de Especificaciones (Filtros) ---
        Specification<User> specUsernameUser = (root, query, criteriaBuilder) ->
                username.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> specEmailUser = (root, query, criteriaBuilder) ->
                email.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(m -> criteriaBuilder.equal(root.get("isDeleted"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> criterio = Specification.allOf(
                specUsernameUser,
                specEmailUser,
                specIsDeleted
        );

        // Mapeamos el Page de Entidad a Page de DTO
        return usersRepository.findAll(criterio, pageable).map(usersMapper::toUserResponse);
    }

    @Override
    @Cacheable(key = "#id")
    public UserInfoResponseDto findById(Long id) { // DTO adaptado
        log.info("Buscando usuario por id: {}", id);

        var user = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFound(id));

        // ELIMINADO: No se necesita buscar tarjetas
        // return usersMapper.toUserInfoResponse(user, tarjetas);
        return usersMapper.toUserInfoResponse(user);
    }

    @Override
    @CachePut(key = "#result.id")
    public UserResponseDto save(UserRequestDto userRequest) { // DTO adaptado
        log.info("Guardando usuario: {}", userRequest.getUsername());

        // 1. Validación de unicidad
        usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(u -> {
                    throw new UserNameOrEmailExists("Ya existe un usuario con ese username o email");
                });

        // 2. Codificación de contraseña
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // 3. Mapeo y guardado
        return usersMapper.toUserResponse(usersRepository.save(usersMapper.toUser(userRequest)));
    }

    @Override
    @CachePut(key = "#result.id")
    public UserResponseDto update(Long id, UserRequestDto userRequest) { // DTO adaptado
        log.info("Actualizando usuario con id: {}", id);

        // 1. Buscar el usuario existente
        User existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFound(id)); // Excepción adaptada

        // 2. Validación de unicidad (si el username/email son de otro usuario)
        usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new UserNameOrEmailExists("Ya existe un usuario con ese username o email"); // Excepción adaptada
                    }
                });

        // 3. Si la contraseña se ha cambiado en el DTO, codificarla
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        } else {
            // Si no se proporciona, mantener la contraseña existente
            userRequest.setPassword(existingUser.getPassword());
        }

        // 4. Mapear y actualizar (usando el mapeo de actualización, aunque aquí lo hacemos manual por la codificación)
        existingUser.setNombre(userRequest.getNombre());
        existingUser.setApellidos(userRequest.getApellidos());
        existingUser.setUsername(userRequest.getUsername());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPassword(userRequest.getPassword());
        existingUser.setRoles(userRequest.getRoles());
        existingUser.setIsDeleted(userRequest.getIsDeleted());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return usersMapper.toUserResponse(usersRepository.save(existingUser));
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.info("Procesando borrado para usuario con id: {}", id);

        User user = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFound(id)); // Excepción adaptada

        // Lógica simplificada (ELIMINADO: No hay dependencia de tarjetas)
        // Por defecto, se realiza el borrado lógico a menos que desees el físico directo.

        // Realizamos el borrado lógico
        log.info("Borrado lógico de usuario por id: {}", id);
        usersRepository.updateIsDeletedToTrueById(id);

        /*
        // Si quisieras borrado físico condicional (ej. si no hay tarjetas):
        if (usersRepository.existsById(id)) { // Revisa si existen otras dependencias si las hubiera
            log.info("Borrado físico de usuario por id: {}", id);
            usersRepository.delete(user);
        } else {
            // Borrado lógico si hay dependencias (si usersRepository.updateIsDeletedToTrueById(id); no funcionara)
            usersRepository.updateIsDeletedToTrueById(id);
        }
        */
    }

    @Override
    public List<User> findAllActiveUsers() {
        log.info("Buscando todos los usuarios activos");
        return usersRepository.findAllByIsDeletedFalse();
    }
}
