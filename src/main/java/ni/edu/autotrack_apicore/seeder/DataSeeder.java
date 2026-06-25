package ni.edu.autotrack_apicore.seeder;

import net.datafaker.Faker;
import ni.edu.autotrack_apicore.models.*;
import ni.edu.autotrack_apicore.models.enums.CategoriaLicencia;
import ni.edu.autotrack_apicore.models.enums.Estado;
import ni.edu.autotrack_apicore.models.enums.TipoNotificacion;
import ni.edu.autotrack_apicore.models.enums.TipoProblema;
import ni.edu.autotrack_apicore.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final RegistroRepository registroRepository;
    private final LicenciaRepository licenciaRepository;
    private final DocumentoVehiculoRepository documentoVehiculoRepository;
    private final MultaRepository multaRepository;
    private final NotificacionRepository notificacionRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Faker faker;


    public DataSeeder(UsuarioRepository usuarioRepository,
                      VehiculoRepository vehiculoRepository,
                      RegistroRepository registroRepository,
                      LicenciaRepository licenciaRepository,
                      DocumentoVehiculoRepository documentoVehiculoRepository,
                      MultaRepository multaRepository,
                      NotificacionRepository notificacionRepository,
                      BCryptPasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.registroRepository = registroRepository;
        this.licenciaRepository = licenciaRepository;
        this.documentoVehiculoRepository = documentoVehiculoRepository;
        this.multaRepository = multaRepository;
        this.notificacionRepository = notificacionRepository;
        this.passwordEncoder = passwordEncoder;
        this.faker = new Faker(new Locale("es"));
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() > 0) {
            System.out.println("──> [PostgreSQL] BD ya cuenta con datos. Saltando Seeder.");
            return;
        }

        System.out.println("──> [PostgreSQL] Generando ambiente de prueba profesional para Autotrack...");

        String passwordGenericaEncriptada = passwordEncoder.encode("password-123");

        // 1. POBLAR USUARIOS
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            Usuario u = new Usuario();
            u.setNombres(faker.name().firstName());
            u.setApellidos(faker.name().lastName());
            u.setEmail(i + "_" + faker.internet().emailAddress());
            u.setUsername(faker.name().username() + "_" + i);
            u.setNumeroTel(faker.phoneNumber().cellPhone());
            u.setPassword(passwordGenericaEncriptada); //contra encriptada
            u.setPais("Nicaragua");
            usuarios.add(u);
        }
        usuarioRepository.saveAll(usuarios);

        // 2. POBLAR VEHÍCULOS
        List<Vehiculo> vehiculos = new ArrayList<>();
        String[] marcas = {"Toyota", "Hyundai", "Kia", "Suzuki"};
        //long contador = 300000;  *dejalo por si acaso te da problemas el faker al correr el ambiente de prueba*

        for (Usuario usuario : usuarios) {
            int randomCars = faker.number().numberBetween(1, 3);
            for (int j = 0; j < randomCars; j++) {
                Vehiculo v = new Vehiculo();
                v.setMarca(marcas[faker.number().numberBetween(0, marcas.length)]);
                v.setModelo(faker.vehicle().model());
                v.setAnio(faker.number().numberBetween(2015, Year.now().getValue()));
                v.setPlaca("M " + faker.number().digits(6)); //si te genera muchos problemas solo pones ("M " + contador)
                // Y aqui abajo contador++;
                v.setVin(faker.vehicle().vin()); // este no me ha dado problemas pero si lo hace pues aplicas lo mismo del contador

                v.setEstado(Estado.CHUQUITI);

                v.setUsuario(usuario);
                vehiculos.add(v);
            }
        }
        vehiculoRepository.saveAll(vehiculos);

        // 3. POBLAR REGISTROS (HERENCIA JOINED)
        List<Registro> todosLosRegistros = new ArrayList<>();

        // Obtenemos los Enums de TipoProblema que creaste
        TipoProblema[] tiposDeProblema = TipoProblema.values();

        for (Vehiculo vehiculo : vehiculos) {
            // Generar un registro de combustible por cada vehículo
            RegistroCombustible rc = new RegistroCombustible();
            // Genera una fecha aleatoria de los últimos 30 días
            rc.setFechaRegistro(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            rc.setNota("Combustible semanal - Gasolinera Puma");
            rc.setVehiculo(vehiculo);

            // Datos del detalle de combustible
            rc.setCantidadCombustible(BigDecimal.valueOf(faker.number().randomDouble(2, 5, 15))); // Galones/Litros
            rc.setCantidadPagado(BigDecimal.valueOf(faker.number().randomDouble(2, 500, 2000))); // Córdobas/Dólares
            rc.setOdometro((long) faker.number().numberBetween(10000, 150000));

            todosLosRegistros.add(rc);

            // Generar un registro de problema de forma aleatoria (50% de probabilidad por vehículo)
            if (faker.bool().bool()) {
                RegistroProblema rp = new RegistroProblema();
                rp.setFechaRegistro(LocalDate.now());
                // Para crear notas de prueba
                String notaFallo = ("Revision de equipamiento: " + faker.vehicle().standardSpecs());
                if (notaFallo.length() > 500) {
                    notaFallo = notaFallo.substring(0, 490) + "...";
                }
                rp.setNota(notaFallo); //fue un dolor de manguaco hayarle solucion
                rp.setVehiculo(vehiculo);

                // Datos del detalle del problema
                rp.setActivo(true);
                rp.setAfectaVehiculo(faker.bool().bool());

                // Asignar un TipoProblema aleatorio de tu enum si contiene valores
                if (tiposDeProblema.length > 0) {
                    rp.setTipoProblema(tiposDeProblema[faker.number().numberBetween(0, tiposDeProblema.length)]);
                }

                todosLosRegistros.add(rp);
            }
        }

        // Al salvar desde el repositorio padre, JPA se encarga de repartir
        // los datos a las tablas hijas correspondientes en Postgres gracias a @Inheritance
        registroRepository.saveAll(todosLosRegistros);

        // 4. POBLAR LICENCIAS (OneToOne con Usuario)
        List<Licencia> licencias = new ArrayList<>();
        CategoriaLicencia[] categorias = CategoriaLicencia.values();
        for (Usuario usuario : usuarios) {
            Licencia l = new Licencia();
            // Fecha emitida en los últimos 3 años
            LocalDate fechaEmitida = faker.date().past(3 * 365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            l.setFechaEmitida(fechaEmitida);
            // Vence en 5 años
            l.setFechaVencimiento(fechaEmitida.plusYears(5));
            l.setImagen("https://picsum.photos/seed/licencia_" + usuario.getId() + "/400/250");

            // Asignar entre 1 y 2 categorías aleatorias
            Set<CategoriaLicencia> catSet = new HashSet<>();
            int numCats = faker.number().numberBetween(1, 3);
            for (int k = 0; k < numCats; k++) {
                catSet.add(categorias[faker.number().numberBetween(0, categorias.length)]);
            }
            l.setCategorias(catSet);
            l.setUsuario(usuario);
            licencias.add(l);
        }
        licenciaRepository.saveAll(licencias);

        // 5. POBLAR DOCUMENTOS DE VEHÍCULOS (ManyToOne con Vehículo)
        List<DocumentoVehiculo> documentosVehiculos = new ArrayList<>();
        String[] nombresDocs = {"Seguro Obligatorio", "Matrícula de Circulación", "Inspección de Gases", "Inspección Mecánica"};
        for (Vehiculo vehiculo : vehiculos) {
            // Generar entre 1 y 3 documentos para cada vehículo
            int randomDocs = faker.number().numberBetween(1, 4);
            for (int k = 0; k < randomDocs; k++) {
                DocumentoVehiculo dv = new DocumentoVehiculo();
                dv.setNombre(nombresDocs[k % nombresDocs.length]);
                LocalDate fechaEmitida = faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dv.setFechaEmitida(fechaEmitida);
                dv.setFechaVencimiento(fechaEmitida.plusYears(1)); // Vence en un año
                dv.setImagen("https://picsum.photos/seed/docvehiculo_" + vehiculo.getId() + "_" + k + "/400/300");
                dv.setVehiculo(vehiculo);
                documentosVehiculos.add(dv);
            }
        }
        documentoVehiculoRepository.saveAll(documentosVehiculos);

        // 6. POBLAR MULTAS (ManyToOne con Usuario)
        List<Multa> multas = new ArrayList<>();
        String[] infracciones = {
            "Girar en U en zona prohibida",
            "Exceso de velocidad en zona urbana",
            "Estacionarse en zona amarilla",
            "No portar el cinturón de seguridad",
            "Hablar por teléfono celular mientras conduce",
            "Irrespeto a la luz roja del semáforo"
        };
        BigDecimal[] montos = {
            BigDecimal.valueOf(160),
            BigDecimal.valueOf(320),
            BigDecimal.valueOf(500),
            BigDecimal.valueOf(1000),
            BigDecimal.valueOf(2500)
        };
        for (Usuario usuario : usuarios) {
            // Solo el 30% de los usuarios tiene multas de tránsito
            if (faker.number().numberBetween(1, 11) <= 3) {
                int randomMultas = faker.number().numberBetween(1, 3);
                for (int k = 0; k < randomMultas; k++) {
                    Multa m = new Multa();
                    m.setDescripcion(infracciones[faker.number().numberBetween(0, infracciones.length)]);
                    m.setMonto(montos[faker.number().numberBetween(0, montos.length)]);

                    LocalDate fechaMulta = faker.date().past(180, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    m.setFechaMulta(fechaMulta);
                    m.setFechaLimite(fechaMulta.plusDays(30));
                    m.setFechaEmitida(fechaMulta);
                    m.setFechaVencimiento(fechaMulta.plusDays(30));
                    m.setPagada(faker.bool().bool());
                    m.setImagen("https://picsum.photos/seed/multa_" + usuario.getId() + "_" + k + "/400/300");
                    m.setUsuario(usuario);
                    multas.add(m);
                }
            }
        }
        multaRepository.saveAll(multas);

        // 7. POBLAR NOTIFICACIONES (Relacionadas a Documentos y Usuarios)
        List<Notificacion> notificaciones = new ArrayList<>();
        List<Documento> todosLosDocumentos = new ArrayList<>();

        // Agrupar licencias, documentos de vehículos y multas como documentos base
        todosLosDocumentos.addAll(licencias);
        todosLosDocumentos.addAll(documentosVehiculos);
        todosLosDocumentos.addAll(multas);

        for (Documento doc : todosLosDocumentos) {
            // 50% de probabilidad de generar una notificación para cada documento registrado
            if (faker.bool().bool()) {
                Notificacion n = new Notificacion();
                n.setFechaInicio(doc.getFechaEmitida());
                n.setFechaFinal(doc.getFechaVencimiento());
                n.setFrecuencia(faker.number().numberBetween(0, 2) == 0 ? "Semanal" : "Mensual");
                n.setIgnorar(faker.bool().bool());
                n.setEnviada(faker.bool().bool());

                Usuario usuarioAsociado = null;
                String mensaje = "";
                TipoNotificacion tipo = TipoNotificacion.VENCIMIENTO_DOCUMENTO;

                if (doc instanceof Licencia) {
                    usuarioAsociado = ((Licencia) doc).getUsuario();
                    mensaje = "Aviso: Su Licencia de Conducir vence pronto el " + doc.getFechaVencimiento();
                } else if (doc instanceof DocumentoVehiculo) {
                    DocumentoVehiculo dv = (DocumentoVehiculo) doc;
                    usuarioAsociado = dv.getVehiculo().getUsuario();
                    mensaje = "Aviso: El documento '" + dv.getNombre() + "' del vehículo con placa " + dv.getVehiculo().getPlaca() + " vencerá el " + doc.getFechaVencimiento();
                } else if (doc instanceof Multa) {
                    Multa m = (Multa) doc;
                    usuarioAsociado = m.getUsuario();
                    tipo = TipoNotificacion.MULTA_REGISTRADA;
                    mensaje = "Alerta: Tiene una multa registrada pendiente por '" + m.getDescripcion() + "' de monto Cordobas: " + m.getMonto();
                    if (m.getPagada()) {
                        mensaje = "Confirmación: Pago de multa por '" + m.getDescripcion() + "' procesado con éxito.";
                    }
                }

                if (usuarioAsociado != null) {
                    n.setMensaje(mensaje);
                    n.setTipo(tipo);
                    n.setDocumento(doc);
                    n.setUsuario(usuarioAsociado);
                    notificaciones.add(n);
                }
            }
        }
        notificacionRepository.saveAll(notificaciones);

        System.out.println("──> [PostgreSQL] ¡Seeder finalizado con éxito!");
        System.out.println("    Usuarios creados: " + usuarios.size());
        System.out.println("    Vehículos creados: " + vehiculos.size());
        System.out.println("    Licencias creadas: " + licencias.size());
        System.out.println("    Documentos de vehículos creados: " + documentosVehiculos.size());
        System.out.println("    Multas de tránsito creadas: " + multas.size());
        System.out.println("    Notificaciones generadas: " + notificaciones.size());
        System.out.println("    Registros totales insertados: " + todosLosRegistros.size());
    }
}
