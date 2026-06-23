package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Documento;
import ni.edu.autotrack_apicore.services.DocumentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos")
@CrossOrigin
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    @GetMapping("/{id}")
    public ResponseEntity<Documento> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(documentoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Documento>> listar() {
        return ResponseEntity.ok(documentoService.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Documento> actualizar(@PathVariable Long id, @RequestBody Documento documento) {
        return ResponseEntity.ok(documentoService.actualizar(id, documento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        documentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<Documento>> listarVencidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(documentoService.listarVencidosAntesDe(fecha));
    }
}
