package es.juanito.institutos.institutos.services;

import es.juanito.institutos.rest.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.rest.institutos.exceptions.InstitutoBadUuidException;
import es.juanito.institutos.rest.institutos.exceptions.InstitutoNotFoundException;
import es.juanito.institutos.rest.institutos.mappers.InstitutoMapper;
import es.juanito.institutos.rest.institutos.models.Instituto;
import es.juanito.institutos.rest.institutos.repositories.InstitutosRepository;
import es.juanito.institutos.rest.institutos.services.InstitutosServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutosServiceImplTest {

    private Instituto instituto1;
    private Instituto instituto2;
    private InstitutoResponseDto institutoResponse1;

    @Mock
    private InstitutosRepository institutosRepository;

    @Spy
    private InstitutoMapper institutoMapper;

    @InjectMocks
    private InstitutosServiceImpl institutosService;

    @Captor
    private ArgumentCaptor<Instituto> institutoCaptor;

    @BeforeEach
    void setUp() {
        instituto1 = Instituto.builder()
                .id(1L)
                .nombre("Ramón María del Valle Inclán")
                .direccion("Calle Medidas")
                .ciudad("Madrid")
                .email("valle@correo.com")
                .numeroProfesores(120)
                .tipo("concertado")
                .anioFundacion(LocalDate.of(2000, 1, 1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .build();

        instituto2 = Instituto.builder()
                .id(2L)
                .nombre("Jesús y María")
                .direccion("García Noblejas")
                .ciudad("Madrid")
                .email("jesus@correo.com")
                .numeroProfesores(46)
                .tipo("privado")
                .anioFundacion(LocalDate.of(2010, 12, 11))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .build();

        institutoResponse1 = institutoMapper.toInstitutoResponseDto(instituto1);
    }

    // ============================
    // FIND ALL
    // ============================

    @Test
    void findAll_sinFiltros() {
        when(institutosRepository.findAll())
                .thenReturn(List.of(instituto1, instituto2));

        List<InstitutoResponseDto> result =
                institutosService.findAll(null, null);

        assertThat(result).hasSize(2);
        verify(institutosRepository).findAll();
    }

    @Test
    void findAll_porNombre() {
        // Arrange
        String nombre = "Ramón";

        when(institutosRepository
                .findByNombreContainingIgnoreCaseAndIsDeletedFalse(nombre))
                .thenReturn(List.of(instituto1));

        // Act
        List<InstitutoResponseDto> result =
                institutosService.findAll(null, nombre);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(instituto1.getNombre(), result.get(0).getNombre());

        // Verify
        verify(institutosRepository, times(1))
                .findByNombreContainingIgnoreCaseAndIsDeletedFalse(nombre);

        verifyNoMoreInteractions(institutosRepository);
    }





    @Test
    void findAll_porCiudadYNombre() {
        // Arrange
        String ciudad = "Madrid";
        String nombre = "Ramón";

        when(institutosRepository
                .findByCiudadContainingIgnoreCaseAndNombreContainingIgnoreCaseAndIsDeletedFalse(
                        ciudad, nombre))
                .thenReturn(List.of(instituto1));

        // Act
        List<InstitutoResponseDto> result =
                institutosService.findAll(ciudad, nombre);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre())
                .isEqualTo(instituto1.getNombre());

        // Verify
        verify(institutosRepository, times(1))
                .findByCiudadContainingIgnoreCaseAndNombreContainingIgnoreCaseAndIsDeletedFalse(
                        ciudad, nombre);

        verifyNoMoreInteractions(institutosRepository);
    }


    // ============================
    // FIND BY ID
    // ============================

    @Test
    void findById_ok() {
        when(institutosRepository.findById(1L))
                .thenReturn(Optional.of(instituto1));

        InstitutoResponseDto result =
                institutosService.findById(1L);

        assertThat(result).isEqualTo(institutoResponse1);
        verify(institutosRepository).findById(1L);
    }

    @Test
    void findById_notFound() {
        when(institutosRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> institutosService.findById(1L))
                .isInstanceOf(InstitutoNotFoundException.class);
    }

    // ============================
    // FIND BY UUID
    // ============================

    @Test
    void findByUuid_ok() {
        // Arrange
        UUID uuid = instituto1.getUuid();

        when(institutosRepository.findByUuidAndIsDeletedFalse(uuid))
                .thenReturn(Optional.of(instituto1));

        // Act
        InstitutoResponseDto result =
                institutosService.findByUuid(uuid.toString());

        // Assert
        assertThat(result).isEqualTo(institutoResponse1);

        // Verify
        verify(institutosRepository, times(1))
                .findByUuidAndIsDeletedFalse(uuid);

        verifyNoMoreInteractions(institutosRepository);
    }


    @Test
    void findByUuid_formatoInvalido() {
        // Act + Assert
        assertThatThrownBy(() -> institutosService.findByUuid("1234"))
                .isInstanceOf(InstitutoBadUuidException.class);

        // Verify: NO debe tocar el repositorio
        verify(institutosRepository, never())
                .findByUuidAndIsDeletedFalse(any());

        verifyNoMoreInteractions(institutosRepository);
    }


    // ============================
    // SAVE
    // ============================

    @Test
    void save_ok() {
        InstitutoCreateDto dto = InstitutoCreateDto.builder()
                .nombre("Las Meigas")
                .ciudad("Galicia")
                .direccion("Calle Barlovento")
                .tipo("publico")
                .anioFundacion(LocalDate.of(2025, 12, 31))
                .build();

        when(institutosRepository.save(any(Instituto.class)))
                .thenReturn(instituto1);

        InstitutoResponseDto result =
                institutosService.save(dto);

        assertThat(result).isNotNull();
        verify(institutosRepository)
                .save(institutoCaptor.capture());
    }

    // ============================
    // UPDATE
    // ============================

    @Test
    void update_ok() {
        when(institutosRepository.findById(1L))
                .thenReturn(Optional.of(instituto1));

        when(institutosRepository.save(any(Instituto.class)))
                .thenReturn(instituto1);

        InstitutoUpdateDto dto = InstitutoUpdateDto.builder()
                .numeroProfesores(999)
                .build();

        InstitutoResponseDto result =
                institutosService.update(1L, dto);

        assertThat(result).isNotNull();
        verify(institutosRepository).findById(1L);
        verify(institutosRepository).save(any());
    }

    // ============================
    // DELETE (SOFT DELETE)
    // ============================

    @Test
    void deleteById_ok() {
        when(institutosRepository.existsById(1L))
                .thenReturn(true);

        doNothing().when(institutosRepository)
                .updateIsDeletedToTrueById(1L);

        assertThatCode(() -> institutosService.deleteById(1L))
                .doesNotThrowAnyException();

        verify(institutosRepository).existsById(1L);
        verify(institutosRepository)
                .updateIsDeletedToTrueById(1L);
    }

    @Test
    void deleteById_notFound() {
        when(institutosRepository.existsById(1L))
                .thenReturn(false);

        assertThatThrownBy(() -> institutosService.deleteById(1L))
                .isInstanceOf(InstitutoNotFoundException.class);
    }
}
