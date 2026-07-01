[ Puerto 5432 (PostgreSQL) ]
└──> Motor de Base de Datos Local
├──> autotrack_db   (Base de datos principal / Persistente)
└──> autotrack_dev  (Base de datos de laboratorio / Pruebas)


### 📋 Perfiles Implementados

1. **`application.properties` (Global/Base):** Contiene la configuración compartida y apunta por defecto a la base de datos `autotrack_db` con la estrategia `spring.jpa.hibernate.ddl-auto=update` para proteger la integridad de los datos.
2. **`application-dev.properties` (Desarrollo):** Sobrescribe la conexión hacia `autotrack_dev`. Usa la estrategia `spring.jpa.hibernate.ddl-auto=create-drop`, lo que significa que **limpia y recrea la estructura de tablas en cada reinicio**, garantizando un laboratorio de pruebas limpio.

---

## 🎲 Data Seeding Operacional (`@Profile("dev")`)

La clase `DataSeeder` (ubicada en el paquete raíz de escaneo) implementa `CommandLineRunner`. Utiliza la librería **Datafaker** para inyectar información realista (en español) de manera automatizada inmediatamente después de que el contexto de Spring levanta con éxito.

## 🔒 Authentication Flow (JWT)

This API utilizes stateless **JSON Web Tokens (JWT)** for secure authentication, migrating away from standard Basic Authentication. This ensures that user sessions do not occupy memory on the server, making the system highly scalable.

### 🔄 Ciclo de Vida de la Autenticacion

1. **Inicio de sesión de usuario:** El cliente inicia una solicitud `POST` a `/api/auth/login` que incluye sus credenciales sin procesar (`email` y `password`).
2. **Verificación y hash:** El `AuthenticationManager` intercepta la solicitud, recupera el perfil del usuario desde PostgreSQL y verifica la contraseña frente al registro almacenado en la base de datos utilizando el algoritmo de hashing seguro **BCrypt**.
3. **Emisión de tokens y metadatos:** Tras una validación exitosa, el sistema genera un token JWT firmado digitalmente y lo devuelve junto con el `id` de la base de datos del usuario. Esto permite que las aplicaciones front-end almacenen el ID localmente para obtener perfiles específicos del usuario en solicitudes posteriores.
4. **Solicitudes API posteriores:** Para todas las solicitudes posteriores a endpoints protegidos (por ejemplo, gestionar vehículos, ver multas, consultar notificaciones), el cliente debe incluir este token en los encabezados HTTP:
   ```http
   Authorization: Bearer <TU_JWT_TOKEN>
   ```

### Restricciones de Unicidad Soportadas:
El script calcula y altera de forma dinámica campos únicos para evitar excepciones de PostgreSQL (`ConstraintViolationException`):
* **Usuarios:** `email` y `username` dinámicos por índice. Contraseña genérica temporal (`password_raw_provicional`) antes del proceso de *chunchado* (encriptación hash con BCrypt).
* **Vehículos:** Generación de placas alfanuméricas con formato local ("M XXXXXX") y números de VIN únicos.
* **Registros (Herencia):** Al utilizar la estrategia **`InheritanceType.JOINED`**, el seeder guarda polimórficamente instancias de `RegistroCombustible` y `RegistroProblema` a través del repositorio padre, y JPA distribuye los datos eficientemente en las tablas hijas de PostgreSQL.

---

## 📂 Estructura General del Proyecto

El backend está organizado bajo una arquitectura limpia en capas dentro del paquete raíz `ni.edu.autotrack_apicore`:

