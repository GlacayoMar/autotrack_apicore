package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.LicenciaRequestDTO;
import ni.edu.autotrack_apicore.dto.response.LicenciaResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.LicenciaSyncDTO;
import ni.edu.autotrack_apicore.models.Licencia;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.services.LicenciaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/licencias")
@CrossOrigin
@RequiredArgsConstructor
public class LicenciaController {

    private final LicenciaService licenciaService;

    @PostMapping
    public ResponseEntity<LicenciaResponseDTO> crear(
            @RequestBody LicenciaRequestDTO dto) {
        Licencia licencia = convertToEntity(dto);
        Licencia nuevoLicencia = licenciaService.crear(licencia);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertToDTO(nuevoLicencia));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LicenciaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDTO(licenciaService.obtenerPorId(id)));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<LicenciaResponseDTO> obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(convertToDTO(licenciaService.obtenerPorUsuarioId(usuarioId)));
    }

    @GetMapping
    public ResponseEntity<List<LicenciaResponseDTO>> listar() {
        List<LicenciaResponseDTO> dtos = licenciaService.listar().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<LicenciaSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        List<LicenciaSyncDTO> dtos = licenciaService.listarActualizadosDespuesDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LicenciaResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody LicenciaRequestDTO dto) {
        Licencia licencia = convertToEntity(dto);
        Licencia actualizado = licenciaService.actualizar(id, licencia);
        return ResponseEntity.ok(convertToDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        licenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private LicenciaSyncDTO convertToSincronizacionDTO(Licencia entity) {
        LicenciaResponseDTO baseDto = convertToDTO(entity);

        LicenciaSyncDTO sincroDto = new LicenciaSyncDTO();

        sincroDto.setId(baseDto.getId());
        sincroDto.setFechaCreacion(baseDto.getFechaCreacion());
        sincroDto.setFechaActualizacion(baseDto.getFechaActualizacion());
        sincroDto.setFechaEmitida(baseDto.getFechaEmitida());
        sincroDto.setFechaVencimiento(baseDto.getFechaVencimiento());
        sincroDto.setImagen(baseDto.getImagen());
        sincroDto.setCategorias(baseDto.getCategorias());
        sincroDto.setUsuarioId(entity.getUsuario().getId());

        // 4. Inyectamos la propiedad exclusiva que los demás endpoints no verán
        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private Licencia convertToEntity(LicenciaRequestDTO dto) {
        Licencia lic = new Licencia();

        lic.setFechaEmitida(dto.getFechaEmitida());
        lic.setFechaVencimiento(dto.getFechaVencimiento());
        lic.setImagen(dto.getImagen());
        lic.setCategorias(dto.getCategorias());


        if (dto.getUsuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioId());
            lic.setUsuario(usuario);
        }

        return lic;
    }

    private LicenciaResponseDTO convertToDTO(Licencia entity) {
        LicenciaResponseDTO dto = new LicenciaResponseDTO();

        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setFechaEmitida(entity.getFechaEmitida());
        dto.setFechaVencimiento(entity.getFechaVencimiento());
        dto.setImagen(entity.getImagen());
        dto.setCategorias(entity.getCategorias());

        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getId());
        }

        return dto;
    }
}
