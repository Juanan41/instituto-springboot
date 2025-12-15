package es.juanito.institutos.estudiantes.services;

import es.juanito.institutos.estudiantes.dto.EstudianteInfoResponseDto; // CORRECTO
import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;   // CORRECTO
import es.juanito.institutos.estudiantes.dto.EstudianteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interfaz que define la lógica de negocio para la gestión de Estudiantes.
 */
public interface EstudianteService { // Nombre en singular mantenido

    /**
     * Busca y pagina todos los estudiantes, permitiendo filtrar por username, email o estado de borrado.
     * La firma se adapta a los parámetros que el controlador de seguridad necesita.
     * @param username Nombre de usuario para filtrar (opcional).
     * @param email Email para filtrar (opcional).
     * @param isDeleted Estado de borrado lógico (opcional).
     * @param pageable Objeto de paginación y ordenación.
     * @return Página de EstudianteResponseDto (respuesta resumida).
     */
    Page<EstudianteResponseDto> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    /**
     * Busca un estudiante por su ID.
     * @param id ID del estudiante.
     * @return EstudianteInfoResponseDto (respuesta detallada).
     */
    EstudianteInfoResponseDto findById(Long id);

    /**
     * Crea un nuevo estudiante (usado por el registro).
     * @param estudianteRequestDto Datos del estudiante.
     * @return EstudianteResponseDto del estudiante creado.
     */
    EstudianteResponseDto save(EstudianteRequestDto estudianteRequestDto);

    /**
     * Actualiza un estudiante existente.
     * @param id ID del estudiante a actualizar.
     * @param estudianteRequestDto Datos para la actualización.
     * @return EstudianteResponseDto del estudiante actualizado.
     */
    EstudianteResponseDto update(Long id, EstudianteRequestDto estudianteRequestDto);

    /**
     * Realiza un borrado lógico (soft delete) de un estudiante por ID.
     * @param id ID del estudiante a borrar.
     */
    void deleteById(Long id);

    // NOTA: Se eliminaron los métodos findByNombre, findByCodigoEstudiante y findById
    // redundantes, y se corrigió el tipo de retorno de findAll.
}