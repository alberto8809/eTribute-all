package org.example.users.model;

public class GeneraBalanceResponse {
    private String cuenta_id;
    private String descripcion;

    private float total;

    public GeneraBalanceResponse() {
    }


    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(String cuenta_id) {
        this.cuenta_id = cuenta_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }


    @Override
    public String toString() {
        return "GeneraBalanceResponse{" +
                "cuenta_id='" + cuenta_id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", total=" + total +
                '}';
    }
}
