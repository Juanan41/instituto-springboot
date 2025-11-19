package es.juanito.institutos.institutos.services;

import es.juanito.institutos.estudiante.models.Estudiante;
import es.juanito.institutos.estudiante.services.EstudianteService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final EstudianteService estudianteService;

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
        return institutoMapper.toInstitutoResponseDto(
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
            return institutoMapper.toInstitutoResponseDto(
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


// Generamos estudiantes aleatorios
        List<Estudiante> estudiantesAleatorios = generarEstudiantesAleatorios(5);

// Creamos la entidad Instituto usando builder
        Instituto nuevoInstituto = Instituto.builder()
                .nombre(institutoCreateDto.getNombre())
                .ciudad(institutoCreateDto.getCiudad())
                .direccion(institutoCreateDto.getDireccion())
                .telefono(institutoCreateDto.getTelefono())
                .email(institutoCreateDto.getEmail())
                .estudiantes(estudiantesAleatorios)
                .numeroProfesores(institutoCreateDto.getNumeroProfesores())
                .tipo(institutoCreateDto.getTipo())
                .anioFundacion(institutoCreateDto.getAnioFundacion())
                .codigoInstituto(institutoCreateDto.getCodigoInstituto())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .build();

// Guardamos en la base de datos y devolvemos el DTO
        return institutoMapper.toInstitutoResponseDto(institutosRepository.save(nuevoInstituto));

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
        return institutoMapper.toInstitutoResponseDto(institutosRepository.save(institutoActualizado));
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
    private List generarEstudiantesAleatorios(int n) {
        Random random = new Random();

        return IntStream.range(0, n)
                .mapToObj(i -> Estudiante.builder()
                        .id(null) // se asigna al guardar en DB
                        .nombre("Estudiante_" + UUID.randomUUID().toString().substring(0, 5))
                        .codigoInstituto("C-" + random.nextInt(1000))
                        .isDeleted(false)
                        .build())
                .collect(Collectors.toList());

    }



}
