package es.juanito.institutos.rest.institutos.repositories;

import es.juanito.institutos.rest.institutos.models.Instituto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstitutosRepository extends JpaRepository<Instituto, Long> {

    // =================================================
    // 🔎 MÉTODOS BASE
    // =================================================

    List<Instituto> findByCiudadContainingIgnoreCaseAndIsDeletedFalse(String ciudad);

    List<Instituto> findByNombreContainingIgnoreCaseAndIsDeletedFalse(String nombre);

    List<Instituto> findByCiudadContainingIgnoreCaseAndNombreContainingIgnoreCaseAndIsDeletedFalse(
            String ciudad, String nombre
    );

    Optional<Instituto> findByUuidAndIsDeletedFalse(UUID uuid);

    Optional<Instituto> findByCodigoInstitutoAndIsDeletedFalse(String codigoInstituto);

    boolean existsByUuidAndIsDeletedFalse(UUID uuid);

    // =================================================
    // ✅ BUSCADOR CON PAGINACIÓN (ZONA PÚBLICA)
    // =================================================

    Page<Instituto> findByIsDeletedFalseAndNombreContainingIgnoreCaseOrIsDeletedFalseAndCiudadContainingIgnoreCaseOrIsDeletedFalseAndCodigoInstitutoContainingIgnoreCase(
            String nombre,
            String ciudad,
            String codigoInstituto,
            Pageable pageable
    );

    // =================================================
    // 🔁 MÉTODOS DE COMPATIBILIDAD
    // =================================================

    // --- Ciudad ---
    default List<Instituto> findByCiudad(String ciudad) {
        return findByCiudadContainingIgnoreCaseAndIsDeletedFalse(ciudad);
    }

    default List<Instituto> findByCiudadContainingIgnoreCase(String ciudad) {
        return findByCiudadContainingIgnoreCaseAndIsDeletedFalse(ciudad);
    }

    // --- Nombre ---
    default List<Instituto> findByNombreContainsIgnoreCase(String nombre) {
        return findByNombreContainingIgnoreCaseAndIsDeletedFalse(nombre);
    }

    default List<Instituto> findByNombreContainingIgnoreCase(String nombre) {
        return findByNombreContainingIgnoreCaseAndIsDeletedFalse(nombre);
    }

    // --- Ciudad + Nombre ---
    default List<Instituto> findByCiudadAndNombreContainsIgnoreCase(String ciudad, String nombre) {
        return findByCiudadContainingIgnoreCaseAndNombreContainingIgnoreCaseAndIsDeletedFalse(ciudad, nombre);
    }

    default List<Instituto> findByCiudadAndNombreContainingIgnoreCase(String ciudad, String nombre) {
        return findByCiudadContainingIgnoreCaseAndNombreContainingIgnoreCaseAndIsDeletedFalse(ciudad, nombre);
    }

    // --- UUID ---
    default Optional<Instituto> findByUuid(UUID uuid) {
        return findByUuidAndIsDeletedFalse(uuid);
    }

    default boolean existsByUuid(UUID uuid) {
        return existsByUuidAndIsDeletedFalse(uuid);
    }

    default void deleteByUuid(UUID uuid) {
        findByUuidAndIsDeletedFalse(uuid)
                .ifPresent(i -> updateIsDeletedToTrueById(i.getId()));
    }

    // --- Código Instituto ---
    Optional<Instituto> findByCodigoInstituto(String codigoInstituto);

    // =================================================
    // ✅ CARGAR INSTITUTO CON ESTUDIANTES (JOIN FETCH)
    // =================================================
    // ✅ NO lleva @Modifying
    @Query("SELECT i FROM Instituto i LEFT JOIN FETCH i.estudiantes WHERE i.id = :id")
    Optional<Instituto> findByIdConEstudiantes(@Param("id") Long id);

    // =================================================
    // 🗑️ SOFT DELETE (UPDATE)
    // =================================================
    // ✅ ESTE SÍ lleva @Modifying
    @Transactional
    @Modifying
    @Query("UPDATE Instituto i SET i.isDeleted = true WHERE i.id = :id")
    void updateIsDeletedToTrueById(@Param("id") Long id);
}
