package ni.edu.autotrack_apicore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.base.EntidadBase;
import ni.edu.autotrack_apicore.models.enums.TipoMantenimiento;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servicios_mantenimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(columnName = "eliminado", strategy = SoftDeleteType.DELETED)
public class ServicioMantenimiento extends EntidadBase {

    @Column(name = "titulo_servicio", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descripcion_servicio", length = 250)
    private String descripcion;

    @Column(name = "afecta_vehiculo", nullable = false)
    private Boolean afectaVehiculo;

    @Column(name = "completado", nullable = false)
    private Boolean completado = false;

    @Column(name = "distancia_agendada")
    private Integer distanciaAgendada; // Kilometraje para el próximo servicio

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_agendada")
    private LocalDateTime fechaAgendada;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mantenimiento", nullable = false, length = 50)
    private TipoMantenimiento tipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    @JsonBackReference
    private Vehiculo vehiculo;

    @OneToMany(
            mappedBy = "servicioMantenimiento",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Notificacion> notificaciones = new ArrayList<>();
}
