package org.example.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "policyObjFile")
public class Auxiliar {

    @Id
    @Column(name = "id_policy")
    private int id_policy;
    @Column(name = "cliente")
    private String cliente;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "poliza")
    private String poliza;
    @Column(name = "cuenta")
    private String cuenta;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "debe")
    private String debe;
    @Column(name = "haber")
    private String haber;
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "proveedor")
    private String proveedor;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "total")
    private String total;


    public Auxiliar() {
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    public String getPoliza() {
        return poliza;
    }

    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDebe() {
        return debe;
    }

    public void setDebe(String debe) {
        this.debe = debe;
    }

    public String getHaber() {
        return haber;
    }

    public void setHaber(String haber) {
        this.haber = haber;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }


    @Override
    public String toString() {
        return "Auxiliar{" +
                "id_policy=" + id_policy +
                ", cliente='" + cliente + '\'' +
                ", usuario='" + usuario + '\'' +
                ", tipo='" + tipo + '\'' +
                ", poliza='" + poliza + '\'' +
                ", cuenta='" + cuenta + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", debe='" + debe + '\'' +
                ", haber='" + haber + '\'' +
                ", fecha='" + fecha + '\'' +
                ", proveedor='" + proveedor + '\'' +
                ", referencia='" + referencia + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
