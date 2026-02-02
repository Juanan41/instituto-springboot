package es.juanito.institutos.rest.estudiantes.repositories;

import es.juanito.institutos.rest.estudiantes.models.Estudiante;
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
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // ----------------------------------------------------------------------
    // 🔑 METODOS DE UNICIDAD Y AUTENTICACIÓN (Username/Email/DNI)
    // ----------------------------------------------------------------------

    Optional<Estudiante> findByDniOrEmail(String dni, String email);

    Optional<Estudiante> findByEmail(String email);

    Optional<Estudiante> findByDniEqualsIgnoreCase(String dni);

    Optional<Estudiante> findByUsername(String username);



    // ----------------------------------------------------------------------
    // 🔎 METODOS DE BUSQUEDA SIN PAGINACION
    // ----------------------------------------------------------------------

    List<Estudiante> findByInstituto_CodigoInstitutoContainsIgnoreCase(String codigoInstituto);

    List<Estudiante> findByNombreContainsIgnoreCase(String nombre);

    List<Estudiante> findByNombreContainingIgnoreCase(String nombre);

    List<Estudiante> findAllByInstitutoId(Long institutoId);

    // ✅ IMPORTANTE: el tipo Instituto correcto (mismo que el Controller GraphQL)
    List<Estudiante> findByInstituto(Instituto instituto);

    // ----------------------------------------------------------------------
    // 📄 METODOS DE BUSQUEDA CON PAGINACION
    // ----------------------------------------------------------------------

    Page<Estudiante> findByNombreContainsIgnoreCase(String nombre, Pageable pageable);

    Page<Estudiante> findByInstituto_CodigoInstitutoContainsIgnoreCase(String codigoInstituto, Pageable pageable);

    Page<Estudiante> findByInstituto_CodigoInstitutoContainsIgnoreCaseAndNombreContainsIgnoreCase(
            String codigoInstituto, String nombre, Pageable pageable
    );

    // ✅ BUSCADOR ZONA PÚBLICA (NOMBRE / APELLIDOS / DNI)
    Page<Estudiante> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDniContainingIgnoreCase(
            String nombre,
            String apellidos,
            String dni,
            Pageable pageable
    );

    // ----------------------------------------------------------------------
    // 📚 CONSULTAS BASADAS EN RELACION INSTITUTO (@Query)
    // ----------------------------------------------------------------------

    @Query("SELECT e FROM Estudiante e JOIN FETCH e.instituto WHERE e.id = :id")
    Optional<Estudiante> findByIdConInstituto(@Param("id") Long id);

    @Query("SELECT e FROM Estudiante e WHERE LOWER(e.instituto.nombre) LIKE %:nombreInstituto%")
    List<Estudiante> findByInstitutoNombreContainsIgnoreCase(@Param("nombreInstituto") String nombreInstituto);

    @Query("SELECT e FROM Estudiante e WHERE e.dni = :dni AND LOWER(e.instituto.nombre) LIKE %:nombreInstituto%")
    List<Estudiante> findByDniAndInstitutoNombreContainsIgnoreCase(
            @Param("dni") String dni,
            @Param("nombreInstituto") String nombreInstituto
    );

    @Query("SELECT e FROM Estudiante e WHERE e.instituto.codigoInstituto = :codigoInstituto")
    List<Estudiante> findByInstitutoCodigoInstituto(@Param("codigoInstituto") String codigoInstituto);

    @Query("""
    SELECT e
        FROM Estudiante e
        LEFT JOIN FETCH e.instituto
        WHERE e.id = :id
    """)
    Optional<Estudiante> findByIdWithInstituto(Long id);


    // ----------------------------------------------------------------------
    // 🗑️ BORRADO LOGICO Y UUID
    // ----------------------------------------------------------------------

    Optional<Estudiante> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    @Transactional
    @Modifying
    @Query("UPDATE Estudiante e SET e.isDeleted = true WHERE e.id = :id")
    void updateIsDeletedToTrueById(@Param("id") Long id);

    // ----------------------------------------------------------------------
    // 🔍 FILTROS FLEXIBLES CON PAGINACION
    // ----------------------------------------------------------------------

    @Query("""
       SELECT e FROM Estudiante e
       WHERE (:username IS NULL OR LOWER(e.username) LIKE LOWER(CONCAT('%', :username, '%')))
         AND (:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%')))
         AND (:isDeleted IS NULL OR e.isDeleted = :isDeleted)
       """)
    Page<Estudiante> findByFilters(@Param("username") String username,
                                   @Param("email") String email,
                                   @Param("isDeleted") Boolean isDeleted,
                                   Pageable pageable);
}
