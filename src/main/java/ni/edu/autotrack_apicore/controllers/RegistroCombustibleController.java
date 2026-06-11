package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.RegistroCombustible;
import ni.edu.autotrack_apicore.services.RegistroCombustibleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/combustibles")
@CrossOrigin
@RequiredArgsConstructor
public class RegistroCombustibleController {

    private final RegistroCombustibleService combustibleService;

    // Registrar una nueva carga de combustible para un vehículo específico
    @PostMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<RegistroCombustible> registrar(
            @PathVariable Long vehiculoId,
            @RequestBody RegistroCombustible registro) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(combustibleService.crear(vehiculoId, registro));
    }

    // Obtener todo el historial de combustible de un vehículo
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<RegistroCombustible>> listarPorVehiculo(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(combustibleService.listarPorVehiculo(vehiculoId));
    }

    // Endpoint de estadística: Total gastado en dinero
    @GetMapping("/vehiculo/{vehiculoId}/total-gastado")
    public ResponseEntity<BigDecimal> obtenerTotalGastado(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(combustibleService.obtenerTotalGastado(vehiculoId));
    }

    // Endpoint de estadística: Rendimiento promedio (Km por Litro/Galón)
    @GetMapping("/vehiculo/{vehiculoId}/rendimiento")
    public ResponseEntity<Double> calcularRendimiento(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(combustibleService.calcularRendimientoPromedio(vehiculoId));
    }
}
