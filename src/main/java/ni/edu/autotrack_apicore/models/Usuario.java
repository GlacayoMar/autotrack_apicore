package ni.edu.autotrack_apicore.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.base.EntidadBase;

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
    private List<Vehiculo> vehiculos;
}

