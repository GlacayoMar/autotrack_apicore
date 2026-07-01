package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.AuthenticationResponse;
import ni.edu.autotrack_apicore.dto.LoginRequest;
import ni.edu.autotrack_apicore.dto.request.UsuarioRequestDTO;
import ni.edu.autotrack_apicore.dto.response.UsuarioResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.UsuarioSyncDTO;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.repositories.UsuarioRepository;
import ni.edu.autotrack_apicore.security.JwtService;
import ni.edu.autotrack_apicore.services.UsuarioService;
import org.springframework.http.HttpStatus;
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
    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> crear(@RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = convertToEntity(dto);
        Usuario nuevoUsuario = usuarioService.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(nuevoUsuario));
    }

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

    private Usuario convertToEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setNumeroTel(dto.getNumeroTel());
        usuario.setUsername(dto.getUsername());
        usuario.setPais(dto.getPais());
        usuario.setPassword(dto.getPassword());
        return usuario;
    }

    private UsuarioResponseDTO convertToDTO(Usuario entity) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setNombres(entity.getNombres());
        dto.setApellidos(entity.getApellidos());
        dto.setEmail(entity.getEmail());
        dto.setNumeroTel(entity.getNumeroTel());
        dto.setUsername(entity.getRealUsername());
        dto.setPais(entity.getPais());

        return dto;
    }
}