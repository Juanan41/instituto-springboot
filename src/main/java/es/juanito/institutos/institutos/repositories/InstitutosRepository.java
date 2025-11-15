package es.juanito.institutos.institutos.repositories;

import es.juanito.institutos.institutos.models.Instituto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstitutosRepository extends JpaRepository<Instituto, Long> {

    // Por ciudad
    List<Instituto> findByCiudad(String ciudad);
    List<Instituto> findByCiudadContainsIgnoreCase(String ciudad); // <-- FALTABA
    List<Instituto> findByCiudadAndIsDeletedFalse(String ciudad);

    // Por nombre
    List<Instituto> findByNombreContainsIgnoreCase(String nombre);
    List<Instituto> findByNombreContainsIgnoreCaseAndIsDeletedFalse(String nombre);

    // Por ciudad y nombre
    List<Instituto> findByCiudadAndNombreContainsIgnoreCase(String ciudad, String nombre);
    List<Instituto> findByCiudadAndNombreContainsIgnoreCaseAndIsDeletedFalse(String ciudad, String nombre);

    // Por UUID
    Optional<Instituto> findByUuid(UUID uuid);
    boolean existsByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);

    // Si está borrado
    List<Instituto> findByIsDeleted(Boolean isDeleted);

    @Modifying
    @Query("UPDATE Instituto i SET i.isDeleted = true WHERE i.id = :id")
    void updateIsDeletedToTrueById(Long id);
}
