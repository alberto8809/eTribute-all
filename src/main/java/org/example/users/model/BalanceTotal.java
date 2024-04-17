package org.example.users.model;

public class BalanceTotal {
    private String deudorInicial_total;
    private String acredorinical_total;

    private String debe_total;
    private String haber_total;
    private String deudorFinal_total;
    private String acredorFinal_total;

    public BalanceTotal() {
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
        return "BalanceTotal{" +
                "deudorInicial_total='" + deudorInicial_total + '\'' +
                ", acredorinical_total='" + acredorinical_total + '\'' +
                ", debe_total='" + debe_total + '\'' +
                ", haber_total='" + haber_total + '\'' +
                ", deudorFinal_total='" + deudorFinal_total + '\'' +
                ", acredorFinal_total='" + acredorFinal_total + '\'' +
                '}';
    }
}
