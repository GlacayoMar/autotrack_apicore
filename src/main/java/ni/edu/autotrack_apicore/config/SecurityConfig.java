package ni.edu.autotrack_apicore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desactivamos CSRF temporalmente porque estamos probando una API REST local
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // 1. PERMITIR ACCESO PÚBLICO A SWAGGER (Imprescindible para que te cargue en localhost)
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 2. Cualquier otra petición a tu API requerirá que estés autenticado
                        .anyRequest().authenticated()
                )

                // Habilita la autenticación básica (para mandar usuario/clave desde Postman o Swagger)
                .httpBasic(basic -> basic.init(http))

                .build();
    }
}
