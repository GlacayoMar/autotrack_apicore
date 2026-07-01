package ni.edu.autotrack_apicore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.enums.CategoriaLicencia;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "licencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Licencia extends Documento {

    @ElementCollection(targetClass = CategoriaLicencia.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "licencia_categorias",
            joinColumns = @JoinColumn(
                    name = "id_documento",
                    foreignKey = @ForeignKey(name = "fk_categorias_licencia")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_licencia", nullable = false, length = 30)
    private Set<CategoriaLicencia> categorias = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_licencia_usuario")
    )
    @JsonBackReference
    private Usuario usuario;
}
