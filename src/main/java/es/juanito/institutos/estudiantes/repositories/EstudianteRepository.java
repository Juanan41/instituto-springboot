package es.juanito.institutos.estudiante.repositories;

import es.juanito.institutos.estudiante.models.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByCodigoInstitutoEqualsIgnoreCase(String codigoInstituto);

    Optional<Estudiante> findByCodigoInstitutoEqualsIgnoreCaseAndIsDeletedFalse(String codigoInstituto);

    List<Estudiante> findByCodigoInstitutoContainingIgnoreCase(String codigoInstituto);

    List<Estudiante> findByCodigoInstitutoContainingIgnoreCaseAndIsDeletedFalse(String codigoInstituto);

    List<Estudiante> findByIsDeleted(Boolean isDeleted);
}
