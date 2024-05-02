package org.example.users.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BalanceGeneral {
    private String importe_mensual;
    private String porcentaje_mensual;
    private String importe_anual;
    private String porcentaje_anual;

}
