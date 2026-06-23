package ni.edu.autotrack_apicore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.base.EntidadBase;
import ni.edu.autotrack_apicore.models.enums.Estado;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vehiculos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_vehiculo_placa", columnNames = "placa_vehiculo"),
        @UniqueConstraint(name = "uk_vehiculo_vin", columnNames = "vin_vehiculo")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(columnName = "eliminado", strategy = SoftDeleteType.ACTIVE)
public class Vehiculo extends EntidadBase {
    @Column(name = "marca_vehiculo", nullable = false, length = 50)
    private String marca;

    @Column(name = "modelo_vehiculo", nullable = false, length = 50)
    private String modelo;

    @Column(name = "anio_vehiculo", nullable = false)
    private Integer anio;

    @Column(name = "placa_vehiculo", nullable = false, length = 20)
    private String placa;

    @Column(name = "vin_vehiculo", nullable = false, length = 50)
    private String vin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_vehiculo", nullable = false, length = 30)
    private Estado estado;

    @ElementCollection
    @CollectionTable(
            name = "vehiculo_imagenes",
            joinColumns = @JoinColumn(name = "id_vehiculo")
    )
    @Column(name = "url_imagen")
    private Set<String> imagenes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehiculo_usuario")
    )
    @JsonBackReference
    private Usuario usuario;

    @OneToMany(
            mappedBy = "vehiculo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Registro> registros = new ArrayList<>();

    @OneToMany(
            mappedBy = "vehiculo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<DocumentoVehiculo> documentosVehiculo = new ArrayList<>();
}
