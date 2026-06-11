package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.services.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
@CrossOrigin
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    // Registrar un nuevo vehículo
    @PostMapping
    public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo vehiculo) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehiculoService.crear(vehiculo));
    }

    // Obtener un vehículo específico por su ID único
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id));
    }

    // Obtener todos los vehículos registrados en el sistema
    @GetMapping
    public ResponseEntity<List<Vehiculo>> listar() {
        return ResponseEntity.ok(vehiculoService.listar());
    }

    // Modificar datos generales, Placa o VIN de un vehículo de forma segura
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(
            @PathVariable Long id,
            @RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, vehiculo));
    }

    // Borrado lógico del vehículo (Pasa el campo activo a false)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar un vehículo de forma exacta por su número de placa
    @GetMapping("/buscar-por-placa")
    public ResponseEntity<Vehiculo> obtenerPorPlaca(@RequestParam String placa) {
        return ResponseEntity.ok(vehiculoService.obtenerPorPlaca(placa));
    }

    // Buscar un vehículo de forma exacta por su número de VIN / Chasis
    @GetMapping("/buscar-por-vin")
    public ResponseEntity<Vehiculo> obtenerPorVin(@RequestParam String vin) {
        return ResponseEntity.ok(vehiculoService.obtenerPorVin(vin));
    }

    // Listar todos los vehículos asignados o pertenecientes a un usuario (Dueño / Conductor)
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Vehiculo>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(vehiculoService.listarPorUsuarioId(usuarioId));
    }
}
