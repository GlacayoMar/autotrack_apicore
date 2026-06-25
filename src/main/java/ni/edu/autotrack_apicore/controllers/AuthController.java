package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.AuthenticationResponse;
import ni.edu.autotrack_apicore.dto.LoginRequest;
import ni.edu.autotrack_apicore.repositories.UsuarioRepository;
import ni.edu.autotrack_apicore.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository; // 1. Inject your repository here

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        // Se autentica al usuario o se eleva una excepción
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Buscamos el usuario completo en la base de datos usando el email
        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + request.getEmail()));

        // Generamos el token pasando la entidad usuario (que ya implementa UserDetails)
        final String jwtToken = jwtService.generateToken(usuario);

        // Retornamos tanto el token como el ID del usuario
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken, usuario.getId()));
    }
}