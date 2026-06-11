package ni.edu.autotrack_apicore.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "registros_combustible")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroCombustible extends Registro {

    @Column(name = "cantidad_combustible", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadCombustible;

    @Column(name = "cantidad_pagado", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadPagado;

    @Column(name = "odometro", nullable = false)
    private Long odometro;
}
