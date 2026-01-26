package es.juanito.institutos.rest.institutos.services;

import es.juanito.institutos.rest.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoUpdateDto;


import java.util.List;


public interface InstitutosService {
    List<InstitutoResponseDto> findAll(String ciudad, String nombre);

    InstitutoResponseDto findById(Long id);

    InstitutoResponseDto findByUuid(String uuid);

    InstitutoResponseDto save(InstitutoCreateDto institutoCreateDto);

    InstitutoResponseDto update(Long id, InstitutoUpdateDto institutoUpdateDto);

    void deleteById(Long id);
}
