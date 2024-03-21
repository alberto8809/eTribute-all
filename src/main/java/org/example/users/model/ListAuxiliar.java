package org.example.users.model;

public class ListAuxiliar {

    private String cuenta;
    private String concepto;

    public ListAuxiliar() {
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

    @Override
    public String toString() {
        return "ListAuxiliar{" +
                "cuenta='" + cuenta + '\'' +
                ", concepto='" + concepto + '\'' +
                '}';
    }
}
