package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsername(String username);

    @Query(value = "SELECT * FROM usuarios WHERE fecha_actualizacion > :fecha", nativeQuery = true)
    List<Usuario> findUpdatedAfterRaw(@Param("fecha") LocalDateTime fecha);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
