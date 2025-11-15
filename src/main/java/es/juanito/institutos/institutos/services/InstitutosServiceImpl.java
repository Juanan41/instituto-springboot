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

import java.util.List;
import java.util.UUID;

/**
 * Servicio que implementa la lógica de negocio para Institutos.
 * Adaptado para trabajar con la nueva estructura del repositorio.
 */
@CacheConfig(cacheNames = {"institutos"})
@Slf4j
@RequiredArgsConstructor
@Service
public class InstitutosServiceImpl implements InstitutosService {
    private final InstitutosRepository institutosRepository;
    private final InstitutoMapper institutoMapper;


    /**
     * Busca institutos aplicando filtros opcionales de ciudad y nombre.
     */
    @Override
    public List<InstitutoResponseDto> findAll(String ciudad, String nombre) {
        // Si no hay filtros, devolvemos todos los institutos no eliminados
        if ((ciudad == null || ciudad.isEmpty()) && (nombre == null || nombre.isEmpty())) {
            log.info("Buscando todos los institutos");
            return institutoMapper.toResponseDtoList(institutosRepository.findAll());
        }

        // Solo filtrando por ciudad
        if ((ciudad != null && !ciudad.isEmpty()) && (nombre == null || nombre.isEmpty())) {
            log.info("Buscando institutos por ciudad: {}", ciudad);
            return institutoMapper.toResponseDtoList(
                    institutosRepository.findByCiudad(ciudad));
        }

        // Solo filtrando por nombre
        if (ciudad == null || ciudad.isEmpty()) {
            log.info("Buscando institutos por nombre: {}", nombre);
            return institutoMapper.toResponseDtoList(
                    institutosRepository.findByNombreContainsIgnoreCase(nombre));
        }

        // Filtrando por ciudad y nombre
        log.info("Buscando institutos por ciudad: {} y nombre: {}", ciudad, nombre);
        return institutoMapper.toResponseDtoList(
                institutosRepository.findByCiudadAndNombreContainsIgnoreCase(ciudad, nombre));
    }

    /**
     * Busca un instituto por ID.
     * Cachea con el ID como key.
     */
    @Override
    @Cacheable(key = "#id")
    public InstitutoResponseDto findById(Long id) {
        log.info("Buscando instituto por id {}", id);

        // Estilo funcional: devuelve el DTO o lanza excepción si no existe o está borrado
        return institutoMapper.toinstitutoResponseDto(
                institutosRepository.findById(id)
                        .orElseThrow(() -> new InstitutoNotFoundException(id)));
    }

    /**
     * Busca un instituto por UUID.
     * Cachea con el UUID como key.
     */
    @Override
    @Cacheable(key = "#uuid")
    public InstitutoResponseDto findByUuid(String uuid) {
        log.info("Buscando instituto por uuid: {}", uuid);
        try {
            UUID myUUID = UUID.fromString(uuid);
            return institutoMapper.toinstitutoResponseDto(
                    institutosRepository.findByUuid(myUUID)
                            .orElseThrow(() -> new InstitutoNotFoundException(myUUID)));
        } catch (IllegalArgumentException e) {
            throw new InstitutoBadUuidException(uuid);
        }
    }

    /**
     * Guarda un nuevo instituto.
     * Cachea con el ID del instituto guardado.
     */
    @Override
    @CachePut(key = "#result.id")
    public InstitutoResponseDto save(InstitutoCreateDto institutoCreateDto) {
        log.info("Guardando instituto: {}", institutoCreateDto);

        // Creamos la entidad desde el DTO
        Instituto nuevoInstituto = institutoMapper.toInstituto(institutoCreateDto);

        // Guardamos en la base de datos y devolvemos el DTO
        return institutoMapper.toinstitutoResponseDto(institutosRepository.save(nuevoInstituto));
    }

    /**
     * Actualiza un instituto existente.
     * Cachea con el ID del instituto actualizado.
     */
    @Override
    @CachePut(key = "#result.id")
    public InstitutoResponseDto update(Long id, InstitutoUpdateDto institutoUpdateDto) {
        log.info("Actualizando instituto por id: {}", id);

        // Buscamos el instituto o lanzamos excepción si no existe
        var institutoActual = institutosRepository.findById(id).orElseThrow(()-> new InstitutoNotFoundException(id));

        // Actualizamos la entidad con los datos del DTO
        Instituto institutoActualizado = institutoMapper.toInstituto(institutoUpdateDto, institutoActual);

        // Guardamos cambios y devolvemos DTO
        return institutoMapper.toinstitutoResponseDto(institutosRepository.save(institutoActualizado));
    }

    /**
     * Elimina un instituto por ID (soft delete).
     * Evita la caché con ese ID.
     */
    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        log.debug("Borrando instituto por id: {}", id);
        // Si no existe lanza excepción
        institutosRepository.findById(id).orElseThrow(()-> new InstitutoNotFoundException(id));
        // La borramos del repositorio si existe
        institutosRepository.deleteById(id);
        // O lo marcamos como borrado
        //institutosRepository.updateIsDeletedToTrueById(id);

    }


}
