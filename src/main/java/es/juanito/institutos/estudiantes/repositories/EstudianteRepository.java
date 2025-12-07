package es.juanito.institutos.estudiantes.repositories;

import es.juanito.institutos.estudiantes.models.Estudiante;
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
    // 🔑 METODOS DE UNICIDAD Y VALIDACION (Añadidos/Asegurados)
    // ----------------------------------------------------------------------

    // Método para validar si el DNI o Email ya existen (usado antes de guardar/actualizar)
    Optional<Estudiante> findByDniOrEmail(String dni, String email);

    Optional<Estudiante> findByEmail(String email);
    Optional<Estudiante> findByDniEqualsIgnoreCase(String dni);

    // ----------------------------------------------------------------------
    // 🔎 METODOS DE BUSQUEDA POR CONVENIO
    // ----------------------------------------------------------------------

    List<Estudiante> findByInstitutoCodigoInstitutoContainsIgnoreCase(String codigoInstituto);
    List<Estudiante> findByNombreContainsIgnoreCase(String nombre);
    List<Estudiante> findByNombreContainingIgnoreCase(String nombre);
    List<Estudiante> findAllByInstitutoId(Long institutoId);

    // ----------------------------------------------------------------------
    // 📚 CONSULTAS BASADAS EN RELACION INSTITUTO (@Query)
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
    // 🗑️ BORRADO LOGICO Y UUID
    // ----------------------------------------------------------------------

    Optional<Estudiante> findByUuid(UUID uuid);
    boolean existsByUuid(UUID uuid);

    // Nota: deleteByUuid puede eliminarse si solo se usa Soft Delete, pero se deja si es parte de la guía.
    void deleteByUuid(UUID uuid);

    @Modifying
    @Query("UPDATE Estudiante e SET e.isDeleted = true WHERE e.id = :id")
    void updateIsDeletedToTrueById(@Param("id") Long id);
}