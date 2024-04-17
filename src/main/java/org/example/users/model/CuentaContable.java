package org.example.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "cuentas")
public class CuentaContable {

    @Id
    @Column(name = "nivel")
    private String nivel;
    @Column(name = "codigo_agrupador")
    private String codigo_agrupador;
    @Column(name = "nombre_cuenta")
    private String nombre_cuenta;


    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getCodigo_agrupador() {
        return codigo_agrupador;
    }

    public void setCodigo_agrupador(String codigo_agrupador) {
        this.codigo_agrupador = codigo_agrupador;
    }

    public String getNombre_cuenta() {
        return nombre_cuenta;
    }

    public void setNombre_cuenta(String nombre_cuenta) {
        this.nombre_cuenta = nombre_cuenta;
    }

    @Override
    public String toString() {
        return "{ codigo_agrupador='" + codigo_agrupador + '\'' +
                ", nombre_cuenta='" + nombre_cuenta + '\'' +
                '}';
    }
}
