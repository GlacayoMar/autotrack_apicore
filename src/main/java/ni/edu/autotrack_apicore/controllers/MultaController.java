package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Multa;
import ni.edu.autotrack_apicore.services.MultaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/multas")
@CrossOrigin
@RequiredArgsConstructor
public class MultaController {

    private final MultaService multaService;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Multa> crear(
            @PathVariable Long usuarioId,
            @RequestBody Multa multa) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(multaService.crear(usuarioId, multa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Multa> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(multaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Multa>> listar() {
        return ResponseEntity.ok(multaService.listar());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Multa>> listarPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(multaService.listarPorUsuarioId(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/estado")
    public ResponseEntity<List<Multa>> listarPorUsuarioIdYEstadoPago(
            @PathVariable Long usuarioId,
            @RequestParam Boolean pagada) {
        return ResponseEntity.ok(multaService.listarPorUsuarioIdYEstadoPago(usuarioId, pagada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Multa> actualizar(
            @PathVariable Long id,
            @RequestBody Multa multa) {
        return ResponseEntity.ok(multaService.actualizar(id, multa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        multaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<Multa> pagarMulta(@PathVariable Long id) {
        return ResponseEntity.ok(multaService.pagarMulta(id));
    }
}
