package org.example.users.model;

public class HeaderAuxiliar {

    private String cuenta;
    private String nombre;
    private String saldoInicial;
    private String debe;
    private String haber;
    private String saldoFinal;

    public HeaderAuxiliar() {
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

    public String getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(String saldoInicial) {
        this.saldoInicial = saldoInicial;
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

    public String getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(String saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    @Override
    public String toString() {
        return "HeaderAuxiliar{" +
                "cuenta='" + cuenta + '\'' +
                ", nombre='" + nombre + '\'' +
                ", saldoInicial='" + saldoInicial + '\'' +
                ", debe='" + debe + '\'' +
                ", haber='" + haber + '\'' +
                ", saldoFinal='" + saldoFinal + '\'' +
                '}';
    }
}
