package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Registro;
import ni.edu.autotrack_apicore.services.RegistroService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/registros")
@CrossOrigin
@RequiredArgsConstructor
public class RegistroGeneralController {

    private final RegistroService registroService;

    // Retorna una lista unificada de combustibles y problemas ordenados por fecha
    @GetMapping("/vehiculo/{vehiculoId}/historial")
    public ResponseEntity<List<Registro>> obtenerHistorialCompleto(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(registroService.listarTodoPorVehiculo(vehiculoId));
    }

    // Permite al frontend buscar todo el historial en un rango de fechas (auditoría mensual/anual)
    @GetMapping("/vehiculo/{vehiculoId}/filtrar")
    public ResponseEntity<List<Registro>> filtrarPorFechas(
            @PathVariable Long vehiculoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(registroService.listarPorVehiculoYRangoFechas(vehiculoId, inicio, fin));
    }

    // Endpoint unificado para borrar cualquier registro (JPA se encarga de limpiar las tablas hijas automáticamente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        registroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}