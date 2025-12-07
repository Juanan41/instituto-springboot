package es.juanito.institutos.institutos.services;

import es.juanito.institutos.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.institutos.exceptions.InstitutoBadUuidException;
import es.juanito.institutos.institutos.exceptions.InstitutoNotFoundException;
import es.juanito.institutos.institutos.mappers.InstitutoMapper;
import es.juanito.institutos.institutos.models.Instituto;
import es.juanito.institutos.institutos.repositories.InstitutosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importación necesaria

import java.util.List;
import java.util.UUID;

@CacheConfig(cacheNames = {"institutos"})
@Slf4j
@RequiredArgsConstructor
@Service
public class InstitutosServiceImpl implements InstitutosService {
    private final InstitutosRepository institutosRepository;
    private final InstitutoMapper institutoMapper;

    @Override
    public List<InstitutoResponseDto> findAll(String ciudad, String nombre) {
        if ((ciudad == null || ciudad.isEmpty()) && (nombre == null || nombre.isEmpty())) {
            log.info("Buscando todos los institutos");
            return institutoMapper.toResponseDtoList(institutosRepository.findAll());
        }
        if ((ciudad != null && !ciudad.isEmpty()) && (nombre == null || nombre == null)) {
            log.info("Buscando institutos por ciudad: " + ciudad);
            return institutoMapper.toResponseDtoList(institutosRepository.findByCiudad(ciudad));
        }
        if (ciudad == null || ciudad.isEmpty()) {
            log.info("Buscando institutos por nombre: " + nombre);
            return institutoMapper.toResponseDtoList(institutosRepository. findByNombreContainsIgnoreCase(nombre));
        }
        log.info("Buscando institutos por ciudad: " + ciudad + " y nombre: " + nombre);
        return institutoMapper.toResponseDtoList(institutosRepository.findByCiudadAndNombreContainsIgnoreCase(ciudad, nombre));
    }

    @Override
    @Cacheable(key = "#id")
    public InstitutoResponseDto findById(Long id) {
        log.info("Buscando instituto por id {}", id);
        return institutoMapper.toInstitutoResponseDto(institutosRepository.findById(id)
                .orElseThrow(() -> new InstitutoNotFoundException(id)));
    }

    @Cacheable(key = "#uuid")
    @Override
    public InstitutoResponseDto findByUuid(String uuid) {
        log.info("Buscando instituto por uuid: {}", uuid);
        try {
            var myUUID = UUID.fromString(uuid);
            return institutoMapper.toInstitutoResponseDto(institutosRepository.findByUuid(myUUID)
                    .orElseThrow(() -> new InstitutoNotFoundException(myUUID)));
        } catch (IllegalArgumentException e) {
            throw new InstitutoBadUuidException(uuid);
        }
    }

    @Override
    @CachePut(key = "#result.id")
    public InstitutoResponseDto save(InstitutoCreateDto institutoCreateDto) {
        log.info("Guardando instituto: {}", institutoCreateDto);

        // ❌ ERROR CORREGIDO: ELIMINAMOS EL ARGUMENTO 'null' EN EL MAPPER.
        // El método toInstituto en el mapper debe aceptar el DTO de creación.
        Instituto nuevoInstituto = institutoMapper.toInstituto(institutoCreateDto);

        return institutoMapper.toInstitutoResponseDto(institutosRepository.save(nuevoInstituto));
    }

    @CachePut(key = "#result.id")
    @Override
    public InstitutoResponseDto update(Long id, InstitutoUpdateDto institutoUpdateDto) {
        log.info("Actualizando instituto por id: {}", id);
        var institutoActual = institutosRepository.findById(id)
                .orElseThrow(() -> new InstitutoNotFoundException(id));

        Instituto institutoActualizado = institutoMapper.toInstituto(institutoUpdateDto, institutoActual);
        return institutoMapper.toInstitutoResponseDto(institutosRepository.save(institutoActualizado));
    }

    @CacheEvict(key = "#id")
    @Override
    // ✅ CORRECCIÓN CRÍTICA: Se debe usar @Transactional para modificar datos con consultas JPA personalizadas
    @Transactional
    public void deleteById(Long id) {
        log.debug("Borrando instituto por id: {}", id);

        // ✅ CORRECCIÓN CRÍTICA: No se usa delete(instituto) si se quiere hacer Soft Delete.
        // El método de soft delete está en el repositorio.
        if (!institutosRepository.existsById(id)) {
            throw new InstitutoNotFoundException(id);
        }
        institutosRepository.updateIsDeletedToTrueById(id);
        // Nota: Si el controlador lanza una excepción 404, asegúrate de que findById() no esté cacheado,
        // o que tu lógica de borrado no muestre entidades borradas (usando isDeletedFalse en findAll, etc.).
    }
}