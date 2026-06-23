package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Notificacion;
import ni.edu.autotrack_apicore.services.NotificacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
@CrossOrigin
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping("/usuario/{usuarioId}/documento/{documentoId}")
    public ResponseEntity<Notificacion> crear(
            @PathVariable Long usuarioId,
            @PathVariable Long documentoId,
            @RequestBody Notificacion notificacion) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificacionService.crear(usuarioId, documentoId, notificacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> listar() {
        return ResponseEntity.ok(notificacionService.listar());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> listarPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarPorUsuarioId(usuarioId));
    }

    @GetMapping("/documento/{documentoId}")
    public ResponseEntity<List<Notificacion>> listarPorDocumentoId(@PathVariable Long documentoId) {
        return ResponseEntity.ok(notificacionService.listarPorDocumentoId(documentoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(
            @PathVariable Long id,
            @RequestBody Notificacion notificacion) {
        return ResponseEntity.ok(notificacionService.actualizar(id, notificacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enviada")
    public ResponseEntity<Notificacion> marcarComoEnviada(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoEnviada(id));
    }

    @PatchMapping("/{id}/ignorar")
    public ResponseEntity<Notificacion> marcarComoIgnorada(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean ignorar) {
        return ResponseEntity.ok(notificacionService.marcarComoIgnorada(id, ignorar));
    }
}
