package ni.edu.autotrack_apicore.repositories;

import lombok.Getter;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
