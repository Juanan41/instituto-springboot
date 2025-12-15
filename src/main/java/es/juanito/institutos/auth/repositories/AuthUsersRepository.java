package es.juanito.institutos.auth.repositories;

import es.juanito.institutos.users.models.User; // Ajustado a tu modelo
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUsersRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     */
    Optional<User> findByUsername(String username);

    // Si tu profesor utiliza la búsqueda combinada para registro:
    Optional<User> findByUsernameOrEmail(String username, String email);
}