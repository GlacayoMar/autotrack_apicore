package ni.edu.autotrack_apicore.seeder;

import net.datafaker.Faker;
import ni.edu.autotrack_apicore.models.*;
import ni.edu.autotrack_apicore.models.enums.Estado;
import ni.edu.autotrack_apicore.models.enums.TipoProblema;
import ni.edu.autotrack_apicore.repositories.RegistroRepository;
import ni.edu.autotrack_apicore.repositories.UsuarioRepository;
import ni.edu.autotrack_apicore.repositories.VehiculoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final RegistroRepository registroRepository; // Inyecta el repo padre para salvar los registros

    private final Faker faker;

    public DataSeeder(UsuarioRepository usuarioRepository,
                      VehiculoRepository vehiculoRepository,
                      RegistroRepository registroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.registroRepository = registroRepository;
        this.faker = new Faker(new Locale("es"));
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() > 0) {
            System.out.println("──> [PostgreSQL] BD ya cuenta con datos. Saltando Seeder.");
            return;
        }

        System.out.println("──> [PostgreSQL] Generando ambiente de prueba profesional para Autotrack...");

        // 1. POBLAR USUARIOS
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Usuario u = new Usuario();
            u.setNombres(faker.name().firstName());
            u.setApellidos(faker.name().lastName());
            u.setEmail(i + "_" + faker.internet().emailAddress());
            u.setUsername(faker.name().username() + "_" + i);
            u.setNumeroTel(faker.phoneNumber().cellPhone());
            u.setPassword("password_raw_provicional");
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
            rc.setFechaRegistro(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
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

        System.out.println("──> [PostgreSQL] ¡Seeder finalizado con éxito!");
        System.out.println("    Usuarios creados: " + usuarios.size());
        System.out.println("    Vehículos creados: " + vehiculos.size());
        System.out.println("    Registros totales insertados: " + todosLosRegistros.size());
    }
}
