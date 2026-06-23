package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Documento;
import ni.edu.autotrack_apicore.repositories.DocumentoRepository;
import ni.edu.autotrack_apicore.services.DocumentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentoServiceImpl implements DocumentoService {

    private final DocumentoRepository documentoRepository;

    @Override
    @Transactional(readOnly = true)
    public Documento obtenerPorId(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Documento> listar() {
        return documentoRepository.findAll();
    }

    @Override
    public Documento actualizar(Long id, Documento documentoActualizado) {
        Documento documento = obtenerPorId(id);
        documento.setFechaVencimiento(documentoActualizado.getFechaVencimiento());
        documento.setFechaEmitida(documentoActualizado.getFechaEmitida());
        documento.setImagen(documentoActualizado.getImagen());
        return documentoRepository.save(documento);
    }

    @Override
    public void eliminar(Long id) {
        Documento documento = obtenerPorId(id);
        documentoRepository.delete(documento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Documento> listarVencidosAntesDe(LocalDate fecha) {
        return documentoRepository.findByFechaVencimientoBefore(fecha);
    }
}
