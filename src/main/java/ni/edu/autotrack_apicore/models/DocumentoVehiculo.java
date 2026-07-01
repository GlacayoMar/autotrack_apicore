package ni.edu.autotrack_apicore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "documentos_vehiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoVehiculo extends Documento{

    @Column(name = "nombre_documento", nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id_vehiculo",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_documentovh_vehiculo")
    )
    @JsonBackReference
    private Vehiculo vehiculo;
}
