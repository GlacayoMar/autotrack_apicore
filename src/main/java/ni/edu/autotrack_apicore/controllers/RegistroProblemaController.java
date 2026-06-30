package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.RegistroProblemaRequestDTO;
import ni.edu.autotrack_apicore.dto.response.RegistroProblemaResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.RegistroProblemaSyncDTO;
import ni.edu.autotrack_apicore.models.RegistroProblema;
import ni.edu.autotrack_apicore.services.RegistroProblemaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/problemas")
@CrossOrigin
@RequiredArgsConstructor
public class RegistroProblemaController {

    private final RegistroProblemaService problemaService;

    // Reportar una avería o falla en un carro
    @PostMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<RegistroProblemaResponseDTO> reportar(
            @PathVariable Long vehiculoId,
            @RequestBody RegistroProblemaRequestDTO dto) {
        RegistroProblema registro = convertToEntity(dto);
        RegistroProblema nuevoRegistro = problemaService.reportar(vehiculoId, registro);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertToDTO(nuevoRegistro));
    }

    // Listar problemas filtrando opcionalmente por solo activos (abiertos) usando QueryParams
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<RegistroProblemaResponseDTO>> listar(
            @PathVariable Long vehiculoId,
            @RequestParam(defaultValue = "false") boolean soloActivos) {
        List<RegistroProblemaResponseDTO> dtos = problemaService.listarPorVehiculo(vehiculoId, soloActivos).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<RegistroProblemaSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        List<RegistroProblemaSyncDTO> dtos = problemaService.listarActualizadoDespuesDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO) // <-- Usamos el mapeador especializado
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroProblemaResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody RegistroProblemaRequestDTO dto) {
        RegistroProblema rp = convertToEntity(dto);
        RegistroProblema actualizado = problemaService.actualizar(id, rp);
        return ResponseEntity.ok(convertToDTO(actualizado));
    }

    // Marcar un problema mecánico/eléctrico como solucionado
    @PatchMapping("/{id}/solucionar")
    public ResponseEntity<Void> solucionar(@PathVariable Long id) {
        problemaService.solucionarProblema(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content (operación exitosa sin cuerpo)
    }

    // Verificar si el carro tiene permitido circular bajo la ley de Nicaragua (sin fallas graves activas)
    @GetMapping("/vehiculo/{vehiculoId}/apto-circular")
    public ResponseEntity<Boolean> comprobarAptitud(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(problemaService.esVehiculoAptoParaCircular(vehiculoId));
    }

    private RegistroProblemaSyncDTO convertToSincronizacionDTO(RegistroProblema entity) {
        // 1. Reutilizamos el mapeo base (marca, modelo, usuario, etc.)
        RegistroProblemaResponseDTO baseDto = convertToDTO(entity);

        // 2. Construimos el DTO hijo
        RegistroProblemaSyncDTO sincroDto = new RegistroProblemaSyncDTO();

        // 3. Copiamos los datos base
        sincroDto.setId(entity.getId());
        sincroDto.setFechaCreacion(entity.getFechaCreacion());
        sincroDto.setFechaActualizacion(entity.getFechaActualizacion());
        sincroDto.setFechaRegistro(entity.getFechaRegistro());
        sincroDto.setNota(entity.getNota());
        sincroDto.setVehiculoId(entity.getVehiculo().getId());
        sincroDto.setAfectaVehiculo(entity.getAfectaVehiculo());
        sincroDto.setTipoProblema(entity.getTipoProblema());

        // 4. Inyectamos la propiedad exclusiva que los demás endpoints no verán
        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private RegistroProblema convertToEntity(RegistroProblemaRequestDTO dto) {
        RegistroProblema rp = new RegistroProblema();

        rp.setFechaRegistro(dto.getFechaRegistro());
        rp.setNota(dto.getNota());
        rp.setAfectaVehiculo(dto.getAfectaVehiculo());
        rp.setTipoProblema(dto.getTipoProblema());

        return rp;
    }

    private RegistroProblemaResponseDTO convertToDTO(RegistroProblema entity) {
        RegistroProblemaResponseDTO dto = new RegistroProblemaResponseDTO();

        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setFechaRegistro(entity.getFechaRegistro());
        dto.setNota(entity.getNota());
        dto.setVehiculoId(entity.getVehiculo().getId());
        dto.setAfectaVehiculo(entity.getAfectaVehiculo());
        dto.setTipoProblema(entity.getTipoProblema());

        return dto;
    }
}