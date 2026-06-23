package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Licencia;
import ni.edu.autotrack_apicore.services.LicenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/licencias")
@CrossOrigin
@RequiredArgsConstructor
public class LicenciaController {

    private final LicenciaService licenciaService;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Licencia> crear(
            @PathVariable Long usuarioId,
            @RequestBody Licencia licencia) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(licenciaService.crear(usuarioId, licencia));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Licencia> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(licenciaService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Licencia> obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(licenciaService.obtenerPorUsuarioId(usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<Licencia>> listar() {
        return ResponseEntity.ok(licenciaService.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Licencia> actualizar(
            @PathVariable Long id,
            @RequestBody Licencia licencia) {
        return ResponseEntity.ok(licenciaService.actualizar(id, licencia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        licenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
