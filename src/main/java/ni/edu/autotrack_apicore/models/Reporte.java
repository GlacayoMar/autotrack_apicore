package ni.edu.autotrack_apicore.models;

import jakarta.persistence.Column;
import ni.edu.autotrack_apicore.models.base.EntidadBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reporte extends EntidadBase {
    @Column(name = "costos")
    private BigDecimal costo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "titulo")
    private String titulo;
}
