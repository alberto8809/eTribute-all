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

    private String deudorInicial_total;
    private String acredorinical_total;

    private String debe_total;
    private String haber_total;
    private String deudorFinal_total;
    private String acredorFinal_total;

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

    public String getDeudorInicial_total() {
        return deudorInicial_total;
    }

    public void setDeudorInicial_total(String deudorInicial_total) {
        this.deudorInicial_total = deudorInicial_total;
    }

    public String getAcredorinical_total() {
        return acredorinical_total;
    }

    public void setAcredorinical_total(String acredorinical_total) {
        this.acredorinical_total = acredorinical_total;
    }

    public String getDebe_total() {
        return debe_total;
    }

    public void setDebe_total(String debe_total) {
        this.debe_total = debe_total;
    }

    public String getHaber_total() {
        return haber_total;
    }

    public void setHaber_total(String haber_total) {
        this.haber_total = haber_total;
    }

    public String getDeudorFinal_total() {
        return deudorFinal_total;
    }

    public void setDeudorFinal_total(String deudorFinal_total) {
        this.deudorFinal_total = deudorFinal_total;
    }

    public String getAcredorFinal_total() {
        return acredorFinal_total;
    }

    public void setAcredorFinal_total(String acredorFinal_total) {
        this.acredorFinal_total = acredorFinal_total;
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
                ", deudorInicial_total='" + deudorInicial_total + '\'' +
                ", acredorinical_total='" + acredorinical_total + '\'' +
                ", debe_total='" + debe_total + '\'' +
                ", haber_total='" + haber_total + '\'' +
                ", deudorFinal_total='" + deudorFinal_total + '\'' +
                ", acredorFinal_total='" + acredorFinal_total + '\'' +
                '}';
    }
}
