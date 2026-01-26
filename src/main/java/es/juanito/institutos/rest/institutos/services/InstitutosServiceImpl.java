package es.juanito.institutos.rest.institutos.services;

import es.juanito.institutos.rest.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.rest.institutos.exceptions.InstitutoBadUuidException;
import es.juanito.institutos.rest.institutos.exceptions.InstitutoNotFoundException;
import es.juanito.institutos.rest.institutos.mappers.InstitutoMapper;
import es.juanito.institutos.rest.institutos.models.Instituto;
import es.juanito.institutos.rest.institutos.repositories.InstitutosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "institutos")
public class InstitutosServiceImpl implements InstitutosService {

    private final InstitutosRepository repositorio;
    private final InstitutoMapper mapper;

    // ============================
    // FIND ALL CON FILTROS
    // ============================

    @Override
    public List<InstitutoResponseDto> findAll(String ciudad, String nombre) {

        boolean ciudadVacia = ciudad == null || ciudad.isBlank();
        boolean nombreVacio = nombre == null || nombre.isBlank();

        if (ciudadVacia && nombreVacio) {
            return mapper.toResponseDtoList(
                    repositorio.findAll()
                            .stream()
                            .filter(i -> !i.getIsDeleted())
                            .toList()
            );
        }

        if (!ciudadVacia && nombreVacio) {
            return mapper.toResponseDtoList(
                    repositorio.findByCiudadContainingIgnoreCaseAndIsDeletedFalse(ciudad)
            );
        }

        if (ciudadVacia) {
            return mapper.toResponseDtoList(
                    repositorio.findByNombreContainingIgnoreCaseAndIsDeletedFalse(nombre)
            );
        }

        return mapper.toResponseDtoList(
                repositorio.findByCiudadContainingIgnoreCaseAndNombreContainingIgnoreCaseAndIsDeletedFalse(ciudad, nombre)
        );
    }

    // ============================
    // FIND BY ID
    // ============================

    @Override
    @Cacheable(key = "#id")
    public InstitutoResponseDto findById(Long id) {
        Instituto instituto = repositorio.findById(id)
                .filter(i -> !i.getIsDeleted())
                .orElseThrow(() -> new InstitutoNotFoundException(id));

        return mapper.toInstitutoResponseDto(instituto);
    }

    // ============================
    // FIND BY UUID
    // ============================

    @Override
    @Cacheable(key = "#uuid")
    public InstitutoResponseDto findByUuid(String uuid) {
        try {
            UUID parsed = UUID.fromString(uuid);
            Instituto instituto = repositorio.findByUuidAndIsDeletedFalse(parsed)
                    .orElseThrow(() -> new InstitutoNotFoundException(parsed));

            return mapper.toInstitutoResponseDto(instituto);

        } catch (IllegalArgumentException e) {
            throw new InstitutoBadUuidException(uuid);
        }
    }

    // ============================
    // CREATE
    // ============================

    @Override
    @CachePut(key = "#result.id")
    public InstitutoResponseDto save(InstitutoCreateDto dto) {
        Instituto instituto = mapper.toInstituto(dto);
        return mapper.toInstitutoResponseDto(repositorio.save(instituto));
    }

    // ============================
    // UPDATE
    // ============================

    @Override
    @CachePut(key = "#result.id")
    public InstitutoResponseDto update(Long id, InstitutoUpdateDto dto) {
        Instituto existente = repositorio.findById(id)
                .filter(i -> !i.getIsDeleted())
                .orElseThrow(() -> new InstitutoNotFoundException(id));

        Instituto actualizado = mapper.toInstituto(dto, existente);
        return mapper.toInstitutoResponseDto(repositorio.save(actualizado));
    }

    // ============================
    // DELETE (SOFT)
    // ============================

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        if (!repositorio.existsById(id)) {
            throw new InstitutoNotFoundException(id);
        }
        repositorio.updateIsDeletedToTrueById(id);
    }
}
