package org.example.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "")
public class PolicytoDB {
    @Id
    @Column(name = "CFDI_use_id")
    private String CFDI_use_id;

    public PolicytoDB() {
    }

    public String getCFDI_use_id() {
        return CFDI_use_id;
    }

    public void setCFDI_use_id(String CFDI_use_id) {
        this.CFDI_use_id = CFDI_use_id;
    }


    @Override
    public String toString() {
        return "PolicytoDB{" +
                "CFDI_use_id='" + CFDI_use_id + '\'' +
                '}';
    }
}
