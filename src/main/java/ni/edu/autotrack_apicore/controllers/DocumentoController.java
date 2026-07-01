package ni.edu.autotrack_apicore.controllers;

import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.DocumentoRequestDTO;
import ni.edu.autotrack_apicore.dto.response.DocumentoResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.DocumentoSyncDTO;
import ni.edu.autotrack_apicore.models.Documento;
import ni.edu.autotrack_apicore.services.DocumentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/documentos")
@CrossOrigin
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDTO(documentoService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<DocumentoResponseDTO>> listar() {
        List<DocumentoResponseDTO> dtos = documentoService.listar().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoResponseDTO> actualizar(@PathVariable Long id, @RequestBody DocumentoRequestDTO dto) {
        Documento doc = convertToEntity(dto);
        Documento docActualizado = documentoService.actualizar(id, doc);
        return ResponseEntity.ok(convertToDTO(docActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        documentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<DocumentoResponseDTO>> listarVencidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<DocumentoResponseDTO> dtos = documentoService.listarVencidosAntesDe(fecha).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<DocumentoSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        List<DocumentoSyncDTO> dtos = documentoService.listarActualizadosDespuesDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    private DocumentoSyncDTO convertToSincronizacionDTO(Documento entity) {
        DocumentoResponseDTO baseDto = convertToDTO(entity);

        DocumentoSyncDTO sincroDto = new DocumentoSyncDTO();

        sincroDto.setId(baseDto.getId());
        sincroDto.setFechaCreacion(baseDto.getFechaCreacion());
        sincroDto.setFechaActualizacion(baseDto.getFechaActualizacion());
        sincroDto.setFechaEmitida(baseDto.getFechaEmitida());
        sincroDto.setFechaVencimiento(baseDto.getFechaVencimiento());
        sincroDto.setImagen(baseDto.getImagen());

        // 4. Inyectamos la propiedad exclusiva que los demás endpoints no verán
        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private Documento convertToEntity(DocumentoRequestDTO dto) {
        Documento doc = new Documento();

        doc.setFechaEmitida(dto.getFechaEmitida());
        doc.setFechaVencimiento(dto.getFechaVencimiento());
        doc.setImagen(dto.getImagen());

        return doc;
    }

    private DocumentoResponseDTO convertToDTO(Documento entity) {
        DocumentoResponseDTO dto = new DocumentoResponseDTO();
        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setFechaEmitida(entity.getFechaEmitida());
        dto.setFechaVencimiento(entity.getFechaVencimiento());
        dto.setImagen(entity.getImagen());


        return dto;
    }
}
