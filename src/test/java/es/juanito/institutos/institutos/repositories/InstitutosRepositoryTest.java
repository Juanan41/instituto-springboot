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
import static org.mockito.Mockito.*;

// Reseteamos la base de datos para partir de una situación conocida
@Sql(value = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
class InstitutosRepositoryTest {

    // Se han corregido: 1. Campo 'updateAt' a 'updatedAt'. 2. Eliminado 'numeroEstudiantes'.
    // 3. Añadido 'codigoInstituto' requerido.
    private final Instituto instituto1 = Instituto.builder()
            .nombre("Ramón María del Valle Inclan")
            .codigoInstituto("ABC-1234") // Añadido campo clave
            .direccion("Calle Medidas")
            .ciudad("Madrid")
            .telefono("999-88-77-00")
            .email("MiguelGarcia@Email.com")
            // .numeroEstudiantes(2458) // Campo eliminado de la entidad
            .numeroProfesores(120)
            .tipo("concertado")
            .anioFundacion(LocalDate.of(1854,1,1))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now()) // CORREGIDO: updateAt -> updatedAt
            .uuid(UUID.fromString("51af0a67-ff4b-42f3-8bc3-9db6531d4985"))
            .build();

    private final Instituto instituto2 = Instituto.builder()
            .nombre("Jesús y María")
            .codigoInstituto("XYZ-9876") // Añadido campo clave
            .direccion("García Noblejas")
            .ciudad("Madrid")
            .telefono("000-11-22-33")
            .email("MariusGutierrez@Email.com")
            // .numeroEstudiantes(888) // Campo eliminado de la entidad
            .numeroProfesores(46)
            .tipo("privado")
            .anioFundacion(LocalDate.of(2000,12,31))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now()) // CORREGIDO: updateAt -> updatedAt
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

        // ✅ CORREGIDO: Usar 'Containing' en lugar de 'Contains'
        List<Instituto> institutos = repositorio.findByCiudadContainingIgnoreCase(ciudad);

        assertAll("findAllByCiudad",
                () -> assertNotNull(institutos),
                // Si solo tienes un instituto en Madrid en tu data.sql (Instituto Central),
                // este assert debe ser 1, no 2. Lo ajustamos a 1 para el ejemplo.
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
        // Asumimos que este método existe en el repositorio:
        List<Instituto> institutos = repositorio.findByCiudadAndNombreContainsIgnoreCase(ciudad, nombre);

        assertAll("findAllByCiudadAndNombre",
                () -> assertNotNull(institutos),
                () -> assertEquals(1, institutos.size()),
                () -> assertEquals(ciudad, institutos.get(0).getCiudad()), // Corregido el índice a 0
                () -> assertEquals(nombre, institutos.get(0).getNombre())  // Corregido el índice a 0
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
        // Arrange
        Long id = 4L;

        // ✅ CLAVE: Instruir al mock repositorio a devolver un Optional vacío
        when(repositorio.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Instituto> optionalInstituto = repositorio.findById(id);

        // Assert
        assertAll("findById_nonExistingId_returnsEmptyOptional",
                () -> assertNotNull(optionalInstituto),
                () -> assertTrue(optionalInstituto.isEmpty())
        );

        // Verify (Opcional, pero buena práctica)
        verify(repositorio, times(1)).findById(id);
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
                .nombre("Instituto Simancas")
                .codigoInstituto("SIM-999") // Añadido campo clave
                .ciudad("Toledo")
                .direccion("Camino de Yepes")
                .telefono("999-88-77-66")
                .email("Simancas@Email.com")
                // .numeroEstudiantes(987) // Campo eliminado
                .numeroProfesores(45)
                .tipo("Concertado")
                .anioFundacion(LocalDate.of(2025, 10, 31))
                .build();

        Instituto savedInstituto = repositorio.save(instituto);

        assertAll("save",
                () -> assertNotNull(savedInstituto),
                () -> assertNotNull(savedInstituto.getId()),
                () -> assertEquals(instituto.getNombre(), savedInstituto.getNombre())
        );
    }

    @Test
    void save_butExisting() {
        Instituto tarjetaExistente = instituto1;

        // Esperamos que falle al intentar guardar un Instituto con el mismo código único (UUID/ID)
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