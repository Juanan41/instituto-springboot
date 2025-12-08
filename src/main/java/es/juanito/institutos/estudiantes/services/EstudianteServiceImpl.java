package es.juanito.institutos.estudiantes.services;

import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.exceptions.EstudianteConflictException;
import es.juanito.institutos.estudiantes.exceptions.EstudianteNotFoundException;
import es.juanito.institutos.estudiantes.mappers.EstudianteMapper;
import es.juanito.institutos.estudiantes.models.Estudiante;
import es.juanito.institutos.institutos.models.Instituto; // Importar la clase Instituto
import es.juanito.institutos.estudiantes.repositories.EstudianteRepository;
import es.juanito.institutos.institutos.repositories.InstitutosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page; // Nueva importación para paginación
import org.springframework.data.domain.Pageable; // Nueva importación para paginación
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = {"estudiantes"})
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteMapper estudianteMapper;
    private final InstitutosRepository institutosRepository;

    // --- Métodos Auxiliares ---

    /**
     * Busca un Estudiante por ID o lanza EstudianteNotFoundException.
     * @param id El ID del estudiante.
     * @return La entidad Estudiante.
     */
    private Estudiante getEstudiante(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new EstudianteNotFoundException(id));
    }

    /**
     * Busca un Instituto por código o lanza EstudianteBadRequestException (para FK).
     * @param codigoInstituto El código del instituto.
     * @return La entidad Instituto.
     */
    private Instituto getInstitutoByCodigo(String codigoInstituto) {
        return institutosRepository.findByCodigoInstituto(codigoInstituto)
                .orElseThrow(() -> new EstudianteConflictException("Código de Instituto '" + codigoInstituto + "' no encontrado."));
    }

    // --- Métodos de CRUD y Búsqueda ---

    /**
     * Obtiene todos los estudiantes con paginación y filtros opcionales por código de Instituto o nombre.
     *
     * @param codigoInstituto Código para filtrar (opcional).
     * @param nombre Nombre para filtrar (opcional).
     * @param pageable Objeto de paginación y ordenación de Spring.
     * @return Página de EstudianteRequestDto.
     */
    @Override
    public Page<EstudianteRequestDto> findAll(String codigoInstituto, String nombre, Pageable pageable) {
        log.info("Buscando estudiantes paginados, filtro: código={}, nombre={}, pageable={}", codigoInstituto, nombre, pageable);
        Page<Estudiante> estudiantesPage;

        // Limpiamos los filtros para facilitar las comprobaciones
        boolean hasCodigo = codigoInstituto != null && !codigoInstituto.trim().isEmpty();
        boolean hasNombre = nombre != null && !nombre.trim().isEmpty();

        if (!hasCodigo && !hasNombre) {
            // Caso 1: Sin filtros, trae todos
            estudiantesPage = estudianteRepository.findAll(pageable);
        } else if (hasCodigo && hasNombre) {
            // Caso 2: Filtrado por Instituto Y Nombre
            // NOTA: NECESITAS DEFINIR ESTE MÉTODO EN TU REPOSITORIO
            estudiantesPage = estudianteRepository.findByInstituto_CodigoInstitutoContainsIgnoreCaseAndNombreContainsIgnoreCase(
                    codigoInstituto, nombre, pageable
            );
        } else if (hasCodigo) {
            // Caso 3: Filtrado solo por Instituto
            // NOTA: NECESITAS DEFINIR ESTE MÉTODO EN TU REPOSITORIO
            estudiantesPage = estudianteRepository.findByInstituto_CodigoInstitutoContainsIgnoreCase(
                    codigoInstituto, pageable
            );
        } else { // if (hasNombre)
            // Caso 4: Filtrado solo por Nombre
            // NOTA: NECESITAS DEFINIR ESTE MÉTODO EN TU REPOSITORIO
            estudiantesPage = estudianteRepository.findByNombreContainsIgnoreCase(nombre, pageable);
        }

        // Mapear la página de Entidades (Estudiante) a la página de DTOs (EstudianteRequestDto)
        return estudiantesPage.map(estudianteMapper::toEstudianteRequestDto);
    }
    // El método findAll anterior (sin paginación) ya no es necesario o debería renombrarse:
    /*
    @Override
    public List<EstudianteRequestDto> findAll(String codigoInstituto, String nombre) {
        // ... Lógica anterior que devuelve List<EstudianteRequestDto> ...
    }
    */

    // El método findByNombre ya no es necesario si se usa el método findAll paginado
    // para todas las búsquedas de lista. Se mantiene si se requiere una búsqueda no paginada.

    @Override
    public List<EstudianteRequestDto> findByNombre(String nombre) {
        log.info("Buscando estudiantes por nombre: {}", nombre);
        return estudianteMapper.toRequestDtoList(estudianteRepository.findByNombreContainsIgnoreCase(nombre));
    }

    /**
     * Busca un estudiante por su identificador único de código de estudiante.
     * @param codigoEstudiante Código del estudiante.
     * @return EstudianteRequestDto.
     */
    @Override
    // NOTA: Se ha corregido el nombre del método para reflejar el uso de "código de estudiante"
    public EstudianteRequestDto findByCodigoEstudiante(String codigoEstudiante) {
        log.info("Buscando estudiante por código de estudiante: {}", codigoEstudiante);

        // En EstudianteServiceImpl.java (método findByCodigoEstudiante):
        Estudiante estudiante = estudianteRepository.findByDniEqualsIgnoreCase(codigoEstudiante)
                // ...
                .orElseThrow(() -> new EstudianteNotFoundException("Estudiante no encontrado con código: " + codigoEstudiante));

        // El DTO de retorno es el DTO de Request, como se ha indicado.
        return estudianteMapper.toEstudianteRequestDto(estudiante);
    }

    /**
     * Busca un estudiante por ID, usando caché.
     * @param id El ID del estudiante.
     * @return EstudianteRequestDto.
     */
    @Override
    @Cacheable(key = "#id")
    public EstudianteRequestDto findById(Long id) {
        log.info("Buscando estudiante por id: {}", id);
        return estudianteMapper.toEstudianteRequestDto(getEstudiante(id));
    }

    /**
     * Guarda un nuevo estudiante, actualizando la caché.
     * @param estudianteRequestDto Datos del estudiante.
     * @return EstudianteRequestDto del estudiante guardado.
     */
    @Override
    @CachePut(key = "#result.id")
    @Transactional
    public EstudianteRequestDto save(EstudianteRequestDto estudianteRequestDto) {
        log.info("Guardando estudiante: {}", estudianteRequestDto);

        // 1. Validar y obtener la entidad Instituto (FK)
        Instituto instituto = getInstitutoByCodigo(estudianteRequestDto.getCodigoInstituto());

        // 2. Mapear DTO a Entidad
        Estudiante nuevoEstudiante = estudianteMapper.toEstudiante(estudianteRequestDto, instituto);

        // 3. Guardar y devolver DTO de respuesta (EstudianteRequestDto)
        return estudianteMapper.toEstudianteRequestDto(estudianteRepository.save(nuevoEstudiante));
    }

    /**
     * Actualiza un estudiante existente, actualizando la caché.
     * @param id ID del estudiante a actualizar.
     * @param estudianteRequestDto Datos para la actualización.
     * @return EstudianteRequestDto del estudiante actualizado.
     */
    @Override
    @CachePut(key = "#result.id")
    @Transactional
    public EstudianteRequestDto update(Long id, EstudianteRequestDto estudianteRequestDto) {
        log.info("Actualizando estudiante id={} con datos: {}", id, estudianteRequestDto);

        // 1. Buscar el estudiante existente
        Estudiante estudianteActual = getEstudiante(id);

        // 2. Obtener el Instituto (puede cambiar)
        Instituto nuevoInstituto = getInstitutoByCodigo(estudianteRequestDto.getCodigoInstituto());

        // 3. Mapear DTO a Entidad, aplicando las actualizaciones
        Estudiante estudianteActualizado = estudianteMapper.toEstudiante(estudianteRequestDto, estudianteActual, nuevoInstituto);

        // 4. Guardar y devolver DTO de respuesta (EstudianteRequestDto)
        return estudianteMapper.toEstudianteRequestDto(estudianteRepository.save(estudianteActualizado));
    }

    /**
     * Realiza un borrado lógico (Soft Delete) de un estudiante y elimina la entrada de caché.
     * @param id ID del estudiante a borrar.
     */
    @Override
    @CacheEvict(key = "#id")
    @Transactional
    public void deleteById(Long id) {
        log.debug("Borrando (Soft Delete) estudiante por id: {}", id);

        // 1. Buscar si existe (lanza 404 si no)
        if (!estudianteRepository.existsById(id)) {
            throw new EstudianteNotFoundException(id);
        }

        // 2. Realizar el Soft Delete
        estudianteRepository.updateIsDeletedToTrueById(id);
    }
}