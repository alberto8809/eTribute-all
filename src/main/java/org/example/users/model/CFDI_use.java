package org.example.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "CFDI_use")
public class CFDI_use {
    @Id
    @Column(name = "CFDI_use_id")
    private String CFDI_use_id;
    @Column(name = "description")
    private String description;
    @Column(name = "person_type")
    private String person_type;
    @Column(name = "start_date")
    private String start_date;
    @Column(name = "end_date")
    private String end_date;
    @Column(name = "regimen")
    private String regimen;
    @Column(name = "person_type_")
    private String person_type_;

    public CFDI_use() {
    }

    public String getCFDI_use_id() {
        return CFDI_use_id;
    }

    public void setCFDI_use_id(String CFDI_use_id) {
        this.CFDI_use_id = CFDI_use_id;
    }

    public String getDesciption() {
        return description;
    }

    public void setDesciption(String desciption) {
        this.description = desciption;
    }

    public String getPerson_type() {
        return person_type;
    }

    public void setPerson_type(String person_type) {
        this.person_type = person_type;
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

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getPerson_type_() {
        return person_type_;
    }

    public void setPerson_type_(String person_type_) {
        this.person_type_ = person_type_;
    }

    @Override
    public String toString() {
        return "CFDI_use{" +
                "CFDI_use_id='" + CFDI_use_id + '\'' +
                ", description='" + description + '\'' +
                ", person_type='" + person_type + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", regimen='" + regimen + '\'' +
                ", person_type_='" + person_type_ + '\'' +
                '}';
    }
}
