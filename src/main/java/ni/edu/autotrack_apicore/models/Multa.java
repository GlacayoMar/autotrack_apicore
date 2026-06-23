package ni.edu.autotrack_apicore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "multas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Multa extends Documento {
    @Column(name = "descripcion_multa", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "monto_multa", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_multa", nullable = false)
    private LocalDate fechaMulta;

    @Column(name = "fecha_limite", nullable = false)
    private LocalDate fechaLimite;

    @Column(name = "pagada_multa", nullable = false)
    private Boolean pagada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_multa_usuario")
    )
    @JsonBackReference
    private Usuario usuario;
}
