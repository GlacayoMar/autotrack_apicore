package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.MultaRequestDTO;
import ni.edu.autotrack_apicore.dto.response.MultaResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.MultaSyncDTO;
import ni.edu.autotrack_apicore.models.Multa;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.services.MultaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/multas")
@CrossOrigin
@RequiredArgsConstructor
public class MultaController {

    private final MultaService multaService;

    @PostMapping
    public ResponseEntity<MultaResponseDTO> crear(
            @RequestBody MultaRequestDTO dto) {
        Multa multa = convertToEntity(dto);
        Multa nuevoMulta = multaService.crear(multa);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertToDTO(nuevoMulta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDTO(multaService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<MultaResponseDTO>> listar() {
        List<MultaResponseDTO> dtos = multaService.listar().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MultaResponseDTO>> listarPorUsuarioId(@PathVariable Long usuarioId) {
        List<MultaResponseDTO> dtos = multaService.listarPorUsuarioId(usuarioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{usuarioId}/estado")
    public ResponseEntity<List<MultaResponseDTO>> listarPorUsuarioIdYEstadoPago(
            @PathVariable Long usuarioId,
            @RequestParam Boolean pagada) {
        List<MultaResponseDTO> dtos = multaService.listarPorUsuarioIdYEstadoPago(usuarioId, pagada).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<MultaSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        List<MultaSyncDTO> dtos = multaService.listarActualizadosDespuesDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MultaResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody MultaRequestDTO dto) {
        Multa multa = convertToEntity(dto);
        Multa actualizado = multaService.actualizar(id, multa);
        return ResponseEntity.ok(convertToDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        multaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<MultaResponseDTO> pagarMulta(@PathVariable Long id) {
        multaService.pagarMulta(id);
        Multa multa = multaService.obtenerPorId(id);
        return ResponseEntity.ok(convertToDTO(multa));
    }

    private MultaSyncDTO convertToSincronizacionDTO(Multa entity) {
        MultaResponseDTO baseDto = convertToDTO(entity);

        MultaSyncDTO sincroDto = new MultaSyncDTO();

        sincroDto.setId(baseDto.getId());
        sincroDto.setFechaCreacion(baseDto.getFechaCreacion());
        sincroDto.setFechaActualizacion(baseDto.getFechaActualizacion());
        sincroDto.setFechaEmitida(baseDto.getFechaEmitida());
        sincroDto.setFechaVencimiento(baseDto.getFechaVencimiento());
        sincroDto.setImagen(baseDto.getImagen());
        sincroDto.setDescripcion(baseDto.getDescripcion());
        sincroDto.setMonto(baseDto.getMonto());
        sincroDto.setFechaMulta(baseDto.getFechaMulta());
        sincroDto.setFechaLimite(baseDto.getFechaLimite());
        sincroDto.setPagada(baseDto.getPagada());
        sincroDto.setUsuarioId(baseDto.getUsuarioId());

        // 4. Inyectamos la propiedad exclusiva que los demás endpoints no verán
        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private Multa convertToEntity(MultaRequestDTO dto) {
        Multa multa = new Multa();

        multa.setFechaEmitida(dto.getFechaEmitida());
        multa.setFechaVencimiento(dto.getFechaVencimiento());
        multa.setImagen(dto.getImagen());
        multa.setDescripcion(dto.getDescripcion());
        multa.setMonto(dto.getMonto());
        multa.setFechaMulta(dto.getFechaMulta());
        multa.setFechaLimite(dto.getFechaLimite());
        multa.setPagada(dto.getPagada());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioId());
            multa.setUsuario(usuario);
        }

        return multa;
    }

    private MultaResponseDTO convertToDTO(Multa entity) {
        MultaResponseDTO dto = new MultaResponseDTO();

        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setFechaEmitida(entity.getFechaEmitida());
        dto.setFechaVencimiento(entity.getFechaVencimiento());
        dto.setImagen(entity.getImagen());
        dto.setDescripcion(entity.getDescripcion());
        dto.setMonto(entity.getMonto());
        dto.setFechaMulta(entity.getFechaMulta());
        dto.setFechaLimite(entity.getFechaLimite());
        dto.setPagada(entity.getPagada());

        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getId());
        }

        return dto;
    }
}
