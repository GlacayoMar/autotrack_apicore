package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.DocumentoVehiculo;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.repositories.DocumentoVehiculoRepository;
import ni.edu.autotrack_apicore.services.DocumentoVehiculoService;
import ni.edu.autotrack_apicore.services.VehiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentoVehiculoServiceImpl implements DocumentoVehiculoService {

    private final DocumentoVehiculoRepository documentoVehiculoRepository;
    private final VehiculoService vehiculoService;

    @Override
    public DocumentoVehiculo crear(Long vehiculoId, DocumentoVehiculo documento) {
        Vehiculo vehiculo = vehiculoService.obtenerPorId(vehiculoId);
        documento.setVehiculo(vehiculo);
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
        documentoVehiculoRepository.delete(documento);
    }
}
