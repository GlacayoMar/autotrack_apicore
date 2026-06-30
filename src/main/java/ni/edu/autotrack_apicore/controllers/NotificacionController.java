package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.NotificacionRequestDTO;
import ni.edu.autotrack_apicore.dto.request.VehiculoRequestDTO;
import ni.edu.autotrack_apicore.dto.response.NotificacionResponseDTO;
import ni.edu.autotrack_apicore.dto.response.VehiculoResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.NotificacionSyncDTO;
import ni.edu.autotrack_apicore.dto.sync.VehiculoSyncDTO;
import ni.edu.autotrack_apicore.models.Documento;
import ni.edu.autotrack_apicore.models.Notificacion;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.services.NotificacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notificaciones")
@CrossOrigin
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping("/usuario/{usuarioId}/documento/{documentoId}")
    public ResponseEntity<NotificacionResponseDTO> crear(
            @PathVariable Long usuarioId,
            @PathVariable Long documentoId,
            @RequestBody NotificacionRequestDTO dto) {
        Notificacion noti = convertToEntity(dto);
        Notificacion notificacion = notificacionService.crear(usuarioId, documentoId, noti);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertToDTO(notificacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDTO(notificacionService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listar() {
        List<NotificacionResponseDTO> dtos = notificacionService.listar().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorUsuarioId(@PathVariable Long usuarioId) {
        List<NotificacionResponseDTO> dtos = notificacionService.listarPorUsuarioId(usuarioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/documento/{documentoId}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorDocumentoId(@PathVariable Long documentoId) {
        List<NotificacionResponseDTO> dtos = notificacionService.listarPorDocumentoId(documentoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody NotificacionRequestDTO dto) {
        Notificacion noti = convertToEntity(dto);
        Notificacion actualizado = notificacionService.actualizar(id, noti);
        return ResponseEntity.ok(convertToDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enviada")
    public ResponseEntity<NotificacionResponseDTO> marcarComoEnviada(@PathVariable Long id) {
        notificacionService.marcarComoEnviada(id);
        Notificacion notificacion = notificacionService.obtenerPorId(id);
        return ResponseEntity.ok(convertToDTO(notificacion));
    }

    @PatchMapping("/{id}/ignorar")
    public ResponseEntity<NotificacionResponseDTO> marcarComoIgnorada(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean ignorar) {
        notificacionService.marcarComoIgnorada(id, ignorar);
        Notificacion notificacion = notificacionService.obtenerPorId(id);
        return ResponseEntity.ok(convertToDTO(notificacion));
    }

    private NotificacionSyncDTO convertToSincronizacionDTO(Notificacion entity) {
        // 1. Reutilizamos el mapeo base (marca, modelo, usuario, etc.)
        NotificacionResponseDTO baseDto = convertToDTO(entity);

        // 2. Construimos el DTO hijo
        NotificacionSyncDTO sincroDto = new NotificacionSyncDTO();

        // 3. Copiamos los datos base
        sincroDto.setId(baseDto.getId());
        sincroDto.setFechaCreacion(baseDto.getFechaCreacion());
        sincroDto.setFechaActualizacion(baseDto.getFechaActualizacion());
        sincroDto.setFechaInicio(baseDto.getFechaInicio());
        sincroDto.setFechaFinal(baseDto.getFechaFinal());
        sincroDto.setFrecuencia(baseDto.getFrecuencia());
        sincroDto.setIgnorar(baseDto.getIgnorar());
        sincroDto.setMensaje(baseDto.getMensaje());
        sincroDto.setEnviada(baseDto.getEnviada());
        sincroDto.setTipo(baseDto.getTipo());
        sincroDto.setDocumentoId(entity.getDocumento().getId());
        sincroDto.setUsuarioId(entity.getUsuario().getId());

        // 4. Inyectamos la propiedad exclusiva que los demás endpoints no verán
        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private Notificacion convertToEntity(NotificacionRequestDTO dto) {
        Notificacion noti = new Notificacion();

        noti.setFechaInicio(dto.getFechaInicio());
        noti.setFechaFinal(dto.getFechaFinal());
        noti.setFrecuencia(dto.getFrecuencia());
        noti.setIgnorar(dto.getIgnorar());
        noti.setMensaje(dto.getMensaje());
        noti.setEnviada(dto.getEnviada());
        noti.setTipo(dto.getTipo());

        return noti;
    }

    private NotificacionResponseDTO convertToDTO(Notificacion entity) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setFechaInicio(entity.getFechaInicio());
        dto.setFechaFinal(entity.getFechaFinal());
        dto.setFrecuencia(entity.getFrecuencia());
        dto.setIgnorar(entity.getIgnorar());
        dto.setMensaje(entity.getMensaje());
        dto.setEnviada(entity.getEnviada());
        dto.setTipo(entity.getTipo());

        if(entity.getDocumento() != null) {
            dto.setDocumentoId(entity.getDocumento().getId());
        }

        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getId());
        }

        return dto;
    }
}
