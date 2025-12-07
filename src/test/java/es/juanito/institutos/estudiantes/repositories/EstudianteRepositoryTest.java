package es.juanito.institutos.estudiantes.repositories;

import es.juanito.institutos.estudiantes.models.Estudiante;
import es.juanito.institutos.institutos.models.Instituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Reseteamos la base de datos para partir de una situación conocida
// El script SQL se ejecuta antes de cada método (y DEBE crear el DDL)
@Sql(value = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
class EstudianteRepositoryTest {

    // 🗃️ Repositorio de Estudiante
    @Autowired
    private EstudianteRepository repositorio;

    // EntityManager para hacer las pruebas y asegurar la persistencia
    @Autowired
    private TestEntityManager entityManager;

    // 👤 Entidades de prueba
    private Instituto instituto1; // La FK para el estudiante

    private final Estudiante estudiante = Estudiante.builder()
            .nombre("Jose").apellidos("García").dni("11111111J")
            .email("jose.garcia@test.com").fechaNacimiento(LocalDate.of(2000, 1, 1))
            // El instituto se asigna en el setUp porque necesita el objeto persistido
            .build();

    @BeforeEach
    void setUp() {
        // 1. Asegurar la existencia del Instituto (FK) en la base de datos.
        // Lo persistimos aquí, ya que el script SQL solo crea el esquema,
        // o si el script inserta datos, este insertará un nuevo instituto.
        instituto1 = entityManager.persist(Instituto.builder()
                .nombre("IES Central").codigoInstituto("IC-01").build());

        // 2. Asignar la FK al Estudiante
        estudiante.setInstituto(instituto1);

        // 3. Insertamos el estudiante base antes de cada test (obtendrá ID=1L)
        entityManager.persist(estudiante);

        // Sincroniza los cambios en los objetos del contexto de persistencia con la BD
        entityManager.flush();
        entityManager.clear(); // Limpiamos para que las consultas lean de la BD real
    }

    // --- 1. CRUD BÁSICO ---

    @Test
    void findAll() {
        // Act
        // El findAll debe devolver AL MENOS el estudiante insertado en setUp
        List<Estudiante> estudiantes = repositorio.findAll();

        // Assert
        assertAll("findAll",
                () -> assertNotNull(estudiantes),
                () -> assertFalse(estudiantes.isEmpty())
        );
    }

    @Test
    void findByNombre() {
        // Act
        List<Estudiante> estudiantes = repositorio.findByNombreContainingIgnoreCase("Jose");

        // Assert
        assertAll("findAllByNombre",
                () -> assertNotNull(estudiantes),
                () -> assertFalse(estudiantes.isEmpty()),
                () -> assertEquals("Jose", estudiantes.getFirst().getNombre())
        );
    }

    @Test
    void findById() {
        // Act (Buscamos el estudiante que insertamos en setUp, que tendrá ID 1L)
        Optional<Estudiante> found = repositorio.findById(estudiante.getId());

        // Assert
        assertAll("findById",
                () -> assertTrue(found.isPresent()),
                () -> assertEquals("Jose", found.get().getNombre()),
                () -> assertEquals("11111111J", found.get().getDni())
        );
    }

    @Test
    void findByIdNotFound() {
        // Act
        Optional<Estudiante> found = repositorio.findById(100L);
        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    void save() {
        // Act
        Estudiante nuevo = Estudiante.builder()
                .nombre("Pepe").apellidos("Ruiz").dni("22222222P")
                .email("pepe.ruiz@save.com").fechaNacimiento(LocalDate.of(2003, 3, 3))
                .instituto(instituto1) // Usamos el instituto ya insertado
                .build();

        Estudiante saved = repositorio.save(nuevo);

        // Assert
        assertAll("save",
                () -> assertNotNull(saved),
                () -> assertEquals("Pepe", saved.getNombre()),
                () -> assertEquals("22222222P", saved.getDni())
        );
    }

    @Test
    void update() {
        // Arrange
        var existente = repositorio.findById(estudiante.getId()).orElseThrow();
        existente.setNombre("Pepe Actualizado");
        existente.setEmail("actualizado@update.com");

        // Act
        Estudiante updated = repositorio.save(existente);

        // Assert
        assertAll("update",
                () -> assertNotNull(updated),
                () -> assertEquals("Pepe Actualizado", updated.getNombre()),
                () -> assertEquals("actualizado@update.com", updated.getEmail())
        );
    }

    @Test
    void delete() {
        // Act
        var estudianteBorrar = repositorio.findById(estudiante.getId()).orElseThrow();
        repositorio.delete(estudianteBorrar);

        Optional<Estudiante> estudianteBorrado = repositorio.findById(estudiante.getId());

        // Assert
        assertTrue(estudianteBorrado.isEmpty());
    }

    // --- 2. TESTS DE BÚSQUEDA PERSONALIZADA (DNI, EMAIL, INSTITUTO) ---

    @Test
    void findByDni() {
        // Act
        Optional<Estudiante> found = repositorio.findByDniEqualsIgnoreCase("11111111J");

        // Assert
        assertAll("findByDni",
                () -> assertTrue(found.isPresent()),
                () -> assertEquals("Jose", found.get().getNombre())
        );
    }

    @Test
    void findByEmail() {
        // Act
        Optional<Estudiante> found = repositorio.findByEmail("jose.garcia@test.com");

        // Assert
        assertAll("findByEmail",
                () -> assertTrue(found.isPresent()),
                () -> assertEquals("11111111J", found.get().getDni())
        );
    }

    @Test
    void findAllByInstitutoId() {
        // Act
        List<Estudiante> estudiantes = repositorio.findAllByInstitutoId(instituto1.getId());

        // Assert
        assertAll("findAllByInstitutoId",
                () -> assertNotNull(estudiantes),
                () -> assertFalse(estudiantes.isEmpty()),
                () -> assertEquals(1, estudiantes.size()) // Solo el estudiante base insertado en setUp
        );
    }

    // Test de FetchType (Simula el del profesor para observar las queries)
    @Test
    void test_FetchType_LAZY() {
        // Vacía la cache del contexto de persistencia (L1 Cache) para forzar la BD
        entityManager.clear();

        // Act: Busca el estudiante. Si la relación es LAZY, solo hace una consulta aquí.
        Optional<Estudiante> found = repositorio.findById(estudiante.getId());

        // Assert
        assertAll("test_FetchType_LAZY",
                () -> assertTrue(found.isPresent()),
                // Si la relación Instituto -> Estudiantes es LAZY, acceder a found.get().getInstituto()
                // DENTRO de un @DataJpaTest puede generar la segunda consulta inmediatamente.
                () -> assertNotNull(found.get().getInstituto())
        );
    }
}