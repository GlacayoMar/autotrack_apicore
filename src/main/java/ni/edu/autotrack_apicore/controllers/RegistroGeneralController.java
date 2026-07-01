package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.response.RegistroResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.RegistroSyncDTO;
import ni.edu.autotrack_apicore.models.Registro;
import ni.edu.autotrack_apicore.services.RegistroService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/registros")
@CrossOrigin
@RequiredArgsConstructor
public class RegistroGeneralController {

    private final RegistroService registroService;

    // Retorna una lista unificada de combustibles y problemas ordenados por fecha
    @GetMapping("/vehiculo/{vehiculoId}/historial")
    public ResponseEntity<List<RegistroResponseDTO>> obtenerHistorialCompleto(@PathVariable Long vehiculoId) {
        List<RegistroResponseDTO> dtos = registroService.listarTodoPorVehiculo(vehiculoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Permite al frontend buscar todo el historial en un rango de fechas (auditoría mensual/anual)
    @GetMapping("/vehiculo/{vehiculoId}/filtrar")
    public ResponseEntity<List<RegistroResponseDTO>> filtrarPorFechas(
            @PathVariable Long vehiculoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
                List<RegistroResponseDTO> dtos = registroService.listarPorVehiculoYRangoFechas(vehiculoId, inicio, fin).stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<RegistroSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        List<RegistroSyncDTO> dtos = registroService.listarActualizadosDespuesDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // Endpoint unificado para borrar cualquier registro (JPA se encarga de limpiar las tablas hijas automáticamente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        registroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private RegistroSyncDTO convertToSincronizacionDTO(Registro entity) {
        RegistroResponseDTO baseDto = convertToDTO(entity);

        RegistroSyncDTO sincroDto = new RegistroSyncDTO();

        sincroDto.setId(baseDto.getId());
        sincroDto.setFechaCreacion(baseDto.getFechaCreacion());
        sincroDto.setFechaActualizacion(baseDto.getFechaActualizacion());
        sincroDto.setNota(baseDto.getNota());
        sincroDto.setFechaRegistro(baseDto.getFechaRegistro());
        sincroDto.setVehiculoId(baseDto.getVehiculoId());

        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private RegistroResponseDTO convertToDTO(Registro entity) {
        RegistroResponseDTO dto = new RegistroResponseDTO();
        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setNota(entity.getNota());
        dto.setFechaRegistro(entity.getFechaRegistro());
        dto.setVehiculoId(entity.getVehiculo().getId());

        return dto;
    }
}