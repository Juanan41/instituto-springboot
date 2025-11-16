package es.juanito.institutos.nombre.services;

import es.juanito.institutos.nombre.dto.NombreRequestDto;
import es.juanito.institutos.nombre.exceptions.NombreConflictException;
import es.juanito.institutos.nombre.exceptions.NombreException;
import es.juanito.institutos.nombre.mappers.NombreMapper;
import es.juanito.institutos.nombre.models.Nombre;
import es.juanito.institutos.nombre.repositories.NombreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NombreServiceImpl implements NombreService {

    private final NombreRepository nombreRepository;
    private final NombreMapper nombreMapper;

    @Override
    public List<Nombre> findAll(String codigoInstituto) {
        if (codigoInstituto == null || codigoInstituto.isEmpty()) {
            return nombreRepository.findAll();
        } else {
            return nombreRepository.findByCodigoInstitutoContainingIgnoreCase(codigoInstituto);
        }
    }

    @Override
    public Nombre findByCodigoInstituto(String codigoInstituto) {
        return nombreRepository.findByCodigoInstitutoEqualsIgnoreCase(codigoInstituto)
                .orElseThrow(() -> new NombreException("No se encontró el nombre: " + codigoInstituto));
    }

    @Override
    public Nombre findById(Long id) {
        return nombreRepository.findById(id)
                .orElseThrow(() -> new NombreException("No se encontró el nombre con id: " + id));
    }

    @Override
    public Nombre save(NombreRequestDto nombreRequestDto) {
        return nombreRepository.save(nombreMapper.toNombre(nombreRequestDto));
    }

    @Override
    public Nombre update(Long id, NombreRequestDto nombreRequestDto) {
        Nombre nombreActual = findById(id);
        return nombreRepository.save(nombreMapper.toNombre(nombreRequestDto, nombreActual));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Nombre nombre = findById(id);
        // Para evitar borrado físico, lo marcamos como borrado
        nombre.setIsDeleted(true);
        nombreRepository.save(nombre);
    }
}
