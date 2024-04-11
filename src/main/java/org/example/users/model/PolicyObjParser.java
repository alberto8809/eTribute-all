package org.example.users.model;

import java.util.List;
import java.util.Map;


//@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@Table(name = "PolicyObjParser")
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



    public PolicyObjParser() {
    }

    public PolicyObjParser(String regimen, String usoCFDI, List<String> claveProdServ, String concepto_Descripcion, String amoubnt, List<String> traslado, String impuestos, List<String> retencion_importe, String timbreFiscalDigital_UUID, String totalAmount) {
        this.regimen = regimen;
        this.usoCFDI = usoCFDI;
        ClaveProdServ = claveProdServ;
        this.concepto_Descripcion = concepto_Descripcion;
        this.amoubnt = amoubnt;
        this.traslado = traslado;
        this.impuestos = impuestos;
        this.retencion_importe = retencion_importe;
        this.timbreFiscalDigital_UUID = timbreFiscalDigital_UUID;
        this.totalAmount = totalAmount;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getUsoCFDI() {
        return usoCFDI;
    }

    public void setUsoCFDI(String usoCFDI) {
        this.usoCFDI = usoCFDI;
    }

    public List<String>  getClaveProdServ() {
        return ClaveProdServ;
    }

    public void setClaveProdServ(List<String> claveProdServ) {
        ClaveProdServ = claveProdServ;
    }

    public String getConcepto_Descripcion() {
        return concepto_Descripcion;
    }

    public void setConcepto_Descripcion(String concepto_Descripcion) {
        this.concepto_Descripcion = concepto_Descripcion;
    }

    public String getAmoubnt() {
        return amoubnt;
    }

    public void setAmoubnt(String amoubnt) {
        this.amoubnt = amoubnt;
    }

    public List<String> getTraslado() {
        return traslado;
    }

    public void setTraslado(List<String> traslado) {
        this.traslado = traslado;
    }

    public String getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(String impuestos) {
        this.impuestos = impuestos;
    }

    public List<String> getRetencion_importe() {
        return retencion_importe;
    }

    public void setRetencion_importe(List<String> retencion_importe) {
        this.retencion_importe = retencion_importe;
    }

    public String getTimbreFiscalDigital_UUID() {
        return timbreFiscalDigital_UUID;
    }

    public void setTimbreFiscalDigital_UUID(String timbreFiscalDigital_UUID) {
        this.timbreFiscalDigital_UUID = timbreFiscalDigital_UUID;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getType_of_value() {
        return type_of_value;
    }

    public void setType_of_value(String type_of_value) {
        this.type_of_value = type_of_value;
    }

    public String getVenta_id() {
        return venta_id;
    }

    public void setVenta_id(String venta_id) {
        this.venta_id = venta_id;
    }

    public String getVenta_descripcion() {
        return venta_descripcion;
    }

    public void setVenta_descripcion(String venta_descripcion) {
        this.venta_descripcion = venta_descripcion;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }


    public List<String> getCargo() {
        return cargo;
    }

    public void setCargo(List<String> cargo) {
        this.cargo = cargo;
    }

    public List<String> getAbono() {
        return abono;
    }

    public void setAbono(List<String> abono) {
        this.abono = abono;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Map<String, String> getIva() {
        return iva;
    }

    public void setIva(Map<String, String> iva) {
        this.iva = iva;
    }

    public List<String> getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(List<String> tax_amount) {
        this.tax_amount = tax_amount;
    }


    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @Override
    public String toString() {
        return "PolicyObjParser{" +
                "regimen='" + regimen + '\'' +
                ", usoCFDI='" + usoCFDI + '\'' +
                ", ClaveProdServ=" + ClaveProdServ +
                ", concepto_Descripcion='" + concepto_Descripcion + '\'' +
                ", amoubnt='" + amoubnt + '\'' +
                ", traslado=" + traslado +
                ", impuestos='" + impuestos + '\'' +
                ", retencion_importe=" + retencion_importe +
                ", timbreFiscalDigital_UUID='" + timbreFiscalDigital_UUID + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", type_of_value='" + type_of_value + '\'' +
                ", venta_id='" + venta_id + '\'' +
                ", venta_descripcion='" + venta_descripcion + '\'' +
                ", metodo='" + metodo + '\'' +
                ", cargo=" + cargo +
                ", abono=" + abono +
                ", subtotal=" + subtotal +
                ", iva=" + iva +
                ", tax_amount=" + tax_amount +
                ", rfc='" + rfc + '\'' +
                '}';
    }
}
