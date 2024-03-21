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
    @Column(name = "folio")
    private String folio;
    @Column(name = "cuenta")
    private String cuenta;
    @Column(name = "concepto")
    private String concepto;
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "abono")
    private String abono;
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "proveedor")
    private String proveedor;
    @Column(name = "folio_fiscal")
    private String folio_fiscal;
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

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getAbono() {
        return abono;
    }

    public void setAbono(String abono) {
        this.abono = abono;
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

    public String getFolio_fiscal() {
        return folio_fiscal;
    }

    public void setFolio_fiscal(String folio_fiscal) {
        this.folio_fiscal = folio_fiscal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Auxiliar{" +
                "id_policy=" + id_policy +
                ", cliente='" + cliente + '\'' +
                ", usuario='" + usuario + '\'' +
                ", tipo='" + tipo + '\'' +
                ", folio='" + folio + '\'' +
                ", cuenta='" + cuenta + '\'' +
                ", concepto='" + concepto + '\'' +
                ", cargo='" + cargo + '\'' +
                ", abono='" + abono + '\'' +
                ", fecha='" + fecha + '\'' +
                ", proveedor='" + proveedor + '\'' +
                ", folio_fiscal='" + folio_fiscal + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
