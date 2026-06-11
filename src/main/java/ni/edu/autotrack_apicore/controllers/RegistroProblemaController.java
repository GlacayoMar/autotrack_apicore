package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.RegistroProblema;
import ni.edu.autotrack_apicore.services.RegistroProblemaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/problemas")
@CrossOrigin
@RequiredArgsConstructor
public class RegistroProblemaController {

    private final RegistroProblemaService problemaService;

    // Reportar una avería o falla en un carro
    @PostMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<RegistroProblema> reportar(
            @PathVariable Long vehiculoId,
            @RequestBody RegistroProblema problema) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(problemaService.reportar(vehiculoId, problema));
    }

    // Listar problemas filtrando opcionalmente por solo activos (abiertos) usando QueryParams
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<RegistroProblema>> listar(
            @PathVariable Long vehiculoId,
            @RequestParam(defaultValue = "false") boolean soloActivos) {
        return ResponseEntity.ok(problemaService.listarPorVehiculo(vehiculoId, soloActivos));
    }

    // Marcar un problema mecánico/eléctrico como solucionado
    @PutMapping("/{id}/solucionar")
    public ResponseEntity<Void> solucionar(@PathVariable Long id) {
        problemaService.solucionarProblema(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content (operación exitosa sin cuerpo)
    }

    // Verificar si el carro tiene permitido circular bajo la ley de Nicaragua (sin fallas graves activas)
    @GetMapping("/vehiculo/{vehiculoId}/apto-circular")
    public ResponseEntity<Boolean> comprobarAptitud(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(problemaService.esVehiculoAptoParaCircular(vehiculoId));
    }
}