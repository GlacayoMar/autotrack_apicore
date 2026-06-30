package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.DocumentoVehiculoRequestDTO;
import ni.edu.autotrack_apicore.dto.response.DocumentoVehiculoResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.DocumentoVehiculoSyncDTO;
import ni.edu.autotrack_apicore.dto.sync.RegistroCombustibleSyncDTO;
import ni.edu.autotrack_apicore.models.DocumentoVehiculo;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.services.DocumentoVehiculoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/documentos-vehiculos")
@CrossOrigin
@RequiredArgsConstructor
public class DocumentoVehiculoController {

    private final DocumentoVehiculoService documentoVehiculoService;

    @PostMapping
    public ResponseEntity<DocumentoVehiculoResponseDTO> crear(
            @RequestBody DocumentoVehiculoRequestDTO dto) {
        DocumentoVehiculo doc = convertToEntity(dto);
        DocumentoVehiculo documento = documentoVehiculoService.crear(doc);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertToDTO(documento));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoVehiculoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDTO(documentoVehiculoService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<DocumentoVehiculoResponseDTO>> listar() {
        List<DocumentoVehiculoResponseDTO> dtos = documentoVehiculoService.listar().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<DocumentoVehiculoResponseDTO>> listarPorVehiculoId(@PathVariable Long vehiculoId) {
        List<DocumentoVehiculoResponseDTO> dtos = documentoVehiculoService.listarPorVehiculoId(vehiculoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<DocumentoVehiculoSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        List<DocumentoVehiculoSyncDTO> dtos = documentoVehiculoService.listarActualizadosDespuesDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoVehiculoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody DocumentoVehiculoRequestDTO dto) {
        DocumentoVehiculo docVehiculo = convertToEntity(dto);
        DocumentoVehiculo documentoActualizado = documentoVehiculoService.actualizar(id, docVehiculo);
        return ResponseEntity.ok(convertToDTO(documentoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        documentoVehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private DocumentoVehiculoSyncDTO convertToSincronizacionDTO(DocumentoVehiculo entity) {
        DocumentoVehiculoResponseDTO baseDto = convertToDTO(entity);

        DocumentoVehiculoSyncDTO sincroDto = new DocumentoVehiculoSyncDTO();

        sincroDto.setId(baseDto.getId());
        sincroDto.setFechaCreacion(baseDto.getFechaCreacion());
        sincroDto.setFechaActualizacion(baseDto.getFechaActualizacion());
        sincroDto.setFechaEmitida(baseDto.getFechaEmitida());
        sincroDto.setFechaVencimiento(baseDto.getFechaVencimiento());
        sincroDto.setImagen(baseDto.getImagen());
        sincroDto.setNombre(entity.getNombre());
        sincroDto.setVehiculoId(entity.getVehiculo().getId());

        // 4. Inyectamos la propiedad exclusiva que los demás endpoints no verán
        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private DocumentoVehiculo convertToEntity(DocumentoVehiculoRequestDTO dto) {
        DocumentoVehiculo doc = new DocumentoVehiculo();

        doc.setFechaEmitida(dto.getFechaEmitida());
        doc.setFechaVencimiento(dto.getFechaVencimiento());
        doc.setImagen(dto.getImagen());
        doc.setNombre(dto.getNombre());

        if (dto.getVehiculoId() != null) {
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setId(dto.getVehiculoId());
            doc.setVehiculo(vehiculo);
        }

        return doc;
    }

    private DocumentoVehiculoResponseDTO convertToDTO(DocumentoVehiculo entity) {
        DocumentoVehiculoResponseDTO dto = new DocumentoVehiculoResponseDTO();
        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setFechaEmitida(entity.getFechaEmitida());
        dto.setFechaVencimiento(entity.getFechaVencimiento());
        dto.setImagen(entity.getImagen());
        dto.setFechaEmitida(entity.getFechaEmitida());
        dto.setFechaVencimiento(entity.getFechaVencimiento());
        dto.setImagen(entity.getImagen());
        dto.setNombre(entity.getNombre());

        if (entity.getVehiculo() != null) {
            dto.setVehiculoId(entity.getVehiculo().getId());
        }

        return dto;
    }
}
