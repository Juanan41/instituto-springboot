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


@CacheConfig(cacheNames = {"institutos"})
@Slf4j
@RequiredArgsConstructor
@Service
public class InstitutosServiceImpl implements InstitutosService {
    private final InstitutosRepository institutosRepository;
    private final InstitutoMapper institutoMapper;


    @Override
    public List<InstitutoResponseDto> findAll(String ciudad, String nombre) {
        // Si todo está vacío o nulo, devolvemos todas los institutos
        if ((ciudad == null || ciudad.isEmpty()) && (nombre == null || nombre.isEmpty())) {
            log.info("Buscando todos los institutos");
            return institutoMapper.toResponseDtoList(institutosRepository.findAll());
        }
        // Si la ciudad no está vacía, pero el nombre si, buscamos por ciudad
        if ((ciudad != null && !ciudad.isEmpty()) && (nombre == null || nombre.isEmpty())) {
            log.info("Buscando institutos por ciudad: " + ciudad);
            return institutoMapper.toResponseDtoList(institutosRepository.findAllByCiudad(ciudad));
        }
        // Si la ciudad está vacía, pero el nombre no, buscamos por nombre
        if (ciudad == null || ciudad.isEmpty()) {
            log.info("Buscando institutos por nombre: " + nombre);
            return institutoMapper.toResponseDtoList(institutosRepository.findAllByNombre(nombre));
        }
        // Si el numero y el titular no están vacíos, buscamos por ambos
        log.info("Buscando institutos por ciudad: " + ciudad + " y nombre: " + nombre);
        return institutoMapper.toResponseDtoList(institutosRepository.findAllByCiudadAndNombre(ciudad, nombre));
    }
    // Cachea con el id como key
    @Override
    @Cacheable(key = "#id")
    public InstitutoResponseDto findById(Long id) {
        log.info("Buscando instituto por id {}", id);
        /*
        // Estilo estructurado
        Optional<Instituto> institutoEncontrado = institutosRepository.findById(id);
        if (institutoEncontrado.isPresent()) {
            return institutoEncontrado.get();
        } else {
            throw new InstitutoNotFoundException(id);
        }

         */
        // estilo funcional
        return institutoMapper.toinstitutoResponseDto(institutosRepository.findById(id)
                .orElseThrow(() -> new InstitutoNotFoundException(id)));
    }



    // Cachea con el uuid como key

    @Cacheable(key = "#uuid")
    @Override
    public InstitutoResponseDto findByUuid(String uuid) {
        log.info("Buscando instituto por uuid: {}" , uuid);
        try {
            var myUUID = UUID.fromString(uuid);
            return institutoMapper.toinstitutoResponseDto(institutosRepository.findByUuid(myUUID)
                    .orElseThrow(() -> new InstitutoNotFoundException(myUUID)));
        }catch (IllegalArgumentException e){
            throw new InstitutoBadUuidException(uuid);
        }
    }
    // Cachea con el id del resultado de la operación como key
    @Override
    @CachePut(key = "#result.id")
    public InstitutoResponseDto save(InstitutoCreateDto institutoCreateDto) {
        log.info("Guardando instituto: {}" , institutoCreateDto);
        // obtenemos id de instituto
        Long id = institutosRepository.nextId();
        // Creamos la tarjeta nueva con los datos que nos vienen
        Instituto nuevoInstituto = institutoMapper.toInstituto(id, institutoCreateDto);

        // La guardamos en el repositorio
        return institutoMapper.toinstitutoResponseDto(institutosRepository.save(nuevoInstituto));
    }

    @CachePut(key = "#result.id")
    @Override
    public InstitutoResponseDto update(Long id, InstitutoUpdateDto institutoUpdateDto) {
        log.info("Actualizando instituto por id: {}" , id);
        // Si no existe lanza excepción
        var institutoActual = institutosRepository.findById(id).orElseThrow(() -> new InstitutoNotFoundException(id));
        // Actualizamos el instituto con los datos que nos vienen
        Instituto institutoActualizado = institutoMapper.toInstituto(institutoUpdateDto, institutoActual);
        // La guardamos en el repositorio
        return institutoMapper.toinstitutoResponseDto(institutosRepository.save(institutoActualizado));
    }
    // El key es opcional, si no se indica
    @CacheEvict(key = "#id")
    @Override
    public void deleteById(Long id) {
        log.debug("Borrando instituto por id: {}" , id);
        institutosRepository.findById(id).orElseThrow(() -> new InstitutoNotFoundException(id));
        // La borramos del repositorio si existe
        institutosRepository.deleteById(id);

    }


}