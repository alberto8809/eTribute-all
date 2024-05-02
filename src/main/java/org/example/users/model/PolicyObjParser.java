package org.example.users.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@Table(name = "PolicyObjParser")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PolicyObjParser {
    //    @Id
//    @Column(name = "regimen")
    private String regimen;
    private String usoCFDI;
    private List<String> ClaveProdServ;
    private String concepto_Descripcion;
    private String amoubnt;
    private List<String> traslado;
    private String impuestos;
    private List<String> retencion_importe;
    private String timbreFiscalDigital_UUID;
    private String totalAmount;
    private String type_of_value;
    private String venta_id;
    private String venta_descripcion;
    private String metodo;
    private List<String> cargo;
    private List<String> abono;
    private double subtotal;
    private Map<String, String> iva;
    private List<String> tax_amount;
    private String rfc;

}
