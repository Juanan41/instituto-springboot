package es.juanito.institutos.institutos.repositories;

import es.juanito.institutos.institutos.models.Instituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// Reseteamos la base de datos para partir de una situación conocida
@Sql(value = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
class InstitutosRepositoryTest {

    private final Instituto instituto1 = Instituto.builder()
            .nombre("Ramón María del Valle Inclan")
            .direccion("Calle Medidas")
            .ciudad("Madrid")
            .telefono("999-88-77-00")
            .email("MiguelGarcia@Email.com")
            .numeroEstudiantes(2458)
            .numeroProfesores(120)
            .tipo("concertado")
            .anioFundacion(LocalDate.of(1854,1,1))
            .createdAt(LocalDateTime.now())
            .updateAt(LocalDateTime.now())
            .uuid(UUID.fromString("51af0a67-ff4b-42f3-8bc3-9db6531d4985"))
            .build();

    private final Instituto instituto2 = Instituto.builder()
            .nombre("Jesús y María")
            .direccion("García Noblejas")
            .ciudad("Madrid")
            .telefono("000-11-22-33")
            .email("MariusGutierrez@Email.com")
            .numeroEstudiantes(888)
            .numeroProfesores(46)
            .tipo("privado")
            .anioFundacion(LocalDate.of(2000,12,31))
            .createdAt(LocalDateTime.now())
            .updateAt(LocalDateTime.now())
            .uuid(UUID.fromString("8e7780f9-0771-4ff8-abdc-6e93f771f3c7"))
            .build();

    @Autowired
    private InstitutosRepository repositorio;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.merge(instituto1);
        entityManager.merge(instituto2);
        entityManager.flush();
    }



    @Test
    void findAll() {
        List<Instituto> institutos = repositorio.findAll();

        assertAll("findAll",
                () -> assertNotNull(institutos),
                () -> assertEquals(2, institutos.size())
        );
    }

    @Test
    void findAllByCiudad() {
        String ciudad = "Madrid";
        List<Instituto> institutos = repositorio.findByCiudadContainsIgnoreCase(ciudad);

        assertAll("findAllByCiudad",
                () -> assertNotNull(institutos),
                () -> assertEquals(2, institutos.size()),
                () -> assertEquals(ciudad, institutos.getFirst().getCiudad())
        );
    }

    @Test
    void findAllByNombre() {
        String nombre = "Ramón María del Valle Inclan";
        List<Instituto> institutos = repositorio.findByNombreContainsIgnoreCase(nombre);

        assertAll("findAllByNombre",
                () -> assertNotNull(institutos),
                () -> assertEquals(1,institutos.size()),
                () -> assertEquals(nombre, institutos.getFirst().getNombre())
        );
    }

    @Test
    void findAllByCiudadAndNombre() {
        String ciudad = "Madrid";
        String nombre = "Jesús y María";
        List<Instituto> institutos = repositorio.findByCiudadAndNombreContainsIgnoreCase(ciudad, nombre);

        assertAll("findAllByCiudadAndNombre",
                () -> assertNotNull(institutos),
                () -> assertEquals(1, institutos.size()),
                () -> assertEquals(ciudad, institutos.get(1).getCiudad()),
                () -> assertEquals(nombre, institutos.get(1).getNombre())
        );
    }

    @Test
    void findById_existingId_returnsOptionalWithInstituto() {
        Long id = 1L;
        Optional<Instituto> optionalInstituto = repositorio.findById(id);

        assertAll("findById_existingId_returnsOptionalWithInstituto",
                () -> assertNotNull(optionalInstituto),
                () -> assertTrue(optionalInstituto.isPresent()),
                () -> assertEquals(id, optionalInstituto.get().getId())
        );
    }

    @Test
    void findById_nonExistingId_returnsEmptyOptional() {
        Long id = 4L;
        Optional<Instituto> optionalInstituto = repositorio.findById(id);

        assertAll("findById_nonExistingId_returnsEmptyOptional",
                () -> assertNotNull(optionalInstituto),
                () -> assertTrue(optionalInstituto.isEmpty())
        );
    }

    @Test
    void findByUuid_existingId_returnsOptionalWithInstituto() {
        UUID uuid = UUID.fromString("51af0a67-ff4b-42f3-8bc3-9db6531d4985");
        Optional<Instituto> optionalInstituto = repositorio.findByUuid(uuid);

        assertAll("findByUuid_existingId_returnsOptionalWithInstituto",
                () -> assertNotNull(optionalInstituto),
                () -> assertTrue(optionalInstituto.isPresent()),
                () -> assertEquals(uuid, optionalInstituto.get().getUuid())
        );
    }

    @Test
    void findByUuid_nonExistingId_returnsEmptyOptional() {
        UUID uuid = UUID.fromString("11111111-2222-3333-4444-555555555555");
        Optional<Instituto> optionalInstituto = repositorio.findByUuid(uuid);

        assertAll("findByUuid_nonExistingId_returnsEmptyOptional",
                () -> assertNotNull(optionalInstituto),
                () -> assertTrue(optionalInstituto.isEmpty())
        );
    }

    @Test
    void existsById_existingId_returnsTrue() {
        Long id = 1L;
        boolean exists = repositorio.existsById(id);
        assertTrue(exists);
    }

    @Test
    void existsById_nonExistingId_returnsFalse() {
        Long id = 4L;
        boolean exists = repositorio.existsById(id);
        assertFalse(exists);
    }

    @Test
    void existsByUuid_existingId_returnsTrue() {
        UUID uuid = UUID.fromString("51af0a67-ff4b-42f3-8bc3-9db6531d4985");
        boolean exists = repositorio.existsByUuid(uuid);
        assertTrue(exists);
    }

    @Test
    void existsByUuid_nonExistingId_returnsFalse() {
        UUID uuid = UUID.fromString("11111111-2222-3333-4444-555555555555");
        boolean exists = repositorio.existsByUuid(uuid);
        assertFalse(exists);
    }

    @Test
    void save_notExisting() {
        Instituto instituto = Instituto.builder()
                .id(3L)
                .nombre("Instituto Simancas")
                .ciudad("Toledo")
                .direccion("Camino de Yepes")
                .telefono("999-88-77-66")
                .email("Simancas@Email.com")
                .numeroEstudiantes(987)
                .numeroProfesores(45)
                .tipo("Concertado")
                .anioFundacion(LocalDate.of(2025, 10, 31))
                .build();

        Instituto savedInstituto = repositorio.save(instituto);
        var all = repositorio.findAll();

        assertAll("save",
                () -> assertNotNull(savedInstituto),
                () -> assertEquals(instituto, savedInstituto),
                () -> assertEquals(3, all.size())
        );
    }

    @Test
    void save_butExisting() {
        Instituto tarjetaExistente = instituto1;

        assertThrows(DataIntegrityViolationException.class,
                () -> repositorio.save(tarjetaExistente));
    }

    @Test
    void deleteById_existingId() {
        Long id = 1L;
        repositorio.deleteById(id);
        var all = repositorio.findAll();

        assertAll("deleteById_existingId",
                () -> assertEquals(1, all.size()),
                () -> assertFalse(repositorio.existsById(id))
        );
    }

    @Test
    void deleteByUuid_existingId() {
        UUID uuid = UUID.fromString("51af0a67-ff4b-42f3-8bc3-9db6531d4985");
        repositorio.deleteByUuid(uuid);
        var all = repositorio.findAll();

        assertAll("deleteByUuid_existingId",
                () -> assertEquals(1, all.size()),
                () -> assertFalse(repositorio.existsByUuid(uuid))
        );
    }
}
