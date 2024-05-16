package org.example.users.model;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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

    public PolicyObjFile(PolicyObjParser policyObjParser, String nameFile, String companyName, String client, String date, String typeOf, String typeOfPayment) {
        this.policyObj = policyObjParser;
        this.nameFile = nameFile;
        this.companyName = companyName;
        this.client = client;
        this.date = date;
        this.typeOf = typeOf;
        this.typeOfPayment = typeOfPayment;
    }


}