```text
ni.edu.autotrack_apicore/
│
├── config/
│   ├── ApplicationConfig.java
│   ├── SecurityConfig.java
│
├── controllers/          # Capa de Exposición: Endpoints REST (Controllers) que reciben las peticiones de Android.
│   ├── RegistroCombustibleController.java
│   ├── RegistroGeneralController.java
│   ├── RegistroProblemaController.java
│   ├── UsuarioController.java
│   ├── VehiculoController.java
│   └── ...
│
├── dto/                 # Capa de Transferencia de Datos:
│   ├── request/         # Cuerpo o estructura de los datos enviados por los clientes atraves de los endpoints.
│   │   ├── UsuarioRequestDTO
│   │   └──  ...
│   ├── response/        # Cuerpo o estructura de los datos devueltos por los endpoints.
│   │   ├── UsuarioResponseDTO
│   │   └──  ...
│   ├── sync/            # Cuerpo o estructura de los datos enviados de manera sincrona o asincrona por los endpoints.
│   │   ├── UsuarioSyncDTO
│   │   └──  ...
│   ├── AuthenticationResponse.java
│   └── LoginRequest.java
│
│
├── models/               # Capa de Dominio: Entidades JPA mapeadas directamente a tablas de PostgreSQL.
│   ├── base/             # Contiene la clase EntidadBase y Auditoria (IDs, fechas de creacion y actualizacion).
│   ├── enums/            # Enumeraciones lógicas globales (Estado, TipoProblema).
│   ├── Usuario.java      # Relación @OneToMany hacia Vehiculo.
│   ├── Vehiculo.java     # Relación @ManyToOne hacia Usuario y @OneToMany hacia Registro.
│   ├── Registro.java     # Clase abstracta con estrategia de herencia JOINED.
│   └──  ...
│
├── repositories/         # Capa de Acceso a Datos: Interfaces que extienden JpaRepository para queries SQL automáticas.
│   ├── UsuarioRepository.java
│   ├── VehiculoRepository.java
│   └── RegistroRepository.java
│
├── security/             # Capa de Seguridad: Esta se encarga de la autenticacion mediante JWT.
│   ├── JWTAuthenticationFilter.java
│   └── JWTService.java
│
├── seeder/               # Laboratorio de Pruebas: Lógica del poblamiento automático (Exclusivo perfil DEV).
│   └── DataSeeder.java
│
└── services/             # Capa de Negocio: Interfaces y lógica de implementaciones (impl/).
    ├── impl/             # Implementaciones concretas de la lógica de negocio.
    │   ├── UsuarioServiceImpl.java
    │   └──  ...
    ├── UsuarioService.java
    └──  ...
    
🚀 Automatización y Comandos en Ubuntu
El proyecto utiliza Gradle como gestor de dependencias. Para simplificar el arranque del entorno de pruebas, se utiliza el script bash automatizado run-dev.sh.

Levantar Entorno de Pruebas:
Bash
./run-dev.sh
Este comando limpia la compilación previa, levanta el backend en el puerto 8080 apuntando al perfil dev, purga autotrack_dev e inyecta la data simulada.

Apagar el Servidor de Forma Correcta:
Para liberar el puerto 8080, presione siempre Ctrl + C en la terminal activa.

⚠️ Auxilio de Emergencia (Proceso Suspendido con Ctrl + Z):
Si por error suspendió el proceso en segundo plano y el puerto quedó secuestrado, libérelo ejecutando:

Bash
fuser -k 8080/tcp
📱 Conexión con el Frontend Móvil (Android Studio + Kotlin)
En el navegador de Ubuntu: Puede verificar la salud del Tomcat REST accediendo a http://localhost:8080/.

En el Emulador de Android: Debido al aislamiento de red del emulador, localhost apunta al mismo dispositivo móvil. Para consumir los endpoints del backend local desde tu código en Kotlin (Retrofit/Volley), configure la URL base apuntando a la IP puente:

Plaintext
[http://10.0.2.2:8080/](http://10.0.2.2:8080/)
"""

with open("README_FEAT.md", "w", encoding="utf-8") as f:
f.write(content.strip())

print("README_FEAT.md creado exitosamente.")

Tu archivo de documentación rápida para este Feature está listo. He generado un archivo markdown (`README_FEAT.md`) estructurado de forma muy profesional que detalla todo lo que construimos, la explicación de tus carpetas y los comandos de Ubuntu.

Aquí tienes el archivo listo para tu repositorio:
[file-tag: code-generated-file-0-1781313943526450793]

***

### 📋 Vista previa de lo que incluye el archivo:

1. **Contexto de Arquitectura:** Explica cómo coexisten en tu PostgreSQL del puerto `5432` las dos bases de datos (`autotrack_db` para tus datos limpios y `autotrack_dev` para el laboratorio del seeder).
2. **Explicación del DataSeeder:** Describe las reglas de negocio de unicidad que le metimos al script para los emails, usernames, placas y la inyección polimórfica en cascada para la estrategia de herencia `JOINED`.
3. **Mapeo General del Proyecto:** Detalla qué hace cada una de las carpetas que tenías en tu árbol de IntelliJ (`controllers`, `models`, `repositories`, `seeder`, `services`), ideal para cuando otra persona (o vos mismo en el futuro) revise cómo escaló el código.
4. **Guía de comandos en Ubuntu:** Deja documentado el comando de Gradle, el uso del script `./run-dev.sh`, el puerto `8080` y el comando de emergencia por si se te vuelve a ir un `Ctrl + Z` (`fuser -k 8080/tcp`).
5. **Tip de Redes para Android:** Deja por escrito la regla de oro de usar `http://10.0.2.2:8080` en el código de Kotlin en Android Studio para no olvidarlo al conectar el frontend.

Podés meter este archivo en la raíz de tu proyecto como un documento técnico complementario. ¡Ya tenés un ambiente de desarrollo automatizado de nivel senior!