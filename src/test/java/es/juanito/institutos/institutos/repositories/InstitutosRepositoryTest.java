package es.juanito.institutos.institutos.repositories;

import es.juanito.institutos.rest.institutos.models.Instituto;
import es.juanito.institutos.rest.institutos.repositories.InstitutosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never"
})
class InstitutosRepositoryTest {

    @Autowired
    private InstitutosRepository repositorio;

    @Autowired
    private TestEntityManager entityManager;

    private Long institutoId;

    private final Instituto instituto = Instituto.builder()
            .nombre("Ramón María del Valle Inclán")
            .codigoInstituto("ABC-1234")
            .direccion("Calle Medidas")
            .ciudad("Madrid")
            .email("valle@correo.com")
            .numeroProfesores(120)
            .tipo("concertado")
            .anioFundacion(LocalDate.of(2000, 1, 1))
            .uuid(UUID.randomUUID())
            .build();

    @BeforeEach
    void setUp() {
        Instituto saved = entityManager.persist(instituto);
        entityManager.flush();
        entityManager.clear();
        institutoId = saved.getId();
    }

    // =========================
    // FIND
    // =========================

    @Test
    void findAll() {
        List<Instituto> institutos = repositorio.findAll();
        assertEquals(1, institutos.size());
    }

    @Test
    void findById_existing() {
        Optional<Instituto> found = repositorio.findById(institutoId);
        assertTrue(found.isPresent());
    }

    @Test
    void findById_notExisting() {
        Optional<Instituto> found = repositorio.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findByCodigoInstituto_existing() {
        Optional<Instituto> found =
                repositorio.findByCodigoInstituto("ABC-1234");
        assertTrue(found.isPresent());
    }


    // =========================
    // SAVE
    // =========================

    @Test
    void save_ok() {
        Instituto nuevo = Instituto.builder()
                .nombre("Instituto Simancas")
                .codigoInstituto("SIM-999")
                .direccion("Camino de Yepes")
                .ciudad("Toledo")
                .email("simancas@correo.com")
                .numeroProfesores(30)
                .tipo("publico")
                .anioFundacion(LocalDate.of(2020, 10, 31))
                .uuid(UUID.randomUUID())
                .build();

        Instituto saved = repositorio.saveAndFlush(nuevo);
        assertNotNull(saved.getId());
    }

    @Test
    void save_codigoInstitutoDuplicado_falla() {
        Instituto duplicado = Instituto.builder()
                .nombre("Duplicado")
                .codigoInstituto("ABC-1234")
                .direccion("Otra calle")
                .ciudad("Madrid")
                .email("duplicado@correo.com")
                .numeroProfesores(10)
                .tipo("privado")
                .anioFundacion(LocalDate.of(2022, 1, 1))
                .uuid(UUID.randomUUID())
                .build();

        assertThrows(
                DataIntegrityViolationException.class,
                () -> repositorio.saveAndFlush(duplicado)
        );
    }

    // =========================
    // DELETE
    // =========================

    @Test
    void deleteById() {
        repositorio.deleteById(institutoId);
        assertFalse(repositorio.existsById(institutoId));
    }
}
