package es.juanito.institutos.nombre.repositories;

import es.juanito.institutos.nombre.models.Nombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NombreRepository extends JpaRepository<Nombre, Long> {

    Optional<Nombre> findByCodigoInstitutoEqualsIgnoreCase(String codigoInstituto);

    Optional<Nombre> findByCodigoInstitutoEqualsIgnoreCaseAndIsDeletedFalse(String codigoInstituto);

    List<Nombre> findByCodigoInstitutoContainingIgnoreCase(String codigoInstituto);

    List<Nombre> findByCodigoInstitutoContainingIgnoreCaseAndIsDeletedFalse(String codigoInstituto);

    List<Nombre> findByIsDeleted(Boolean isDeleted);
}
