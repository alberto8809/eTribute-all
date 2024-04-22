package org.example.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "policy")
public class PolicytoDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id_policy;

    private String cliente;

    private String usuario;


    private String tipo;

    private String poliza;

    private double cuenta;

    private String descripcion;

    private String debe;

    private String haber;

    private String fecha;

    private String proveedor;

    private String referencia;

    private String total;

    private String saldo_inicial;

    private String saldo_final;

    private int account_id;

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

    public String getPoliza() {
        return poliza;
    }

    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }

    public double getCuenta() {
        return cuenta;
    }

    public void setCuenta(double cuenta) {
        this.cuenta = cuenta;
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

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSaldo_inicial() {
        return saldo_inicial;
    }

    public void setSaldo_inicial(String saldo_inicial) {
        this.saldo_inicial = saldo_inicial;
    }

    public String getSaldo_final() {
        return saldo_final;
    }

    public void setSaldo_final(String saldo_final) {
        this.saldo_final = saldo_final;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    @Override
    public String toString() {
        return "PolicytoDB{" +
                "id_policy='" + id_policy + '\'' +
                ", cliente='" + cliente + '\'' +
                ", usuario='" + usuario + '\'' +
                ", tipo='" + tipo + '\'' +
                ", poliza='" + poliza + '\'' +
                ", cuenta=" + cuenta +
                ", descripcion='" + descripcion + '\'' +
                ", debe='" + debe + '\'' +
                ", haber='" + haber + '\'' +
                ", fecha='" + fecha + '\'' +
                ", proveedor='" + proveedor + '\'' +
                ", referencia='" + referencia + '\'' +
                ", total='" + total + '\'' +
                ", saldo_inicial='" + saldo_inicial + '\'' +
                ", saldo_final='" + saldo_final + '\'' +
                ", account_id=" + account_id +
                '}';
    }
}
