package ni.edu.autotrack_apicore.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.base.EntidadBase;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_email", columnNames = "email_usuario"),
        @UniqueConstraint(name = "uk_usuario_username", columnNames = "username")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(columnName = "eliminado", strategy = SoftDeleteType.DELETED)
public class Usuario extends EntidadBase implements UserDetails {

    @Column(name = "nombre_usuario", nullable = false, length = 50)
    private String nombres;

    @Column(name = "apellido_usuario", nullable = false, length = 50)
    private String apellidos;

    @Column(name = "email_usuario", nullable = false, length = 100)
    private String email;

    @Column(name = "numero_telefono", length = 20)
    private String numeroTel;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @Column(name = "pais", nullable = false, length = 50)
    private String pais;

    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Vehiculo> vehiculos;

    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Multa> multas = new ArrayList<>();

    @OneToOne(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private Licencia licencia;

    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Notificacion> notificaciones = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Si no manejas roles (ADMIN, USER), devolvemos una lista con un rol por defecto.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        // ATENCIÓN: Si vas a usar el EMAIL para iniciar sesión, retorna "this.email".
        // Si vas a usar el USERNAME para iniciar sesión, retorna "this.username".
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Cuenta activa
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Cuenta no bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciales vigentes
    }

    @Override
    public boolean isEnabled() {
        return true; // Usuario habilitado
    }
}

