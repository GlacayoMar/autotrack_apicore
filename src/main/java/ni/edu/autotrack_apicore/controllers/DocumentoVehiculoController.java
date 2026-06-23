package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.DocumentoVehiculo;
import ni.edu.autotrack_apicore.services.DocumentoVehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos-vehiculos")
@CrossOrigin
@RequiredArgsConstructor
public class DocumentoVehiculoController {

    private final DocumentoVehiculoService documentoVehiculoService;

    @PostMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<DocumentoVehiculo> crear(
            @PathVariable Long vehiculoId,
            @RequestBody DocumentoVehiculo documento) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentoVehiculoService.crear(vehiculoId, documento));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoVehiculo> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(documentoVehiculoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<DocumentoVehiculo>> listar() {
        return ResponseEntity.ok(documentoVehiculoService.listar());
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<DocumentoVehiculo>> listarPorVehiculoId(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(documentoVehiculoService.listarPorVehiculoId(vehiculoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoVehiculo> actualizar(
            @PathVariable Long id,
            @RequestBody DocumentoVehiculo documento) {
        return ResponseEntity.ok(documentoVehiculoService.actualizar(id, documento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        documentoVehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
