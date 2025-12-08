package es.juanito.institutos.estudiantes.services;

import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EstudianteService {

    /**
     * Busca todos los estudiantes, permitiendo filtrar por código de instituto y/o nombre.
     * La firma debe coincidir con la llamada del Controlador.
     * @param codigoInstituto Código del instituto para filtrar (opcional).
     * @param nombre Nombre del estudiante para filtrar (opcional).
     * @return Lista de EstudianteRequestDto.
     */
    Page<EstudianteRequestDto> findAll(String codigoInstituto, String nombre, Pageable pageable);

    /**
     * Busca estudiantes por nombre, ignorando mayúsculas/minúsculas.
     * (Este método puede ser redundante si se usa el filtro 'nombre' en findAll,
     * pero se mantiene si se usa desde otras partes).
     * @param nombre Parte del nombre a buscar.
     * @return Lista de EstudianteRequestDto.
     */
    List<EstudianteRequestDto> findByNombre(String nombre);

    /**
     * Busca un estudiante por su código único de estudiante.
     * Se renombra el parámetro y el propósito para mayor claridad.
     * #@param codigoEstudiante Código único del estudiante.
     * @return EstudianteRequestDto.
     */
    EstudianteRequestDto findByCodigoEstudiante(String codigoEstudiante);

    /**
     * Busca un estudiante por su ID.
     * @param id ID del estudiante.
     * @return EstudianteRequestDto.
     */
    EstudianteRequestDto findById(Long id);

    /**
     * Crea un nuevo estudiante.
     * @param estudianteRequestDto Datos del estudiante.
     * @return EstudianteRequestDto del estudiante creado.
     */
    EstudianteRequestDto save(EstudianteRequestDto estudianteRequestDto);

    /**
     * Actualiza un estudiante existente.
     * @param id ID del estudiante a actualizar.
     * @param estudianteRequestDto Datos para la actualización.
     * @return EstudianteRequestDto del estudiante actualizado.
     */
    EstudianteRequestDto update(Long id, EstudianteRequestDto estudianteRequestDto);

    /**
     * Realiza un borrado lógico (soft delete) de un estudiante por ID.
     * @param id ID del estudiante a borrar.
     */
    void deleteById(Long id);
}