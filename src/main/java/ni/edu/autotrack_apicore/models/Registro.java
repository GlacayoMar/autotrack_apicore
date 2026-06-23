package ni.edu.autotrack_apicore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.base.EntidadBase;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "registros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(columnName = "eliminado", strategy = SoftDeleteType.ACTIVE)
public abstract class Registro extends EntidadBase {
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "nota", length = 500)
    private String nota;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_vehiculo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_registro_vehiculo")
    )
    @JsonBackReference
    private Vehiculo vehiculo;
}
