package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.DocumentoVehiculo;
import ni.edu.autotrack_apicore.models.RegistroCombustible;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.repositories.DocumentoVehiculoRepository;
import ni.edu.autotrack_apicore.services.DocumentoVehiculoService;
import ni.edu.autotrack_apicore.services.VehiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentoVehiculoServiceImpl implements DocumentoVehiculoService {

    private final DocumentoVehiculoRepository documentoVehiculoRepository;
    private final VehiculoService vehiculoService;

    @Override
    public DocumentoVehiculo crear(DocumentoVehiculo documento) {
        return documentoVehiculoRepository.save(documento);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoVehiculo obtenerPorId(Long id) {
        return documentoVehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento de vehículo no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoVehiculo> listar() {
        return documentoVehiculoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoVehiculo> listarPorVehiculoId(Long vehiculoId) {
        return documentoVehiculoRepository.findByVehiculoId(vehiculoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoVehiculo> listarActualizadosDespuesDe(LocalDateTime fecha) {
        return documentoVehiculoRepository.findUpdatedAfterRaw(fecha);
    }

    @Override
    public DocumentoVehiculo actualizar(Long id, DocumentoVehiculo documentoActualizado) {
        DocumentoVehiculo documento = obtenerPorId(id);
        documento.setNombre(documentoActualizado.getNombre());
        documento.setFechaVencimiento(documentoActualizado.getFechaVencimiento());
        documento.setFechaEmitida(documentoActualizado.getFechaEmitida());
        documento.setImagen(documentoActualizado.getImagen());
        return documentoVehiculoRepository.save(documento);
    }

    @Override
    public void eliminar(Long id) {
        DocumentoVehiculo documento = obtenerPorId(id);

        documento.setFechaActualizacion(java.time.LocalDateTime.now());

        documentoVehiculoRepository.saveAndFlush(documento);

        documentoVehiculoRepository.delete(documento);
    }
}
