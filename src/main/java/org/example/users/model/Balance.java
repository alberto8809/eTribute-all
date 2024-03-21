package org.example.users.model;

public class Balance {

    private String cuenta;
    private String concepto;
    private String deudor_inicial;
    private String acredor_inicial;
    private String cargo;
    private String abono;
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

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
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
                ", concepto='" + concepto + '\'' +
                ", deudor_inicial='" + deudor_inicial + '\'' +
                ", acredor_inicial='" + acredor_inicial + '\'' +
                ", cargo='" + cargo + '\'' +
                ", abono='" + abono + '\'' +
                ", deudor_final='" + deudor_final + '\'' +
                ", acredor_final='" + acredor_final + '\'' +
                '}';
    }
}
