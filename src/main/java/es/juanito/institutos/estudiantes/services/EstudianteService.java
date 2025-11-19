package es.juanito.institutos.estudiante.services;

import es.juanito.institutos.estudiante.models.Estudiante;
import es.juanito.institutos.estudiante.dto.EstudianteRequestDto;

import java.util.List;

public interface EstudianteService {
    List<Estudiante> findAll(String codigoInstituto);
    Estudiante findByCodigoInstituto(String codigoInstituto);
    Estudiante findById(Long id);
    Estudiante save(EstudianteRequestDto estudianteRequestDto);
    Estudiante update(Long id, EstudianteRequestDto estudianteRequestDto);
    void deleteById(Long id);
}
