package es.juanito.institutos.estudiantes.repositories;

import es.juanito.institutos.estudiantes.models.Estudiante;
import org.springframework.data.domain.Page; // Importación para paginación
import org.springframework.data.domain.Pageable; // Importación para paginación
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // ----------------------------------------------------------------------
    // 🔑 METODOS DE UNICIDAD Y VALIDACION (Sin cambios)
    // ----------------------------------------------------------------------

    Optional<Estudiante> findByDniOrEmail(String dni, String email);
    Optional<Estudiante> findByEmail(String email);
    Optional<Estudiante> findByDniEqualsIgnoreCase(String dni);

    // ----------------------------------------------------------------------
    // 🔎 METODOS DE BUSQUEDA SIN PAGINACION (Se mantienen)
    // ----------------------------------------------------------------------

    // NOTA: Se corrige la convención del guion bajo (instituto_codigoInstituto)
    List<Estudiante> findByInstituto_CodigoInstitutoContainsIgnoreCase(String codigoInstituto);
    List<Estudiante> findByNombreContainsIgnoreCase(String nombre);
    List<Estudiante> findByNombreContainingIgnoreCase(String nombre);
    List<Estudiante> findAllByInstitutoId(Long institutoId);

    // ----------------------------------------------------------------------
    // 📄 METODOS DE BUSQUEDA CON PAGINACION (Añadidos/Corregidos)
    // ----------------------------------------------------------------------

    // 1. Paginación y Filtrado por Nombre
    Page<Estudiante> findByNombreContainsIgnoreCase(String nombre, Pageable pageable);

    // 2. Paginación y Filtrado por Código de Instituto
    // Se usa la convención del guion bajo (_) para navegar en la relación
    Page<Estudiante> findByInstituto_CodigoInstitutoContainsIgnoreCase(String codigoInstituto, Pageable pageable);

    // 3. Paginación y Filtrado por Código de Instituto Y Nombre
    // Se usa la convención del guion bajo (_) para navegar en la relación
    Page<Estudiante> findByInstituto_CodigoInstitutoContainsIgnoreCaseAndNombreContainsIgnoreCase(
            String codigoInstituto, String nombre, Pageable pageable
    );

    // ----------------------------------------------------------------------
    // 📚 CONSULTAS BASADAS EN RELACION INSTITUTO (@Query) (Sin cambios)
    // ----------------------------------------------------------------------

    @Query("SELECT e FROM Estudiante e WHERE LOWER(e.instituto.nombre) LIKE %:nombreInstituto%")
    List<Estudiante> findByInstitutoNombreContainsIgnoreCase(@Param("nombreInstituto") String nombreInstituto);

    @Query("SELECT e FROM Estudiante e WHERE e.dni = :dni AND LOWER(e.instituto.nombre) LIKE %:nombreInstituto%")
    List<Estudiante> findByDniAndInstitutoNombreContainsIgnoreCase(
            @Param("dni") String dni,
            @Param("nombreInstituto") String nombreInstituto
    );

    @Query("SELECT e FROM Estudiante e WHERE e.instituto.codigoInstituto = :codigoInstituto")
    List<Estudiante> findByInstitutoCodigoInstituto(
            @Param("codigoInstituto") String codigoInstituto
    );

    // ----------------------------------------------------------------------
    // 🗑️ BORRADO LOGICO Y UUID (Sin cambios)
    // ----------------------------------------------------------------------

    Optional<Estudiante> findByUuid(UUID uuid);
    boolean existsByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);

    @Modifying
    @Query("UPDATE Estudiante e SET e.isDeleted = true WHERE e.id = :id")
    void updateIsDeletedToTrueById(@Param("id") Long id);
}