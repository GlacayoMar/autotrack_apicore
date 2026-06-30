package ni.edu.autotrack_apicore.controllers;


import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.dto.request.VehiculoRequestDTO;
import ni.edu.autotrack_apicore.dto.response.VehiculoResponseDTO;
import ni.edu.autotrack_apicore.dto.sync.VehiculoSyncDTO;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.services.VehiculoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/vehiculos")
@CrossOrigin
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping
    public ResponseEntity<VehiculoResponseDTO> crear(@RequestBody VehiculoRequestDTO dto) {
        Vehiculo vehiculo = convertToEntity(dto);
        Vehiculo nuevoVehiculo = vehiculoService.crear(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(nuevoVehiculo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDTO(vehiculoService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<VehiculoResponseDTO>> listar() {
        List<VehiculoResponseDTO> dtos = vehiculoService.listar().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody VehiculoRequestDTO dto) {
        Vehiculo vehiculoDetalles = convertToEntity(dto);
        Vehiculo actualizado = vehiculoService.actualizar(id, vehiculoDetalles);
        return ResponseEntity.ok(convertToDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-por-placa")
    public ResponseEntity<VehiculoResponseDTO> obtenerPorPlaca(@RequestParam String placa) {
        return ResponseEntity.ok(convertToDTO(vehiculoService.obtenerPorPlaca(placa)));
    }

    @GetMapping("/buscar-por-vin")
    public ResponseEntity<VehiculoResponseDTO> obtenerPorVin(@RequestParam String vin) {
        return ResponseEntity.ok(convertToDTO(vehiculoService.obtenerPorVin(vin)));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<VehiculoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<VehiculoResponseDTO> dtos = vehiculoService.listarPorUsuarioId(usuarioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/updated-after/{timestamp}")
    public ResponseEntity<List<VehiculoSyncDTO>> listarActualizadosDespuesDe(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        List<VehiculoSyncDTO> dtos = vehiculoService.listarActualizadosDespuesDe(timestamp).stream()
                .map(this::convertToSincronizacionDTO) // <-- Usamos el mapeador especializado
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // Mapeador exclusivo para el endpoint de sincronización
    private VehiculoSyncDTO convertToSincronizacionDTO(Vehiculo entity) {
        // 1. Reutilizamos el mapeo base (marca, modelo, usuario, etc.)
        VehiculoResponseDTO baseDto = convertToDTO(entity);

        // 2. Construimos el DTO hijo
        VehiculoSyncDTO sincroDto = new VehiculoSyncDTO();

        // 3. Copiamos los datos base
        sincroDto.setId(baseDto.getId());
        sincroDto.setFechaCreacion(baseDto.getFechaCreacion());
        sincroDto.setFechaActualizacion(baseDto.getFechaActualizacion());
        sincroDto.setActivo(baseDto.getActivo());
        sincroDto.setMarca(baseDto.getMarca());
        sincroDto.setModelo(baseDto.getModelo());
        sincroDto.setAnio(baseDto.getAnio());
        sincroDto.setPlaca(baseDto.getPlaca());
        sincroDto.setVin(baseDto.getVin());
        sincroDto.setEstado(baseDto.getEstado());
        sincroDto.setImagenes(baseDto.getImagenes());
        sincroDto.setUsuarioId(entity.getUsuario().getId());

        // 4. Inyectamos la propiedad exclusiva que los demás endpoints no verán
        sincroDto.setEliminado(entity.getEliminado());

        return sincroDto;
    }

    private Vehiculo convertToEntity(VehiculoRequestDTO dto) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setVin(dto.getVin());
        vehiculo.setEstado(dto.getEstado());
        vehiculo.setImagenes(dto.getImagenes());

        // Relacionamos al usuario usando solo el ID enviado
        if (dto.getUsuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioId());
            vehiculo.setUsuario(usuario);
        }
        return vehiculo;
    }

    private VehiculoResponseDTO convertToDTO(Vehiculo entity) {
        VehiculoResponseDTO dto = new VehiculoResponseDTO();
        dto.setId(entity.getId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        dto.setActivo(entity.getActivo());
        dto.setMarca(entity.getMarca());
        dto.setModelo(entity.getModelo());
        dto.setAnio(entity.getAnio());
        dto.setPlaca(entity.getPlaca());
        dto.setVin(entity.getVin());
        dto.setEstado(entity.getEstado());
        dto.setImagenes(entity.getImagenes());

        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getId());
        }
        return dto;
    }
}