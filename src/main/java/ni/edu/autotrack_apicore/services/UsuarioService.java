package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario crear(Usuario usuario);

    Usuario obtenerPorId(Long id);

    List<Usuario> listar();

    Usuario actualizar(Long id, Usuario usuario);

    void eliminar(Long id);
}
