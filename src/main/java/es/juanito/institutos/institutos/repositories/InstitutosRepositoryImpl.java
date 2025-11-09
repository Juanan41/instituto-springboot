package es.juanito.institutos.institutos.repositories;

import es.juanito.institutos.institutos.models.Instituto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Repository
public class InstitutosRepositoryImpl implements InstitutosRepository {
    private final Map<Long, Instituto> institutos = new LinkedHashMap<>(
            Map.of(
                1L, Instituto.builder()
                            .id(1L)
                            .nombre("Gomez Moreno")
                            .direccion("Calle albaida")
                            .telefono("777-88-99-00")
                            .email("Pepitolopez@Email.com")
                            .numeroEstudiantes(555)
                            .numeroProfesores(20)
                            .tipo("publico")
                            .anioFundacion(LocalDate.of(1983,12,19))
                            .codigoInstituto("4567-XXX")
                            .createdAt(LocalDateTime.now())
                            .updateAt(LocalDateTime.now())
                            .uuid(UUID.randomUUID())
                            .build(),
                2L, Instituto.builder()
                            .id(2L)
                            .nombre("IES Francisco de Quevedo")
                            .direccion("Avenida de los poblados")
                            .telefono("888-99-00-11")
                            .email("ManolitaGomez@Email.com")
                            .numeroEstudiantes(1250)
                            .numeroProfesores(60)
                            .tipo("privado")
                            .anioFundacion(LocalDate.of(1956,6,9))
                            .codigoInstituto("6789-ZZZ")
                            .createdAt(LocalDateTime.now())
                            .updateAt(LocalDateTime.now())
                            .uuid(UUID.randomUUID())
                            .build()
            ));

    @Override
    public List<Instituto> findAll() {
        log.info("Buscando institutos");
        return institutos.values().stream()
                .toList();
    }

    @Override
    public List<Instituto> findAllByCiudad(String ciudad) {
        log.info("Buscando institutos por ciudad: {}" , ciudad);
        return institutos.values().stream()
                .filter(instituto -> instituto.getCiudad().toLowerCase().contains(ciudad.toLowerCase()))
                .toList();
    }

    @Override
    public List<Instituto> findAllByNombre(String nombre) {
        log.info("Buscando instiutos por nombre: {}" , nombre);
        return institutos.values().stream()
                .filter(instituto -> instituto.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    @Override
    public List<Instituto> findAllByCiudadAndNombre(String ciudad, String nombre) {
        log.info("Buscando institutos por ciudad: {} y nombre: {} ", ciudad, nombre);
        return institutos.values().stream()
                .filter(instituto -> instituto.getCiudad().toLowerCase().contains(ciudad.toLowerCase()))
                .filter(instituto -> instituto.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    @Override
    public Optional<Instituto> findById(Long id) {
        log.info("Buscando institutos por id: {}" , id);
        return institutos.get(id) != null ? Optional.of(institutos.get(id)) : Optional.empty();
    }

    @Override
    public Optional<Instituto> findByUuid(UUID uuid) {
        log.info("Buscando instituto por uuid: {}" , uuid);
        return institutos.values().stream()
                .filter(instituto -> instituto.getUuid().equals(uuid))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        log.info("Comprobando si existe instituto por id: {}" , id);
        return institutos.get(id) != null;
    }

    @Override
    public boolean existsByUuid(UUID uuid) {
        log.info("Comprobando si existe instituto por uuid: {}" , uuid);
        return institutos.values().stream()
                .anyMatch(instituto -> instituto.getUuid().equals(uuid));
    }

    @Override
    public Instituto save(Instituto instituto) {
        log.info("Guardando instituto: {}" , instituto);
        institutos.put(instituto.getId(), instituto);
        return instituto;
    }

    @Override
    public void deleteById(Long id) {
        log.info("Borrando tarjeta por id: {}" , id);
        institutos.remove(id);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        log.info("Borrando instituto por uuid: {}" , uuid);
        institutos.values().removeIf(instituto -> instituto.getUuid().equals(uuid));
    }

    @Override
    public Long nextId() {
        log.debug("Obteniendo siguiente id de instituto");
        return institutos.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0) + 1;
    }




}
