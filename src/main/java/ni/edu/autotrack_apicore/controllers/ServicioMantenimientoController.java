package ni.edu.autotrack_apicore.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.ServicioMantenimientoRequestDTO;
import ni.edu.autotrack_apicore.dto.response.ServicioMantenimientoResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.ServicioMantenimientoSyncDTO;
import ni.edu.autotrack_apicore.models.ServicioMantenimiento;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.services.ServicioMantenimientoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/servicios_mantenimiento")
@CrossOrigin
@RequiredArgsConstructor
public class ServicioMantenimientoController {
    private final ServicioMantenimientoService servicioMantenimientoService;

    @PostMapping
    public ResponseEntity<ServicioMantenimientoResponseDTO> crear(
            @Valid @RequestBody ServicioMantenimientoRequestDTO dto) {

        ServicioMantenimiento entidad = convertToEntity(dto);
        ServicioMantenimiento nuevoServicio = servicioMantenimientoService.crear(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(nuevoServicio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioMantenimientoResponseDTO> obtener(@PathVariable Long id) {
        ServicioMantenimiento servicio = servicioMantenimientoService.obtenerPorId(id);
        return ResponseEntity.ok(convertToDTO(servicio));
    }

    // 3. LISTAR TODO: Devuelve el universo global de mantenimientos
    @GetMapping
    public ResponseEntity<List<ServicioMantenimientoResponseDTO>> listar() {
        List<ServicioMantenimientoResponseDTO> lista = servicioMantenimientoService.listar().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/vehiculos/{vehiculoId}/servicios_mantenimiento")
    public ResponseEntity<List<ServicioMantenimientoResponseDTO>> listarPorVehiculo(@PathVariable Long vehiculoId) {
        List<ServicioMantenimientoResponseDTO> lista = servicioMantenimientoService.listarPorVehiculoId(vehiculoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<ServicioMantenimientoSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        // Nota: Asegúrate de tener el método 'listarActualizadosDespuesDe' en tu Servicio
        List<ServicioMantenimientoSyncDTO> dtos = servicioMantenimientoService.listarActualizadosDespuesDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioMantenimientoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicioMantenimientoRequestDTO dto) {

        ServicioMantenimiento detalles = convertToEntity(dto);
        ServicioMantenimiento actualizado = servicioMantenimientoService.actualizar(id, detalles);
        return ResponseEntity.ok(convertToDTO(actualizado));
    }

    @PatchMapping("/{id}/completado")
    public ResponseEntity<ServicioMantenimientoResponseDTO> cambiarEstadoCompletado(
            @PathVariable Long id,
            @RequestParam boolean completado) {

        ServicioMantenimiento modificado = servicioMantenimientoService.cambiarEstadoCompletado(id, completado);
        return ResponseEntity.ok(convertToDTO(modificado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioMantenimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ServicioMantenimientoSyncDTO convertToSincronizacionDTO(ServicioMantenimiento entity) {

        ServicioMantenimientoResponseDTO baseDto = convertToDTO(entity);

        ServicioMantenimientoSyncDTO sincroDto = new ServicioMantenimientoSyncDTO();

        sincroDto.setId(baseDto.getId());
        sincroDto.setFechaCreacion(baseDto.getFechaCreacion());
        sincroDto.setFechaActualizacion(baseDto.getFechaActualizacion());
        sincroDto.setActivo(baseDto.getActivo());
        sincroDto.setTitulo(baseDto.getTitulo());
        sincroDto.setDescripcion(baseDto.getDescripcion());
        sincroDto.setAfectaVehiculo(baseDto.getAfectaVehiculo());
        sincroDto.setCompletado(baseDto.getCompletado());
        sincroDto.setDistanciAgendada(baseDto.getDistanciAgendada());
        sincroDto.setObservaciones(baseDto.getObservaciones());
        sincroDto.setTipoMantenimiento(baseDto.getTipoMantenimiento());
        sincroDto.setVehiculoId(baseDto.getVehiculoId());

        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private ServicioMantenimiento convertToEntity(ServicioMantenimientoRequestDTO dto) {
        ServicioMantenimiento entity = new ServicioMantenimiento();
        entity.setTitulo(dto.getTitulo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setAfectaVehiculo(dto.getAfectaVehiculo());
        entity.setCompletado(dto.getCompletado());
        entity.setDistanciaAgendada(dto.getDistanciAgendada());
        entity.setObservaciones(dto.getObservaciones());
        entity.setTipo(dto.getTipoMantenimiento());
        entity.setFechaAgendada(dto.getFechaAgendada());

        if (dto.getVehiculoId() != null) {
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setId(dto.getVehiculoId());
            entity.setVehiculo(vehiculo);
        }
        // Nota: La fechaAgendada se asume mapeada si tu RequestDTO cuenta con ella
        return entity;
    }

    private ServicioMantenimientoResponseDTO convertToDTO(ServicioMantenimiento entity) {
        ServicioMantenimientoResponseDTO dto = new ServicioMantenimientoResponseDTO();
        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setActivo(entity.getActivo());
        dto.setTitulo(entity.getTitulo());
        dto.setDescripcion(entity.getDescripcion());
        dto.setAfectaVehiculo(entity.getAfectaVehiculo());
        dto.setCompletado(entity.getCompletado());
        dto.setDistanciAgendada(entity.getDistanciaAgendada());
        dto.setObservaciones(entity.getObservaciones());
        dto.setTipoMantenimiento(entity.getTipo());
        dto.setFechaAgendada(entity.getFechaAgendada());

        // Mapeo seguro del ID relacional para evitar NullPointerException si la entidad se recupera sin vehículo
        if (entity.getVehiculo() != null) {
            dto.setVehiculoId(entity.getVehiculo().getId());
        }

        return dto;
    }
}
