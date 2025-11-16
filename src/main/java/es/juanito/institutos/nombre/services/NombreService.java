package es.juanito.institutos.nombre.services;

import es.juanito.institutos.nombre.models.Nombre;
import es.juanito.institutos.nombre.dto.NombreRequestDto;

import java.util.List;

public interface NombreService {
    List<Nombre> findAll(String codigoInstituto);
    Nombre findByCodigoInstituto(String codigoInstituto);
    Nombre findById(Long id);
    Nombre save(NombreRequestDto nombreRequestDto);
    Nombre update(Long id, NombreRequestDto nombreRequestDto);
    void deleteById(Long id);
}
