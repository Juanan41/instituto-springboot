package es.juanito.institutos.institutos.services;

import es.juanito.institutos.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.institutos.models.Instituto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface InstitutosService {
    List<InstitutoResponseDto> findAll(String ciudad, String nombre);

    InstitutoResponseDto findById(Long id);

    InstitutoResponseDto findByUuid(String uuid);

    InstitutoResponseDto save(InstitutoCreateDto institutoCreateDto);

    InstitutoResponseDto update(Long id, InstitutoUpdateDto institutoUpdateDto);

    void deleteById(Long id);
}
