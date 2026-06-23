package ni.edu.autotrack_apicore.services;

import jakarta.persistence.EntityNotFoundException;
import ni.edu.autotrack_apicore.models.*;
import ni.edu.autotrack_apicore.models.enums.CategoriaLicencia;
import ni.edu.autotrack_apicore.models.enums.TipoNotificacion;
import ni.edu.autotrack_apicore.repositories.*;
import ni.edu.autotrack_apicore.services.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicesUnitTests {

    @Mock
    private LicenciaRepository licenciaRepository;

    @Mock
    private DocumentoVehiculoRepository documentoVehiculoRepository;

    @Mock
    private MultaRepository multaRepository;

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private DocumentoRepository documentoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private VehiculoService vehiculoService;

    @Mock
    private DocumentoService documentoService;

    @InjectMocks
    private LicenciaServiceImpl licenciaService;

    @InjectMocks
    private DocumentoVehiculoServiceImpl documentoVehiculoService;

    @InjectMocks
    private MultaServiceImpl multaService;

    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    private Usuario usuario;
    private Vehiculo vehiculo;
    private Documento documento;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");

        vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setPlaca("M123456");

        documento = new Documento();
        documento.setId(10L);
        documento.setFechaVencimiento(LocalDate.now().plusYears(1));
    }

    @Test
    void testCrearLicencia_Exito() {
        Licencia licencia = new Licencia();
        licencia.setFechaVencimiento(LocalDate.now().plusYears(2));

        when(licenciaRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);
        when(licenciaRepository.save(any(Licencia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Licencia creada = licenciaService.crear(1L, licencia);

        assertNotNull(creada);
        assertEquals(usuario, creada.getUsuario());
        verify(licenciaRepository, times(1)).save(licencia);
    }

    @Test
    void testCrearLicencia_UsuarioYaTieneLicencia_LanzaExcepcion() {
        Licencia licencia = new Licencia();

        when(licenciaRepository.existsByUsuarioId(1L)).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            licenciaService.crear(1L, licencia);
        });

        assertEquals("El usuario ya cuenta con una licencia registrada", exception.getMessage());
        verify(licenciaRepository, never()).save(any());
    }

    @Test
    void testCrearDocumentoVehiculo_Exito() {
        DocumentoVehiculo doc = new DocumentoVehiculo();
        doc.setNombre("Matrícula");

        when(vehiculoService.obtenerPorId(1L)).thenReturn(vehiculo);
        when(documentoVehiculoRepository.save(any(DocumentoVehiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DocumentoVehiculo creado = documentoVehiculoService.crear(1L, doc);

        assertNotNull(creado);
        assertEquals(vehiculo, creado.getVehiculo());
        verify(documentoVehiculoRepository, times(1)).save(doc);
    }

    @Test
    void testCrearMulta_Exito() {
        Multa multa = new Multa();
        multa.setDescripcion("Exceso de velocidad");
        multa.setMonto(BigDecimal.valueOf(500));

        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);
        when(multaRepository.save(any(Multa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Multa creada = multaService.crear(1L, multa);

        assertNotNull(creada);
        assertFalse(creada.getPagada());
        assertEquals(usuario, creada.getUsuario());
        verify(multaRepository, times(1)).save(multa);
    }

    @Test
    void testPagarMulta_Exito() {
        Multa multa = new Multa();
        multa.setId(5L);
        multa.setPagada(false);

        when(multaRepository.findById(5L)).thenReturn(Optional.of(multa));
        when(multaRepository.save(any(Multa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Multa pagada = multaService.pagarMulta(5L);

        assertNotNull(pagada);
        assertTrue(pagada.getPagada());
        verify(multaRepository, times(1)).save(multa);
    }

    @Test
    void testCrearNotificacion_Exito() {
        Notificacion notif = new Notificacion();
        notif.setMensaje("Tu licencia está por vencer");
        notif.setTipo(TipoNotificacion.VENCIMIENTO_DOCUMENTO);

        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);
        when(documentoService.obtenerPorId(10L)).thenReturn(documento);
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notificacion creada = notificacionService.crear(1L, 10L, notif);

        assertNotNull(creada);
        assertFalse(creada.getEnviada());
        assertFalse(creada.getIgnorar());
        assertEquals(usuario, creada.getUsuario());
        assertEquals(documento, creada.getDocumento());
        verify(notificacionRepository, times(1)).save(notif);
    }
}
