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

import java.util.ArrayList;
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
@SoftDelete(columnName = "eliminado", strategy = SoftDeleteType.ACTIVE)
public class Usuario extends EntidadBase {

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
    private String password; // hay que chuncharlo

    @Column(name = "pais", nullable = false, length = 50)
    private String pais;

    @OneToMany(mappedBy = "usuario")
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
}

