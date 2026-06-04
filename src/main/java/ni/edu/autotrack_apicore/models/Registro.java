package ni.edu.autotrack_apicore.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.base.EntidadBase;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "registros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Registro extends EntidadBase {
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "nota")
    private String nota;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_vehiculo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_registro_vehiculo")
    )
    private Vehiculo vehiculo;
}
