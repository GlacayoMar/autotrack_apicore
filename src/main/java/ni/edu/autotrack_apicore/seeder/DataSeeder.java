package ni.edu.autotrack_apicore.seeder;

import net.datafaker.Faker;
import ni.edu.autotrack_apicore.models.*;
import ni.edu.autotrack_apicore.models.enums.*;
import ni.edu.autotrack_apicore.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Profile({"dev", "staging"})
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final RegistroRepository registroRepository;
    private final LicenciaRepository licenciaRepository;
    private final DocumentoVehiculoRepository documentoVehiculoRepository;
    private final MultaRepository multaRepository;
    private final NotificacionRepository notificacionRepository;
    private final ServicioMantenimientoRepository servicioMantenimientoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Faker faker;


    public DataSeeder(UsuarioRepository usuarioRepository,
                      VehiculoRepository vehiculoRepository,
                      RegistroRepository registroRepository,
                      LicenciaRepository licenciaRepository,
                      DocumentoVehiculoRepository documentoVehiculoRepository,
                      MultaRepository multaRepository,
                      NotificacionRepository notificacionRepository,
                      ServicioMantenimientoRepository servicioMantenimientoRepository,
                      BCryptPasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.registroRepository = registroRepository;
        this.licenciaRepository = licenciaRepository;
        this.documentoVehiculoRepository = documentoVehiculoRepository;
        this.multaRepository = multaRepository;
        this.notificacionRepository = notificacionRepository;
        this.servicioMantenimientoRepository = servicioMantenimientoRepository;
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

        // ==========================================
        // 1. POBLAR USUARIOS
        // ==========================================
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Usuario u = new Usuario();

            // 1. Generar y asignar nombre y apellido
            String primerNombre = faker.name().firstName();
            String apellido = faker.name().lastName();

            u.setNombres(primerNombre);
            u.setApellidos(apellido);

            // 2. Limpiar los strings para usarlos en email y username (minúsculas y sin espacios)
            String nombreLimpio = primerNombre.toLowerCase().replaceAll("\\s+", "");
            String apellidoLimpio = apellido.toLowerCase().replaceAll("\\s+", "");

            // 3. Generar Email (ej: juanperez@gmail.com)
            // Agregamos el índice "i" al inicio o final solo por si Faker repite combinaciones de nombres
            String email = nombreLimpio + apellidoLimpio + i + "@gmail.com";
            u.setEmail(email);

            // 4. Generar Username (ej: jperez)
            String primerLetra = nombreLimpio.isEmpty() ? "" : nombreLimpio.substring(0, 1);
            String username = primerLetra + apellidoLimpio + "_" + i;
            u.setUsername(username);

            // El resto de los datos se mantiene igual
            u.setNumeroTel(faker.phoneNumber().cellPhone());
            u.setPassword(passwordGenericaEncriptada); //contra encriptada
            u.setPais("Nicaragua");

            usuarios.add(u);
        }
        usuarioRepository.saveAll(usuarios);

        // ==========================================
        // 2. POBLAR VEHÍCULOS
        // ==========================================
        List<Vehiculo> vehiculos = new ArrayList<>();
        String[] marcas = {"Toyota", "Hyundai", "Kia", "Suzuki, Honda"};
        //long contador = 300000;  *dejalo por si acaso te da problemas el faker al correr el ambiente de prueba*

        Estado[] estadoVehiculo = Estado.values();

        for (Usuario usuario : usuarios) {
            int randomCars = faker.number().numberBetween(1, 4);
            for (int j = 0; j < randomCars; j++) {
                Vehiculo v = new Vehiculo();
                v.setMarca(marcas[faker.number().numberBetween(0, marcas.length)]);
                v.setModelo(faker.vehicle().model());
                v.setAnio(faker.number().numberBetween(2015, Year.now().getValue()));
                v.setPlaca("M " + faker.number().digits(6)); //si te genera muchos problemas solo pones ("M " + contador)
                // Y aqui abajo contador++;
                v.setVin(faker.vehicle().vin()); // este no me ha dado problemas pero si lo hace pues aplicas lo mismo del contador

                if (estadoVehiculo.length > 0) {
                    v.setEstado(estadoVehiculo[faker.number().numberBetween(0, estadoVehiculo.length)]);
                }

                v.setUsuario(usuario);
                vehiculos.add(v);
            }
        }
        vehiculoRepository.saveAll(vehiculos);

        // ==========================================
        // 3. POBLAR REGISTROS (HERENCIA JOINED)
        // ==========================================
        List<Registro> todosLosRegistros = new ArrayList<>();

        // Obtenemos los Enums de TipoProblema que creaste
        TipoProblema[] tiposDeProblema = TipoProblema.values();

        for (Vehiculo vehiculo : vehiculos) {
            int randomRegistrosCombustibles = faker.number().numberBetween(8, 16);

            // 1. Generar una lista de fechas ordenadas cronológicamente para este vehículo
            List<LocalDate> fechasOrdenadas = new ArrayList<>();
            for (int i = 0; i < randomRegistrosCombustibles; i++) {
                fechasOrdenadas.add(faker.date().past(90, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            Collections.sort(fechasOrdenadas);

            // 2. Definir un odómetro inicial aleatorio para el carro y el precio por litro en Nic (C$ 47.81 aprox)
            long odometroActual = (long) faker.number().numberBetween(10000, 100000);
            double precioPorUnidad = 47.81; // Basado en el precio congelado en Nicaragua por litro (Regular)

            // Generar un registro de combustible por cada vehículo usando las fechas ordenadas
            for (int l = 0; l < randomRegistrosCombustibles; l++) {
                RegistroCombustible rc = new RegistroCombustible();

                // Asignar fecha en orden ascendente
                rc.setFechaRegistro(fechasOrdenadas.get(l));
                rc.setNota("Combustible semanal - Gasolinera Puma");
                rc.setVehiculo(vehiculo);

                // Datos del detalle de combustible
                double litrosOgalones = faker.number().randomDouble(2, 20, 50); // Ajustado a un tanque normal (20-50 Litros)
                rc.setCantidadCombustible(BigDecimal.valueOf(litrosOgalones));

                // Calcular pago real basado en los litros y el precio de Nicaragua
                double totalPagado = litrosOgalones * precioPorUnidad;
                rc.setCantidadPagado(BigDecimal.valueOf(totalPagado));

                // El odómetro crece de forma realista entre 300 y 700 km/millas por cada tanqueada
                odometroActual += faker.number().numberBetween(300, 700);
                rc.setOdometro(odometroActual);

                todosLosRegistros.add(rc);
            }

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

        // ==========================================
        // 4. POBLAR LICENCIAS (OneToOne con Usuario)
        // ==========================================
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

        // =============================================================
        // 5. POBLAR DOCUMENTOS DE VEHÍCULOS (ManyToOne con Vehículo)
        // =============================================================
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

        // ==========================================
        // 6. POBLAR MULTAS (ManyToOne con Usuario)
        // ==========================================
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

        // ================================================================
        // 7. POBLAR NOTIFICACIONES (Relacionadas a Documentos y Usuarios)
        // ================================================================
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

        // ==========================================
        // 8. POBLAR SERVICIOS DE MANTENIMIENTO
        // ==========================================
        List<ServicioMantenimiento> mantenimientos = new ArrayList<>();
        TipoMantenimiento[] tiposMantenimiento = TipoMantenimiento.values();

        for (Vehiculo vehiculo : vehiculos) {
            // Generamos entre 1 y 3 servicios de mantenimiento aleatorios para cada carro en la base de datos
            int cantidadMantenimientos = faker.number().numberBetween(1, 4);

            for (int k = 0; k < cantidadMantenimientos; k++) {
                ServicioMantenimiento sm = new ServicioMantenimiento();

                // Seleccionamos un tipo de mantenimiento aleatorio de tu Enum
                TipoMantenimiento tipoAleatorio = tiposMantenimiento[faker.number().numberBetween(0, tiposMantenimiento.length)];
                sm.setTipo(tipoAleatorio);

                // Generamos títulos y descripciones coherentes usando Faker
                sm.setTitulo(faker.options().option("Mantenimiento de " + tipoAleatorio.name().toLowerCase().replace("_", " "), "Revisión técnica de " + vehiculo.getMarca()));
                sm.setDescripcion(faker.lorem().sentence(12));
                sm.setObservaciones(faker.lorem().sentence(6));

                // Lógica de negocio e indicadores de control vehicular
                sm.setAfectaVehiculo(faker.bool().bool());
                sm.setCompletado(faker.bool().bool()); // Algunos nacerán hechos y otros pendientes
                sm.setDistanciaAgendada(faker.number().numberBetween(5000, 100000));

                // Lógica de Fechas: Generamos fechas futuras coherentes para evitar la restricción de tu capa de servicio
                // (Entre hoy y los próximos 6 meses)
                int diasEnElFuturo = faker.number().numberBetween(1, 180);
                sm.setFechaAgendada(LocalDateTime.now().plusDays(diasEnElFuturo).withNano(0));

                // Campos de auditoría heredados de EntidadBase
                sm.setFechaCreacion(LocalDateTime.now());
                sm.setFechaActualizacion(LocalDateTime.now());
                sm.setActivo(true);

                // Amarramos el mantenimiento a su respectivo vehículo
                sm.setVehiculo(vehiculo);
                mantenimientos.add(sm);
            }
        }

        servicioMantenimientoRepository.saveAll(mantenimientos);

        System.out.println("──> [PostgreSQL] ¡Seeder finalizado con éxito!");
        System.out.println("    Usuarios creados: " + usuarios.size());
        System.out.println("    Vehículos creados: " + vehiculos.size());
        System.out.println("    Servicios de mantenimiento creados: " + mantenimientos.size());
        System.out.println("    Licencias creadas: " + licencias.size());
        System.out.println("    Documentos de vehículos creados: " + documentosVehiculos.size());
        System.out.println("    Multas de tránsito creadas: " + multas.size());
        System.out.println("    Notificaciones generadas: " + notificaciones.size());
        System.out.println("    Registros totales insertados: " + todosLosRegistros.size());
    }
}
