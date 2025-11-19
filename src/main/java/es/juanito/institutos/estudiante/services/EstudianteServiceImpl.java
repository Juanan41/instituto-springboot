package es.juanito.institutos.estudiante.services;

import es.juanito.institutos.estudiante.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiante.exceptions.EstudianteException;
import es.juanito.institutos.estudiante.mappers.EstudianteMapper;
import es.juanito.institutos.estudiante.models.Estudiante;
import es.juanito.institutos.estudiante.repositories.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteMapper estudianteMapper;

    @Override
    public List<Estudiante> findAll(String codigoInstituto) {
        if (codigoInstituto == null || codigoInstituto.isEmpty()) {
            return estudianteRepository.findAll();
        } else {
            return estudianteRepository.findByCodigoInstitutoContainingIgnoreCase(codigoInstituto);
        }
    }

    @Override
    public Estudiante findByCodigoInstituto(String codigoInstituto) {
        return estudianteRepository.findByCodigoInstitutoEqualsIgnoreCase(codigoInstituto)
                .orElseThrow(() -> new EstudianteException("No se encontró el estudiante: " + codigoInstituto));
    }

    @Override
    public Estudiante findById(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new EstudianteException("No se encontró el estudiante con id: " + id));
    }

    @Override
    public Estudiante save(EstudianteRequestDto estudianteRequestDto) {
        return estudianteRepository.save(estudianteMapper.toEstudiante(estudianteRequestDto));
    }

    @Override
    public Estudiante update(Long id, EstudianteRequestDto estudianteRequestDto) {
        Estudiante estudianteActual = findById(id);
        return estudianteRepository.save(estudianteMapper.toEstudiante(estudianteRequestDto, estudianteActual));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Estudiante estudiante = findById(id);
        // Para evitar borrado físico, lo marcamos como borrado
        estudiante.setIsDeleted(true);
        estudianteRepository.save(estudiante);
    }
}
