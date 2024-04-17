package org.example.users.model;

public class Balance {

    private String cuenta;
    private String nombre;
    private String deudor_inicial;
    private String acredor_inicial;
    private String debe;
    private String haber;
    private String deudor_final;
    private String acredor_final;


    public Balance() {
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDeudor_inicial() {
        return deudor_inicial;
    }

    public void setDeudor_inicial(String deudor_inicial) {
        this.deudor_inicial = deudor_inicial;
    }

    public String getAcredor_inicial() {
        return acredor_inicial;
    }

    public void setAcredor_inicial(String acredor_inicial) {
        this.acredor_inicial = acredor_inicial;
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

    public String getDeudor_final() {
        return deudor_final;
    }

    public void setDeudor_final(String deudor_final) {
        this.deudor_final = deudor_final;
    }

    public String getAcredor_final() {
        return acredor_final;
    }

    public void setAcredor_final(String acredor_final) {
        this.acredor_final = acredor_final;
    }


    @Override
    public String toString() {
        return "Balance{" +
                "cuenta='" + cuenta + '\'' +
                ", nombre='" + nombre + '\'' +
                ", deudor_inicial='" + deudor_inicial + '\'' +
                ", acredor_inicial='" + acredor_inicial + '\'' +
                ", debe='" + debe + '\'' +
                ", haber='" + haber + '\'' +
                ", deudor_final='" + deudor_final + '\'' +
                ", acredor_final='" + acredor_final + '\'' +
                '}';
    }
}
