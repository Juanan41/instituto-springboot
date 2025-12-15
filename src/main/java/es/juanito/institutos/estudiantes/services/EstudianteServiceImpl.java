package es.juanito.institutos.estudiantes.services;

import es.juanito.institutos.estudiantes.dto.EstudianteInfoResponseDto;
import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.dto.EstudianteResponseDto;
import es.juanito.institutos.estudiantes.exceptions.EstudianteNotFoundException;
import es.juanito.institutos.estudiantes.mappers.EstudianteMapper;
import es.juanito.institutos.estudiantes.models.Estudiante;
import es.juanito.institutos.estudiantes.repositories.EstudianteRepository;
import es.juanito.institutos.institutos.models.Instituto;
import es.juanito.institutos.institutos.repositories.InstitutosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = {"estudiantes"})
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteMapper estudianteMapper;
    private final InstitutosRepository institutoRepository;

    // --- Métodos auxiliares ---
    private Estudiante getEstudiante(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new EstudianteNotFoundException(id));
    }

    private Instituto getInstitutoByCodigo(String codigoInstituto) {
        return institutoRepository.findByCodigoInstituto(codigoInstituto)
                .orElseThrow(() -> new EstudianteNotFoundException(
                        "Código de Instituto '" + codigoInstituto + "' no encontrado."
                ));
    }

    // --- Implementación de la interfaz ---

    @Override
    public Page<EstudianteResponseDto> findAll(Optional<String> username,
                                               Optional<String> email,
                                               Optional<Boolean> isDeleted,
                                               Pageable pageable) {
        Page<Estudiante> estudiantesPage;

        if (username.isEmpty() && email.isEmpty() && isDeleted.isEmpty()) {
            estudiantesPage = estudianteRepository.findAll(pageable);
        } else {
            // Adaptar a los métodos de filtrado de tu repositorio
            estudiantesPage = estudianteRepository.findByFilters(
                    username.orElse(null),
                    email.orElse(null),
                    isDeleted.orElse(null),
                    pageable
            );
        }

        return estudiantesPage.map(estudianteMapper::toResponseDto);
    }

    @Override
    @Cacheable(key = "#id")
    public EstudianteInfoResponseDto findById(Long id) {
        Estudiante estudiante = getEstudiante(id);
        return estudianteMapper.toInfoResponseDto(estudiante);
    }

    @Override
    @CachePut(key = "#result.id")
    @Transactional
    public EstudianteResponseDto save(EstudianteRequestDto estudianteRequestDto) {
        Instituto instituto = getInstitutoByCodigo(estudianteRequestDto.getCodigoInstituto());
        Estudiante estudiante = estudianteMapper.toEstudiante(estudianteRequestDto, instituto);

        Estudiante saved = estudianteRepository.save(estudiante);
        return estudianteMapper.toResponseDto(saved);
    }

    @Override
    @CachePut(key = "#result.id")
    @Transactional
    public EstudianteResponseDto update(Long id, EstudianteRequestDto estudianteRequestDto) {
        Estudiante estudianteActual = getEstudiante(id);
        Instituto instituto = getInstitutoByCodigo(estudianteRequestDto.getCodigoInstituto());

        Estudiante estudianteActualizado = estudianteMapper.toEstudiante(estudianteRequestDto, estudianteActual, instituto);
        Estudiante saved = estudianteRepository.save(estudianteActualizado);

        return estudianteMapper.toResponseDto(saved);
    }

    @Override
    @CacheEvict(key = "#id")
    @Transactional
    public void deleteById(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new EstudianteNotFoundException(id);
        }
        estudianteRepository.updateIsDeletedToTrueById(id);
    }
}
