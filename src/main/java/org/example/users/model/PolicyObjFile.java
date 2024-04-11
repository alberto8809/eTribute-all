package org.example.users.model;


import java.util.List;


public class PolicyObjFile {

    private String id_policy;

    private PolicyObjParser policyObj;

    private String nameFile;
    private String companyName;

    private String client;
    private String date;
    private String typeOf;

    private String typeOfPayment;

    private String cuenta_method;

    private String description_methods;


    private List<String> tax_id;

    private List<String> tax_description;
    private String folio;


    public PolicyObjFile() {
    }

    public PolicyObjFile(PolicyObjParser policyObjParser, String nameFile, String companyName, String client, String date, String typeOf, String typeOfPayment) {
        this.policyObj = policyObjParser;
        this.nameFile = nameFile;
        this.companyName = companyName;
        this.client = client;
        this.date = date;
        this.typeOf = typeOf;
        this.typeOfPayment = typeOfPayment;
    }


    public PolicyObjFile(PolicyObjParser policyObjParser, String nameFile, String companyName, String client, String date, String typeOf, String typeOfPayment, String cuenta_method, String description_methods, List<String> tax_id, List<String> tax_description, String folio) {
        this.policyObj = policyObjParser;
        this.nameFile = nameFile;
        this.companyName = companyName;
        this.client = client;
        this.date = date;
        this.typeOf = typeOf;
        this.typeOfPayment = typeOfPayment;
        this.cuenta_method = cuenta_method;
        this.description_methods = description_methods;
        this.tax_id = tax_id;
        this.tax_description = tax_description;
        this.folio = folio;
    }

    public PolicyObjParser getPolicyObj() {
        return policyObj;
    }

    public void setPolicyObj(PolicyObjParser policyObjParser) {
        this.policyObj = policyObjParser;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(String typeOf) {
        this.typeOf = typeOf;
    }

    public String getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(String typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

    public String getCuenta_method() {
        return cuenta_method;
    }

    public void setCuenta_method(String cuenta_method) {
        this.cuenta_method = cuenta_method;
    }

    public String getDescription_methods() {
        return description_methods;
    }

    public void setDescription_methods(String description_methods) {
        this.description_methods = description_methods;
    }

    public List<String> getTax_id() {
        return tax_id;
    }

    public void setTax_id(List<String> tax_id) {
        this.tax_id = tax_id;
    }

    public List<String> getTax_description() {
        return tax_description;
    }

    public void setTax_description(List<String> tax_description) {
        this.tax_description = tax_description;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    @Override
    public String toString() {
        return "ObjFile{" +
                "policyObj=" + policyObj +
                ", nameFile='" + nameFile + '\'' +
                ", companyName='" + companyName + '\'' +
                ", client='" + client + '\'' +
                ", date='" + date + '\'' +
                ", typeOf='" + typeOf + '\'' +
                ", typeOfPayment='" + typeOfPayment + '\'' +
                ", cuenta_method='" + cuenta_method + '\'' +
                ", description_methods='" + description_methods + '\'' +
                ", tax_id='" + tax_id + '\'' +
                ", tax_description='" + tax_description + '\'' +
                ", folio=" + folio +
                '}';
    }
}
