package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Licencia;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.repositories.LicenciaRepository;
import ni.edu.autotrack_apicore.services.LicenciaService;
import ni.edu.autotrack_apicore.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LicenciaServiceImpl implements LicenciaService {

    private final LicenciaRepository licenciaRepository;
    private final UsuarioService usuarioService;

    @Override
    public Licencia crear(Long usuarioId, Licencia licencia) {
        if (licenciaRepository.existsByUsuarioId(usuarioId)) {
            throw new IllegalArgumentException("El usuario ya cuenta con una licencia registrada");
        }
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        licencia.setUsuario(usuario);
        return licenciaRepository.save(licencia);
    }

    @Override
    @Transactional(readOnly = true)
    public Licencia obtenerPorId(Long id) {
        return licenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Licencia no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Licencia obtenerPorUsuarioId(Long usuarioId) {
        return licenciaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró licencia para el usuario con ID: " + usuarioId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Licencia> listar() {
        return licenciaRepository.findAll();
    }

    @Override
    public Licencia actualizar(Long id, Licencia licenciaActualizada) {
        Licencia licencia = obtenerPorId(id);
        licencia.setFechaVencimiento(licenciaActualizada.getFechaVencimiento());
        licencia.setFechaEmitida(licenciaActualizada.getFechaEmitida());
        licencia.setImagen(licenciaActualizada.getImagen());
        licencia.setCategorias(licenciaActualizada.getCategorias());
        return licenciaRepository.save(licencia);
    }

    @Override
    public void eliminar(Long id) {
        Licencia licencia = obtenerPorId(id);
        licenciaRepository.delete(licencia);
    }
}
