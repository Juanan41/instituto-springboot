package es.juanito.institutos.users.repositories; // PAQUETE ADAPTADO: Repositorio de la entidad User

import es.juanito.institutos.users.models.User; // Importación de tu entidad User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// JpaSpecificationExecutor es útil para las consultas complejas de filtros que usan el servicio
public interface UsersRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Busca un usuario por username O email, ignorando mayúsculas/minúsculas.
     * Útil para la validación de registro (evitar duplicados).
     */
    Optional<User> findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(String username, String email);

    /**
     * Realiza un borrado lógico estableciendo isDeleted a true.
     * Esto evita la pérdida de datos y es una mejor práctica para APIs REST.
     */
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE User u SET u.isDeleted = true WHERE u.id = :id") // Usamos el alias 'u' para la entidad User
    void updateIsDeletedToTrueById(Long id);

    /**
     * Obtiene todos los usuarios que NO están borrados lógicamente.
     */
    List<User> findAllByIsDeletedFalse();

}