package es.juanito.institutos.institutos.repositories;

import es.juanito.institutos.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.institutos.models.Instituto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstitutosRepository {
    List<Instituto> findAll();

    List<Instituto> findAllByCiudad(String ciudad);

    List<Instituto> findAllByNombre(String nombre);

    List<Instituto> findAllByCiudadAndNombre(String ciudad, String nombre);

    Optional<Instituto> findById(Long id);

    Optional<Instituto> findByUuid(UUID uuid);

    boolean existsById(Long id);

    boolean existsByUuid(UUID uuid);

    Instituto save(Instituto instituto);

    void deleteById(Long id);

    void deleteByUuid(UUID uuid);

    Long nextId();

}
