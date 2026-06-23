package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Multa;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.repositories.MultaRepository;
import ni.edu.autotrack_apicore.services.MultaService;
import ni.edu.autotrack_apicore.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MultaServiceImpl implements MultaService {

    private final MultaRepository multaRepository;
    private final UsuarioService usuarioService;

    @Override
    public Multa crear(Long usuarioId, Multa multa) {
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        multa.setUsuario(usuario);
        if (multa.getPagada() == null) {
            multa.setPagada(false);
        }
        return multaRepository.save(multa);
    }

    @Override
    @Transactional(readOnly = true)
    public Multa obtenerPorId(Long id) {
        return multaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Multa no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Multa> listar() {
        return multaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Multa> listarPorUsuarioId(Long usuarioId) {
        return multaRepository.findByUsuarioIdOrderByFechaMultaDesc(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Multa> listarPorUsuarioIdYEstadoPago(Long usuarioId, Boolean pagada) {
        return multaRepository.findByUsuarioIdAndPagada(usuarioId, pagada);
    }

    @Override
    public Multa actualizar(Long id, Multa multaActualizada) {
        Multa multa = obtenerPorId(id);
        multa.setFechaVencimiento(multaActualizada.getFechaVencimiento());
        multa.setFechaEmitida(multaActualizada.getFechaEmitida());
        multa.setImagen(multaActualizada.getImagen());
        multa.setDescripcion(multaActualizada.getDescripcion());
        multa.setMonto(multaActualizada.getMonto());
        multa.setFechaMulta(multaActualizada.getFechaMulta());
        multa.setFechaLimite(multaActualizada.getFechaLimite());
        if (multaActualizada.getPagada() != null) {
            multa.setPagada(multaActualizada.getPagada());
        }
        return multaRepository.save(multa);
    }

    @Override
    public void eliminar(Long id) {
        Multa multa = obtenerPorId(id);
        multaRepository.delete(multa);
    }

    @Override
    public Multa pagarMulta(Long id) {
        Multa multa = obtenerPorId(id);
        multa.setPagada(true);
        return multaRepository.save(multa);
    }
}
