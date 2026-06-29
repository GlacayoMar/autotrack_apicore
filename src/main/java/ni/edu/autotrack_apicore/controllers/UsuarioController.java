package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.UsuarioRequestDTO;
import ni.edu.autotrack_apicore.dto.response.UsuarioResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.UsuarioSyncDTO;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.services.UsuarioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin
@RequiredArgsConstructor
public class UsuarioController{

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = convertToEntity(dto);
        Usuario nuevoUsuario = usuarioService.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(nuevoUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDTO(usuarioService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<UsuarioResponseDTO> dtos = usuarioService.listar().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<UsuarioSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        List<UsuarioSyncDTO> dtos = usuarioService.listarActualizadosDespuseDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO) // <-- Usamos el mapeador especializado
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // MEJORA: Endpoint para actualizar datos del usuario
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioRequestDTO dto) {
        Usuario usuarioDetalles = convertToEntity(dto);
        Usuario usuarioActualizado = usuarioService.actualizar(id, usuarioDetalles);
        return ResponseEntity.ok(convertToDTO(usuarioActualizado));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    private UsuarioSyncDTO convertToSincronizacionDTO(Usuario entity) {
        UsuarioResponseDTO baseDto = convertToDTO(entity);

        UsuarioSyncDTO sincroDto = new UsuarioSyncDTO();

        sincroDto.setId(baseDto.getId());
        sincroDto.setFechaCreacion(baseDto.getFechaCreacion());
        sincroDto.setFechaActualizacion(baseDto.getFechaActualizacion());
        sincroDto.setNombres(baseDto.getNombres());
        sincroDto.setApellidos(baseDto.getApellidos());
        sincroDto.setEmail(baseDto.getEmail());
        sincroDto.setNumeroTel(baseDto.getNumeroTel());
        sincroDto.setUsername(baseDto.getUsername());
        sincroDto.setPais(baseDto.getPais());

        sincroDto.setPassword(entity.getPassword());

        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
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
        dto.setUsername(entity.getUsername());
        dto.setPais(entity.getPais());

        return dto;

    }
}
