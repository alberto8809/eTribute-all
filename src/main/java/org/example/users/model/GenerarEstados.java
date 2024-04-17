package org.example.users.model;

public class GenerarEstados {

    private String concepto;
    private String importe;
    private String porcentaje_inicial;
    private String importe_final;
    private String  porcentaje_final;

    public GenerarEstados() {
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getPorcentaje_inicial() {
        return porcentaje_inicial;
    }

    public void setPorcentaje_inicial(String porcentaje_inicial) {
        this.porcentaje_inicial = porcentaje_inicial;
    }

    public String getImporte_final() {
        return importe_final;
    }

    public void setImporte_final(String importe_final) {
        this.importe_final = importe_final;
    }

    public String getPorcentaje_final() {
        return porcentaje_final;
    }

    public void setPorcentaje_final(String porcentaje_final) {
        this.porcentaje_final = porcentaje_final;
    }


    @Override
    public String toString() {
        return "GenerarEstados{" +
                "concepto='" + concepto + '\'' +
                ", importe='" + importe + '\'' +
                ", porcentaje_inicial='" + porcentaje_inicial + '\'' +
                ", importe_final='" + importe_final + '\'' +
                ", porcentaje_final='" + porcentaje_final + '\'' +
                '}';
    }
}
