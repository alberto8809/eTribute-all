package org.example.users.service;

import org.example.users.model.*;
import org.example.users.repository.AuxiliarRepository;
import org.example.users.repository.CuentaContableRepository;
import org.example.users.util.CreateFilePDFBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AuxiliarService {

    @Autowired
    AuxiliarRepository auxiliarRepository;
    @Autowired
    CuentaContableRepository cuentaContableRepository;
    @Autowired
    JdbcClient jdbc;


    public Map<String, List<ListAuxiliar>> getListAccounts(String account, String inicial_date, String final_date) {
        Map<String, List<ListAuxiliar>> list = new HashMap<>();
        Map<String, ListAuxiliar> addingResponse = new HashMap<>();
        List<ListAuxiliar> response = new ArrayList<>();
        List<Auxiliar> list1 = auxiliarRepository.getAuxiliar(account, inicial_date, final_date);
        if (list1 != null) {
            for (Auxiliar aux : list1) {
                ListAuxiliar listAuxiliar = new ListAuxiliar();
                listAuxiliar.setConcepto(aux.getDescripcion());
                listAuxiliar.setCuenta(aux.getCuenta());
                addingResponse.put(aux.getCuenta(), listAuxiliar);

            }
        }

        for (Map.Entry<String, ListAuxiliar> re : addingResponse.entrySet()) {
            response.add(re.getValue());
        }

        list.put("cuentas", response);

        return list;
    }

    public Map<String, Object> getValuesOfTable(String cuenta, String initial, String final_date) {
        Map<String, Object> obj = new HashMap<>();
        List<Auxiliar> auxList = auxiliarRepository.valuesOfTable(cuenta, initial, final_date);
        HeaderAuxiliar headerAux = new HeaderAuxiliar();

        if (!auxList.isEmpty()) {

            headerAux.setCuenta(auxList.get(0).getCuenta());
            headerAux.setNombre(auxList.get(0).getDescripcion());
            headerAux.setSaldoInicial(headerAux.getSaldoInicial() == null ? "0" : auxiliarRepository.getSumInicial(cuenta));
            headerAux.setDebe(headerAux.getDebe() == null ? auxiliarRepository.getSumDebeByAccountId(cuenta) : headerAux.getDebe());
            headerAux.setHaber(headerAux.getHaber() == null ? auxiliarRepository.getSumHaberByAccountId(cuenta) : headerAux.getHaber());
            float sumaFinal = (Float.valueOf(headerAux.getSaldoInicial() + Float.valueOf(headerAux.getHaber())) - Float.valueOf(headerAux.getDebe()));
            headerAux.setSaldoFinal(headerAux.getSaldoFinal() == null ? auxiliarRepository.getSaldoFinal(cuenta) : String.valueOf(sumaFinal));

            obj.put("header", headerAux);
            obj.put("body", auxList);
            obj.put("url_pdf", "");
            //revisar el debe y haber
            return obj;
        } else {
            return null;
        }

    }

    public boolean createFileByAccount(String cuenta, String inicial, String final_) {
        List<Auxiliar> auxiliars = auxiliarRepository.valuesOfPDF(cuenta, inicial, final_);
        if (!auxiliars.isEmpty()) {
            CreateFilePDFBalance.makeFileAuxiliar(auxiliars);
            return true;
        }
        return false;
    }


    public Map<String, Object> getAllBalance(String account_id, String initial_date, String final_date) {
        List<Auxiliar> listAuxiliars = auxiliarRepository.getAuxiliar(account_id, initial_date, final_date);
        List<Object> values = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> obj2 = new HashMap<>();
        if (!listAuxiliars.isEmpty()) {
            for (Auxiliar listAuxiliar : listAuxiliars) {
                Balance balance = new Balance();
                balance.setCuenta(listAuxiliar.getCuenta());
                balance.setNombre(listAuxiliar.getDescripcion());
                balance.setDeudor_inicial(auxiliarRepository.getDeudor_inicial(listAuxiliar.getCuenta(), initial_date, final_date));
                balance.setAcredor_inicial(auxiliarRepository.getAcredor_final(listAuxiliar.getCuenta(), initial_date, final_date));
                balance.setDebe(auxiliarRepository.getSumCargo(listAuxiliar.getCuenta()));
                balance.setHaber(auxiliarRepository.getSumAbono(listAuxiliar.getCuenta()));
                float sum = Float.parseFloat(balance.getDeudor_inicial()) + Float.parseFloat(balance.getDebe());
                balance.setDeudor_final(String.valueOf(sum));
                float sum1 = Float.parseFloat(balance.getAcredor_inicial()) + Float.parseFloat(balance.getHaber());
                balance.setAcredor_final(String.valueOf(sum1));
                obj2.put(listAuxiliar.getCuenta(), balance);
            }

            float deudorInicial_total = 0;
            float acredorinical_total = 0;
            float debe_total = 0;
            float haber_total = 0;
            float deudorFinal_total = 0;
            float acredorFinal_total = 0;
            int i = 0;
            for (Map.Entry<String, Object> ob : obj2.entrySet()) {
                values.add(ob.getValue());
                Balance b = (Balance) values.get(i);
                deudorInicial_total += Float.parseFloat(b.getDebe());
                acredorinical_total += Float.parseFloat(b.getHaber());
                debe_total = Float.parseFloat(b.getDebe());
                haber_total = Float.parseFloat(b.getHaber());
                deudorFinal_total += deudorInicial_total + Float.parseFloat(b.getDebe());
                acredorFinal_total += acredorinical_total + Float.parseFloat(b.getHaber());
                i++;
            }


            BalanceTotal balanceTotal = new BalanceTotal();
            balanceTotal.setDeudorInicial_total(String.valueOf(deudorInicial_total));
            balanceTotal.setAcredorinical_total(String.valueOf(acredorinical_total));
            balanceTotal.setDebe_total(String.valueOf(debe_total));
            balanceTotal.setHaber_total(String.valueOf(haber_total));
            balanceTotal.setDeudorFinal_total(String.valueOf(deudorFinal_total));
            balanceTotal.setAcredorFinal_total(String.valueOf(acredorFinal_total));


            obj.put("balanza", values);
            obj.put("sumas", balanceTotal);
            obj.put("url_pdf", "");
            return obj;
        } else {
            return null;
        }

    }


    public Map<String, Object> getListBalanceDate(String account_id, String inicial_date, String final_date) {
        HashMap<String, Object> response = new HashMap<>();
        List<Object> generaBalanceResponses = new ArrayList<>();
        List<Map<String, Object>> obj = jdbc.sql("SELECT * FROM dbmaster.cuentas WHERE codigo_agrupador='101' OR codigo_agrupador ='102' OR codigo_agrupador ='103' OR codigo_agrupador ='105' OR codigo_agrupador ='107' OR codigo_agrupador ='110' OR codigo_agrupador ='113' OR codigo_agrupador ='114' OR codigo_agrupador ='118' OR codigo_agrupador ='119'")
                .query().listOfRows();

        double total_activoCirculante = 0;
        for (Map<String, Object> cuentaContable : obj) {
            GeneraBalanceResponse generaBalanceResponse = new GeneraBalanceResponse();
            generaBalanceResponse.setCuenta_id(cuentaContable.get("codigo_agrupador").toString());
            generaBalanceResponse.setDescripcion(cuentaContable.get("nombre_cuenta").toString());
            generaBalanceResponse.setTotal(0);
            total_activoCirculante += generaBalanceResponse.getTotal();
            generaBalanceResponses.add(generaBalanceResponse);
        }

        response.put("Activo_Circulante", generaBalanceResponses);


        List<Map<String, Object>> obj2 = jdbc.sql("SELECT *  FROM dbmaster.cuentas WHERE codigo_agrupador='201' OR codigo_agrupador ='205' OR codigo_agrupador ='208' OR codigo_agrupador ='209' OR codigo_agrupador ='210' OR codigo_agrupador ='213' OR codigo_agrupador ='216' OR codigo_agrupador ='218'")
                .query().listOfRows();

        List<Object> pasivo = new ArrayList<>();
        double total_pasivoCirculante = 0;
        for (Map<String, Object> cuentaContable : obj2) {
            GeneraBalanceResponse generaBalanceResponse = new GeneraBalanceResponse();
            generaBalanceResponse.setCuenta_id(cuentaContable.get("codigo_agrupador").toString());
            generaBalanceResponse.setDescripcion(cuentaContable.get("nombre_cuenta").toString());
            generaBalanceResponse.setTotal(0);
            total_pasivoCirculante += generaBalanceResponse.getTotal();
            pasivo.add(generaBalanceResponse);
        }
        response.put("Pasivo_Circulante", pasivo);

        List<CuentaContable> list2 = cuentaContableRepository.getValuestoBlanaceActivoFijo(account_id, inicial_date, final_date);
        List<Object> activo = new ArrayList<>();
        double total_activoFijo = 0;
        for (CuentaContable cuentaContable : list2) {
            GeneraBalanceResponse generaBalanceResponse = new GeneraBalanceResponse();
            generaBalanceResponse.setCuenta_id(cuentaContable.getCodigo_agrupador());
            generaBalanceResponse.setDescripcion(cuentaContable.getNombre_cuenta());
            generaBalanceResponse.setTotal(0);
            total_activoFijo += generaBalanceResponse.getTotal();
            activo.add(generaBalanceResponse);
        }

        response.put("Activo_Fijo", activo);


        List<CuentaContable> list3 = cuentaContableRepository.getValuesBalanceActivoDiferido(account_id, inicial_date, final_date);
        List<Object> activo_diferido = new ArrayList<>();
        double total_activoDiferido = 0;
        for (CuentaContable cuentaContable : list3) {
            GeneraBalanceResponse generaBalanceResponse = new GeneraBalanceResponse();
            generaBalanceResponse.setCuenta_id(cuentaContable.getCodigo_agrupador());
            generaBalanceResponse.setDescripcion(cuentaContable.getNombre_cuenta());
            generaBalanceResponse.setTotal(0);
            total_activoDiferido += generaBalanceResponse.getTotal();
            activo_diferido.add(generaBalanceResponse);
        }

        response.put("Activo_Diferido", activo_diferido);
        
        List<Map<String, Object>> obj3 = jdbc.sql("SELECT *  FROM dbmaster.cuentas WHERE codigo_agrupador='301' OR codigo_agrupador ='304' OR codigo_agrupador ='305'")
                .query().listOfRows();

        List<Object> capital = new ArrayList<>();
        double total_capital = 0;
        for (Map<String, Object> cuentaContable : obj3) {
            GeneraBalanceResponse generaBalanceResponse = new GeneraBalanceResponse();
            generaBalanceResponse.setCuenta_id(cuentaContable.get("codigo_agrupador").toString());
            generaBalanceResponse.setDescripcion(cuentaContable.get("nombre_cuenta").toString());
            generaBalanceResponse.setTotal(0);
            total_capital += generaBalanceResponse.getTotal();
            capital.add(generaBalanceResponse);
        }

        response.put("Capital_Contable", capital);


        Map<String, Double> totales = new HashMap<>();
        totales.put("Total_Activo_Circulante", total_activoCirculante);
        totales.put("Total_Pasivo_Circulante", total_pasivoCirculante);
        totales.put("Total_Activo_Fijo", total_activoFijo);
        //totales.put("Total_Pasivo_Largo_Plazo", getSum);
        totales.put("Total_Pasivo_Largo_Plazo", 0.0);
        totales.put("Total_Activo_Diferido", total_activoDiferido);
        totales.put("Total_Capital_Diferido", total_capital);
        totales.put("TOTAL_ACTIVO", total_activoCirculante + total_activoDiferido);
        totales.put("TOTAL_PASIVO_MAS_CAPITAL", total_pasivoCirculante + total_capital);
        response.put("Totales", totales);


        return response;
    }


    public Map<Object, Object> getValuesOfResults(String account_id, String initial, String final_) {

        Map<Object, Object> back = new HashMap<>();
        Map<Object, Object> obj = new HashMap<>();
        List<Object> list2 = new ArrayList<>();


        CuentaContable c = cuentaContableRepository.getValuesOf401(account_id, initial, final_);
        BalanceGeneral b = new BalanceGeneral();
        b.setNivel(c.getNivel());
        b.setCodigo_agrupador(c.getCodigo_agrupador());
        b.setNombre_cuenta(c.getNombre_cuenta());

        b.setImporte_anual(cuentaContableRepository.getValuesOf401Anual(initial, final_));
        b.setImporte_mensual(cuentaContableRepository.getImporte_mensual401(initial, final_));
        b.setPorcentaje_mensual(cuentaContableRepository.getPorcentaje_mensual401( initial, final_));
        b.setImporte_anual(cuentaContableRepository.getImporte_anual401( initial, final_));
        b.setPorcentaje_anual(cuentaContableRepository.getPorcentaje_anual401( initial, final_));


        CuentaContable d = cuentaContableRepository.getValuesOf501(account_id, initial, final_);
        BalanceGeneral b2 = new BalanceGeneral();
        b2.setNivel(d.getNivel());
        b2.setCodigo_agrupador(d.getCodigo_agrupador());
        b2.setNombre_cuenta(d.getNombre_cuenta());

        b2.setImporte_anual(cuentaContableRepository.getValuesOf401Anual(initial, final_));
        b2.setImporte_mensual(cuentaContableRepository.getImporte_mensual401(initial, final_));
        b2.setPorcentaje_mensual(cuentaContableRepository.getPorcentaje_mensual401(initial, final_));
        b2.setImporte_anual(cuentaContableRepository.getImporte_anual401(initial, final_));
        b2.setPorcentaje_anual(cuentaContableRepository.getPorcentaje_anual401( initial, final_));

        CuentaContable c3 = cuentaContableRepository.getValuesOf503(account_id, initial, final_);
        BalanceGeneral b3 = new BalanceGeneral();
        b3.setNivel(c3.getNivel());
        b3.setCodigo_agrupador(c3.getCodigo_agrupador());
        b3.setNombre_cuenta(c3.getNombre_cuenta());

        b3.setImporte_anual(cuentaContableRepository.getValuesOf401Anual(initial, final_));
        b3.setImporte_mensual(cuentaContableRepository.getImporte_mensual401( initial, final_));
        b3.setPorcentaje_mensual(cuentaContableRepository.getPorcentaje_mensual401( initial, final_));
        b3.setImporte_anual(cuentaContableRepository.getImporte_anual401( initial, final_));
        b3.setPorcentaje_anual(cuentaContableRepository.getPorcentaje_anual401(initial, final_));

        CuentaContable c4 = cuentaContableRepository.getValuesOf601(account_id, initial, final_);
        BalanceGeneral b4 = new BalanceGeneral();
        b4.setNivel(c4.getNivel());
        b4.setCodigo_agrupador(c4.getCodigo_agrupador());
        b4.setNombre_cuenta(c4.getNombre_cuenta());

        b4.setImporte_anual(cuentaContableRepository.getValuesOf401Anual( initial, final_));
        b4.setImporte_mensual(cuentaContableRepository.getImporte_mensual401( initial, final_));
        b4.setPorcentaje_mensual(cuentaContableRepository.getPorcentaje_mensual401( initial, final_));
        b4.setImporte_anual(cuentaContableRepository.getImporte_anual401( initial, final_));
        b4.setPorcentaje_anual(cuentaContableRepository.getPorcentaje_anual401(initial, final_));


        CuentaContable c5 = cuentaContableRepository.getValuesOf603(account_id, initial, final_);
        BalanceGeneral b5 = new BalanceGeneral();
        b5.setNivel(c5.getNivel());
        b5.setCodigo_agrupador(c5.getCodigo_agrupador());
        b5.setNombre_cuenta(c5.getNombre_cuenta());

        b5.setImporte_anual(cuentaContableRepository.getValuesOf401Anual(initial, final_));
        b5.setImporte_mensual(cuentaContableRepository.getImporte_mensual401(initial, final_));
        b5.setPorcentaje_mensual(cuentaContableRepository.getPorcentaje_mensual401( initial, final_));
        b5.setImporte_anual(cuentaContableRepository.getImporte_anual401( initial, final_));
        b5.setPorcentaje_anual(cuentaContableRepository.getPorcentaje_anual401( initial, final_));

        CuentaContable c6 = cuentaContableRepository.getValuesOf704(account_id, initial, final_);
        BalanceGeneral b6 = new BalanceGeneral();
        b6.setNivel(c6.getNivel());
        b6.setCodigo_agrupador(c6.getCodigo_agrupador());
        b6.setNombre_cuenta(c6.getNombre_cuenta());

        b6.setImporte_anual(cuentaContableRepository.getValuesOf401Anual(  initial, final_));
        b6.setImporte_mensual(cuentaContableRepository.getImporte_mensual401( initial, final_));
        b6.setPorcentaje_mensual(cuentaContableRepository.getPorcentaje_mensual401(  initial, final_));
        b6.setImporte_anual(cuentaContableRepository.getImporte_anual401( initial, final_));
        b6.setPorcentaje_anual(cuentaContableRepository.getPorcentaje_anual401( initial, final_));

        CuentaContable c7 = cuentaContableRepository.getValuesOf701(account_id, initial, final_);
        BalanceGeneral b7 = new BalanceGeneral();
        b7.setNivel(c7.getNivel());
        b7.setCodigo_agrupador(c7.getCodigo_agrupador());
        b7.setNombre_cuenta(c7.getNombre_cuenta());

        b7.setImporte_anual(cuentaContableRepository.getValuesOf401Anual(  initial, final_));
        b7.setImporte_mensual(cuentaContableRepository.getImporte_mensual401( initial, final_));
        b7.setPorcentaje_mensual(cuentaContableRepository.getPorcentaje_mensual401( initial, final_));
        b7.setImporte_anual(cuentaContableRepository.getImporte_anual401(  initial, final_));
        b7.setPorcentaje_anual(cuentaContableRepository.getPorcentaje_anual401(  initial, final_));

        CuentaContable c8 = cuentaContableRepository.getValuesOf304(account_id, initial, final_);
        BalanceGeneral b8 = new BalanceGeneral();
        b8.setNivel(c8.getNivel());
        b8.setCodigo_agrupador(c8.getCodigo_agrupador());
        b8.setNombre_cuenta(c8.getNombre_cuenta());

        b8.setImporte_anual(cuentaContableRepository.getValuesOf401Anual( initial, final_));
        b8.setImporte_mensual(cuentaContableRepository.getImporte_mensual401( initial, final_));
        b8.setPorcentaje_mensual(cuentaContableRepository.getPorcentaje_mensual401( initial, final_));
        b8.setImporte_anual(cuentaContableRepository.getImporte_anual401(  initial, final_));
        b8.setPorcentaje_anual(cuentaContableRepository.getPorcentaje_anual401( initial, final_));

        list2.add(b);
        list2.add(b2);
        list2.add(b3);
        list2.add(b4);
        list2.add(b5);
        list2.add(b6);
        list2.add(b7);
        list2.add(b8);


        obj.put("registro", list2);
        back.put("body", obj);
        back.put("url_pdf", "");
        return back;
    }
}