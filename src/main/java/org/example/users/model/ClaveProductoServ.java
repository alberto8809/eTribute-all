package org.example.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "claveProdServ")
public class ClaveProductoServ {
    @Id
    @Column(name = "c_claveprodserv")
    String c_claveprodserv;

    @Column(name = "descripcion")
    String descripcion;

    @Column(name = "c_contable")
    String c_contable;

    public ClaveProductoServ() {
    }

    public ClaveProductoServ(String c_ClaveProdServ, String descripcion, String cuenta_contable) {
        this.c_claveprodserv = c_ClaveProdServ;
        this.descripcion = descripcion;
        this.c_contable = cuenta_contable;
    }

    public String getC_ClaveProdServ() {
        return c_claveprodserv;
    }

    public void setC_ClaveProdServ(String c_ClaveProdServ) {
        this.c_claveprodserv = c_ClaveProdServ;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCuenta_contable() {
        return c_contable;
    }

    public void setCuenta_contable(String cuenta_contable) {
        this.c_contable = cuenta_contable;
    }

    @Override
    public String toString() {
        return "ClaveProductoServ{" +
                "c_ClaveProdServ='" + c_claveprodserv + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cuenta_contable='" + c_contable + '\'' +
                '}';
    }
}
