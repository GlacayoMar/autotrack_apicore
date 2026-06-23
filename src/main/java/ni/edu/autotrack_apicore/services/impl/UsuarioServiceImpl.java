package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.repositories.UsuarioRepository;
import ni.edu.autotrack_apicore.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario crear(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException(
                    "El correo ya se encuentra registrado");
        }

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException(
                    "El username ya se encuentra registrado");
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {

        Usuario usuario = obtenerPorId(id);

        if (!usuarioActualizado.getEmail().equals(usuario.getEmail())
                && usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado");
        }

        usuario.setNombres(usuarioActualizado.getNombres());
        usuario.setApellidos(usuarioActualizado.getApellidos());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setNumeroTel(usuarioActualizado.getNumeroTel());
        usuario.setPais(usuarioActualizado.getPais());
        usuario.setUsername(usuarioActualizado.getUsername());

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuarioRepository.delete(usuario);
    }
}
