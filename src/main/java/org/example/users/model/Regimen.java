package org.example.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "regimen")
public class Regimen {
    @Id
    @Column(name = "regimen_id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    String regimen_id;
    @Column(name = "description")
    String description;
    @Column(name = "fisica")
    String fisica;
    @Column(name = "moral")
    String moral;
    @Column(name = "start_date")
    String start_date;
    @Column(name = "end_date")
    String end_date;

    public Regimen() {
    }

    public Regimen(String regimen_id, String descripcion, String fisica, String moral, String start_date, String end_date) {
        this.regimen_id = regimen_id;
        this.description = descripcion;
        this.fisica = fisica;
        this.moral = moral;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getRegimen_id() {
        return regimen_id;
    }

    public void setRegimen_id(String regimen_id) {
        this.regimen_id = regimen_id;
    }

    public String getDescripcion() {
        return description;
    }

    public void setDescripcion(String descripcion) {
        this.description = descripcion;
    }

    public String getFisica() {
        return fisica;
    }

    public void setFisica(String fisica) {
        this.fisica = fisica;
    }

    public String getMoral() {
        return moral;
    }

    public void setMoral(String moral) {
        this.moral = moral;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return "Regimen{" +
                "regimen_id='" + regimen_id + '\'' +
                ", descripcion='" + description + '\'' +
                ", fisica='" + fisica + '\'' +
                ", moral='" + moral + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                '}';
    }
}
