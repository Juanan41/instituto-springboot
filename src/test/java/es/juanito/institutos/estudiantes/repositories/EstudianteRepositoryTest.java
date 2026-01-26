package es.juanito.institutos.estudiantes.repositories;

import es.juanito.institutos.rest.estudiantes.models.Estudiante;
import es.juanito.institutos.rest.estudiantes.repositories.EstudianteRepository;
import es.juanito.institutos.rest.institutos.models.Instituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never"
})
class EstudianteRepositoryTest {

    @Autowired
    private EstudianteRepository repositorio;

    @Autowired
    private TestEntityManager entityManager;

    private Instituto instituto;
    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        // =========================
        // INSTITUTO (OBLIGATORIO COMPLETO)
        // =========================
        instituto = entityManager.persist(
                Instituto.builder()
                        .nombre("IES Central")
                        .codigoInstituto("INT-0011")
                        .direccion("Calle Test")
                        .ciudad("Madrid")
                        .email("instituto@test.com")
                        .numeroProfesores(50)
                        .tipo("publico")
                        .anioFundacion(LocalDate.of(1995, 1, 1))
                        .telefono("600000000")        // por si en tu entidad es obligatorio
                        .uuid(UUID.randomUUID())
                        .createdAt(now)
                        .updatedAt(now)
                        .isDeleted(false)
                        .build()
        );

        // =========================
        // ESTUDIANTE (OBLIGATORIO COMPLETO)
        // =========================
        estudiante = entityManager.persist(
                Estudiante.builder()
                        .nombre("Jose")
                        .apellidos("García")
                        .dni("11111111J")
                        .email("jose@test.com")
                        .username("joseuser")
                        .password("123456")
                        .fechaNacimiento(LocalDate.of(2000, 1, 1))
                        .instituto(instituto)
                        .uuid(UUID.randomUUID())
                        .createdAt(now)
                        .updatedAt(now)
                        .isDeleted(false)
                        .build()
        );

        entityManager.flush();
        entityManager.clear();
    }

    // ---------- CRUD ----------

    @Test
    void findAll() {
        List<Estudiante> estudiantes = repositorio.findAll();
        assertFalse(estudiantes.isEmpty());
    }

    @Test
    void findById() {
        Optional<Estudiante> found = repositorio.findById(estudiante.getId());
        assertTrue(found.isPresent());
        assertEquals("Jose", found.get().getNombre());
    }

    @Test
    void findByIdNotFound() {
        assertTrue(repositorio.findById(999L).isEmpty());
    }

    @Test
    void save() {
        LocalDateTime now = LocalDateTime.now();

        Estudiante nuevo = Estudiante.builder()
                .nombre("Pepe")
                .apellidos("Ruiz")
                .dni("22222222P")
                .email("pepe@test.com")
                .username("pepeuser")
                .password("123456")
                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                .instituto(instituto)
                .uuid(UUID.randomUUID())
                .createdAt(now)
                .updatedAt(now)
                .isDeleted(false)
                .build();

        Estudiante saved = repositorio.save(nuevo);
        assertNotNull(saved.getId());
    }

    @Test
    void update() {
        estudiante.setNombre("Nombre Actualizado");
        Estudiante updated = repositorio.save(estudiante);
        assertEquals("Nombre Actualizado", updated.getNombre());
    }

    @Test
    void delete() {
        Long id = estudiante.getId();
        repositorio.deleteById(id);
        assertTrue(repositorio.findById(id).isEmpty());
    }

    // ---------- CONSULTAS ----------

    @Test
    void findByDni() {
        Optional<Estudiante> found = repositorio.findByDniEqualsIgnoreCase("11111111J");
        assertTrue(found.isPresent());
    }

    @Test
    void findByEmail() {
        Optional<Estudiante> found = repositorio.findByEmail("jose@test.com");
        assertTrue(found.isPresent());
    }

    @Test
    void findByNombre() {
        List<Estudiante> estudiantes = repositorio.findByNombreContainingIgnoreCase("jose");
        assertFalse(estudiantes.isEmpty());
    }

    @Test
    void findAllByInstitutoId() {
        List<Estudiante> estudiantes = repositorio.findAllByInstitutoId(instituto.getId());
        assertEquals(1, estudiantes.size());
    }
}
