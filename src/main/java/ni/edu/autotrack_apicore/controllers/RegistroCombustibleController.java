package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.RegistroCombustibleRequestDTO;
import ni.edu.autotrack_apicore.dto.response.RegistroCombustibleResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.RegistroCombustibleSyncDTO;
import ni.edu.autotrack_apicore.models.RegistroCombustible;
import ni.edu.autotrack_apicore.services.DocumentoVehiculoService;
import ni.edu.autotrack_apicore.services.RegistroCombustibleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/combustibles")
@CrossOrigin
@RequiredArgsConstructor
public class RegistroCombustibleController {

    private final RegistroCombustibleService combustibleService;
    private final DocumentoVehiculoService documentoVehiculoService;

    // Registrar una nueva carga de combustible para un vehículo específico
    @PostMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<RegistroCombustibleResponseDTO> registrar(
            @PathVariable Long vehiculoId,
            @RequestBody RegistroCombustibleRequestDTO dto) {
        RegistroCombustible registro = convertToEntity(dto);
        RegistroCombustible nuevoRegistro = combustibleService.crear(vehiculoId, registro);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertToDTO(nuevoRegistro));
    }

    // Obtener todo el historial de combustible de un vehículo
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<RegistroCombustibleResponseDTO>> listarPorVehiculo(@PathVariable Long vehiculoId) {
        List<RegistroCombustibleResponseDTO> dtos = combustibleService.listarPorVehiculo(vehiculoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
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

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<RegistroCombustibleSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        List<RegistroCombustibleSyncDTO> dtos = combustibleService.listarActualizadoDespuesDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO) // <-- Usamos el mapeador especializado
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroCombustibleResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody RegistroCombustibleRequestDTO dto
    ) {
        RegistroCombustible regCombustible = convertToEntity(dto);
        RegistroCombustible actualizado = combustibleService.actualizar(id, regCombustible);
        return ResponseEntity.ok(convertToDTO(actualizado));
    }

    private RegistroCombustibleSyncDTO convertToSincronizacionDTO(RegistroCombustible entity) {
        // 1. Reutilizamos el mapeo base (marca, modelo, usuario, etc.)
        RegistroCombustibleResponseDTO baseDto = convertToDTO(entity);

        // 2. Construimos el DTO hijo
        RegistroCombustibleSyncDTO sincroDto = new RegistroCombustibleSyncDTO();

        // 3. Copiamos los datos base
        sincroDto.setId(entity.getId());
        sincroDto.setFechaCreacion(entity.getFechaCreacion());
        sincroDto.setFechaActualizacion(entity.getFechaActualizacion());
        sincroDto.setFechaRegistro(entity.getFechaRegistro());
        sincroDto.setNota(entity.getNota());
        sincroDto.setVehiculoId(entity.getVehiculo().getId());
        sincroDto.setCantidadCombustible(entity.getCantidadCombustible());
        sincroDto.setCantidadPagado(entity.getCantidadPagado());
        sincroDto.setOdometro(entity.getOdometro());

        // 4. Inyectamos la propiedad exclusiva que los demás endpoints no verán
        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private RegistroCombustible convertToEntity(RegistroCombustibleRequestDTO dto) {
        RegistroCombustible rc = new RegistroCombustible();

        rc.setFechaRegistro(dto.getFechaRegistro());
        rc.setNota(dto.getNota());
        rc.setCantidadCombustible(dto.getCantidadCombustible());
        rc.setCantidadPagado(dto.getCantidadPagado());
        rc.setOdometro(dto.getOdometro());

        return rc;
    }

    private RegistroCombustibleResponseDTO convertToDTO(RegistroCombustible entity) {
        RegistroCombustibleResponseDTO dto = new RegistroCombustibleResponseDTO();

        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setFechaRegistro(entity.getFechaRegistro());
        dto.setNota(entity.getNota());
        dto.setVehiculoId(entity.getVehiculo().getId());
        dto.setCantidadCombustible(entity.getCantidadCombustible());
        dto.setCantidadPagado(entity.getCantidadPagado());
        dto.setOdometro(entity.getOdometro());

        return dto;
    }
}
