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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "documentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(columnName = "eliminado", strategy = SoftDeleteType.DELETED)
public class Documento extends EntidadBase {
    @Column(name = "fecha_vecimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_emitida", nullable = false)
    private LocalDate fechaEmitida;

    @Column(name = "imagen_documento", length = 255)
    private String imagen;

    @OneToMany(
            mappedBy = "documento",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Notificacion> notificaciones = new ArrayList<>();
}
