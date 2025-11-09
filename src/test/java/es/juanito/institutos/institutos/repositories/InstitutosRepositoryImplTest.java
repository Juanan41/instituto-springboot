package es.juanito.institutos.institutos.repositories;

import es.juanito.institutos.institutos.models.Instituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InstitutosRepositoryImplTest {

    private final Instituto instituto1 = Instituto.builder()
            .id(1L)
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
            .id(2L)
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

    private InstitutosRepositoryImpl repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new InstitutosRepositoryImpl();
        repositorio.save(instituto1);
        repositorio.save(instituto2);
    }

    @Test
    void findAll() {
        // Act
        List<Instituto> institutos = repositorio.findAll();

        // assert
        assertAll("findAll",
                () -> assertNotNull(institutos),
                () -> assertEquals(2, institutos.size())
                );
    }

    @Test
    void findAllByCiudad() {
        // Act
        String ciudad = "Madrid";
        List<Instituto> institutos = repositorio.findAllByCiudad(ciudad);

        // Assert
        assertAll("findAllByCiudad",
                () -> assertNotNull(institutos),
                () -> assertEquals(2, institutos.size()),
                () -> assertEquals(ciudad, institutos.getFirst().getCiudad())
        );
    }

    @Test
    void findAllByNombre() {
        // Act
        String nombre = "Ramón María del Valle Inclan";
        List<Instituto> institutos = repositorio.findAllByNombre(nombre);

        // Assert
        assertAll("findAllByNombre",
                () -> assertNotNull(institutos),
                () -> assertEquals(1,institutos.size()),
                () -> assertEquals(nombre, institutos.getFirst().getNombre())
        );
    }

    @Test
    void findAllByCiudadAndNombre() {
        // Act
        String ciudad = "Madrid";
        String nombre = "Jesús y María";
        List<Instituto> institutos = repositorio.findAllByCiudadAndNombre(ciudad, nombre);
        // Assert
        assertAll(
                () -> assertNotNull(institutos),
                () -> assertEquals(1, institutos.size()),
                () -> assertEquals(ciudad, institutos.getFirst().getCiudad())
        );
    }

    @Test
    void findById_existingId_returnsOptionalWithInstituto() {
        // Act
        Long id = 1L;
        Optional<Instituto> optionalInstituto = repositorio.findById(id);

        // Assert
        assertAll("findById_existingId_returnsOptionalWithInstituto",
                () -> assertNotNull(optionalInstituto),
                () -> assertTrue(optionalInstituto.isPresent()),
                () -> assertEquals(id, optionalInstituto.get().getId())
        );
    }

    @Test
    void findById_nonExistingId_returnsEmptyOptional() {
        // Act
        Long id = 4L;
        Optional<Instituto> optionalInstituto = repositorio.findById(id);

        //Assert
        assertAll("findById_nonExistingId_returnsEmptyOptional",
                () -> assertNotNull(optionalInstituto),
                () -> assertTrue(optionalInstituto.isEmpty())
        );

    }

    @Test
    void findByUuid_existingId_returnsOptionalWithInstituto() {
        // Act
        UUID uuid = UUID.fromString("51af0a67-ff4b-42f3-8bc3-9db6531d4985");
        Optional<Instituto> optionalInstituto = repositorio.findByUuid(uuid);

        // Assert
        assertAll("findByUuid_existingId_returnsOptionalWithInstituto",
                () -> assertNotNull(optionalInstituto),
                () -> assertTrue(optionalInstituto.isPresent()),
                () -> assertEquals(uuid, optionalInstituto.get().getUuid())
        );
    }

    @Test
    void findByUuid_nonExistingId_returnsEmptyOptional() {
        // Act
        UUID uuid = UUID.randomUUID(); // un UUID que no está en el repositorio
        Optional<Instituto> optionalInstituto = repositorio.findByUuid(uuid);


        // Assert
        assertAll("findByUuid_nonExistingId_returnsEmptyOptional",
                () -> assertNotNull(optionalInstituto),
                () -> assertTrue(optionalInstituto.isEmpty())
        );
    }

    @Test
    void existsById_existingId_returnsTrue() {
        // Act
        Long id = 1L;
        boolean exists = repositorio.existsById(id);

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_nonExistingId_returnsFalse() {
        // Act
        Long id = 4L;
        boolean exists = repositorio.existsById(id);

        // Assert
        assertFalse(exists);
    }


    @Test
    void existsByUuid_existingId_returnsTrue() {
        // Act
        UUID uuid = UUID.fromString("51af0a67-ff4b-42f3-8bc3-9db6531d4985");
        boolean exists = repositorio.existsByUuid(uuid);

        // Assert
        assertAll("existsByUuid_existingId_returnsTrue",
                () -> assertTrue(exists, "El instituto existe")
        );
    }


    @Test

    void existsByUuid_nonExistingId_returnsFalse() {
        // Act
        UUID uuid = UUID.fromString("11111111-2222-3333-4444-555555555555");
        boolean exists = repositorio.existsByUuid(uuid);

        // Assert
        assertFalse(exists);
    }

    @Test
    void save_notExisting() {
        // Arrange
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

        // Act
        Instituto savedInstituto = repositorio.save(instituto);
        var all = repositorio.findAll();

        // Assert
        assertAll("save",
                () -> assertNotNull(savedInstituto),
                () -> assertEquals(instituto, savedInstituto),
                () -> assertEquals(3, all.size())
        );
    }

    @Test
    void save_butExisting() {
        // Arrange
        Instituto instituto = Instituto.builder().id(1L).build();

        // Act
        Instituto savedInstituto = repositorio.save(instituto);
        var all = repositorio.findAll();

        //Assert
        assertAll("save",
                () -> assertNotNull(savedInstituto),
                () -> assertEquals(instituto, savedInstituto),
                () -> assertEquals(2, all.size())
        );
    }

    @Test
    void deleteById_existingId() {
        // Act
        Long id = 1L;
        repositorio.deleteById(id);
        var all = repositorio.findAll();

        // Assert
        assertAll("deleteById_existingId",
                () -> assertEquals(1, all.size()),
                () -> assertFalse(repositorio.existsById(id))
        );

    }

    @Test
    void deleteByUuid_existingId() {
        // Act
        UUID uuid = UUID.fromString("51af0a67-ff4b-42f3-8bc3-9db6531d4985");
        repositorio.deleteByUuid(uuid);
        var all = repositorio.findAll();

        // Assert
        assertAll("deleteByUuid_existingId",
                () -> assertEquals(1, all.size()),
                () -> assertFalse(repositorio.existsByUuid(uuid))
        );
    }

    @Test
    void nextId() {
        // Act
        Long nextId = repositorio.nextId();
        var all = repositorio.findAll();

        // Assert
        assertAll("nextId",
                ()-> assertEquals(3L,nextId),
                () -> assertEquals(2, all.size())
        );
    }
}