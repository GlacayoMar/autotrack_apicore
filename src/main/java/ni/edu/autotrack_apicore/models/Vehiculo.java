package ni.edu.autotrack_apicore.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.base.EntidadBase;
import ni.edu.autotrack_apicore.models.enums.Estado;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehiculos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_vehiculo_placa", columnNames = "placa_vehiculo"),
        @UniqueConstraint(name = "uk_vehiculo_vin", columnNames = "vin_vehiculo")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private List<String> imagenes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_vehiculo_usuario")
    )
    private Usuario usuario;

    @OneToMany(
            mappedBy = "vehiculo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Registro>  registros = new ArrayList<>();
}
