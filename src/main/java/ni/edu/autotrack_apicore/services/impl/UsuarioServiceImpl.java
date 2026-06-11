package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Usuario;
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

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long id) {

        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(false);
    }
}
