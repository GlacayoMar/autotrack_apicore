package ni.edu.autotrack_apicore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.base.EntidadBase;
import ni.edu.autotrack_apicore.models.enums.TipoNotificacion;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.time.LocalDate;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(columnName = "eliminado", strategy = SoftDeleteType.ACTIVE)
public class Notificacion extends EntidadBase {

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_final", nullable = false)
    private LocalDate fechaFinal;

    @Column(name = "frecuencia_notificacion", length = 50)
    private String frecuencia;

    @Column(name = "ignorar_notificacion", nullable = false)
    private Boolean ignorar;

    @Column(name = "mensaje_notificacion", nullable = false, length = 255)
    private String mensaje;

    @Column(name = "enviada_notificacion", nullable = false)
    private Boolean enviada;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion", nullable = false, length = 30)
    private TipoNotificacion tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_documento",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_notificacion_documento")
    )
    @JsonBackReference
    private Documento documento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_notificacion_usuario")
    )
    @JsonBackReference
    private Usuario usuario;
}
