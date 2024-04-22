package org.example.users.service;

import org.example.users.model.*;
import org.example.users.repository.AuxiliarRepository;
import org.example.users.repository.CuentaContableRepository;
import org.example.users.util.CreateFilePDFBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AuxiliarService {

    @Autowired
    AuxiliarRepository auxiliarRepository;
    @Autowired
    CuentaContableRepository cuentaContableRepository;


    public List<ListAuxiliar> getListAccounts(String account, String inicial_date, String final_date) {
        List<ListAuxiliar> list = new ArrayList<>();
        List<Auxiliar> list1 = auxiliarRepository.getAuxiliar(account, inicial_date, final_date);
        if (list1 != null) {
            for (Auxiliar aux : list1) {
                ListAuxiliar listAuxiliar = new ListAuxiliar();
                listAuxiliar.setConcepto(aux.getDescripcion());
                listAuxiliar.setCuenta(aux.getCuenta());
                list.add(listAuxiliar);
            }
        }

        return list.stream().distinct().collect(Collectors.toList());
    }

    public Map<String, Object> getValuesOfTable(String cuenta, String initial, String final_date) {

        Map<String, Object> obj = new HashMap<>();
        List<Auxiliar> auxList = auxiliarRepository.valuesOfTable(cuenta, initial, final_date);
        HeaderAuxiliar headerAux = new HeaderAuxiliar();

        headerAux.setCuenta(auxList.get(0).getCuenta());
        headerAux.setNombre(auxList.get(0).getDescripcion());
        headerAux.setSaldoInicial(headerAux.getSaldoInicial() == null ? "0" : auxiliarRepository.getSumInicial(cuenta));
        headerAux.setDebe(headerAux.getDebe() == null ? auxiliarRepository.getSumDebeByAccountId(cuenta) : "0");
        headerAux.setHaber(headerAux.getHaber() == null ? auxiliarRepository.getSumHaberByAccountId(cuenta) : "0");
        float sumaFinal = (Float.valueOf(headerAux.getSaldoInicial() + Float.valueOf(headerAux.getHaber())) - Float.valueOf(headerAux.getDebe()));
        headerAux.setSaldoFinal(headerAux.getSaldoFinal() == null ? auxiliarRepository.getSaldoFinal(cuenta) : String.valueOf(sumaFinal));

        obj.put("header", headerAux);
        obj.put("body", auxList);
        obj.put("url_pdf", "");

        return obj;
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
        List<Auxiliar> listAuxiliars = auxiliarRepository.getAuxiliarById(account_id);
        List<Balance> values = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();

        for (Auxiliar listAuxiliar : listAuxiliars) {
            Balance balance = new Balance();
            balance.setCuenta(listAuxiliar.getCuenta());
            balance.setNombre(listAuxiliar.getDescripcion());
            //balance.setDeudor_inicial();
            //balance.setAcredor_inicial();
            balance.setDebe(auxiliarRepository.getSumCargo(listAuxiliar.getCuenta()));
            balance.setHaber(auxiliarRepository.getSumAbono(listAuxiliar.getCuenta()));
            values.add(balance);
        }

        BalanceTotal balanceTotal = new BalanceTotal();
        balanceTotal.setDebe_total(auxiliarRepository.getSumDebeByAccountId(account_id));
        balanceTotal.setHaber_total(auxiliarRepository.getSumHaberByAccountId(account_id));

        obj.put("balanza", values);
        obj.put("sumas", balanceTotal);
        //obj.put("url_pdf", createFileBalance(account_id, initial_date, final_date));
        obj.put("url_pdf", "");
        return obj;
    }


    public String createFileBalance(String account_id, String initial_date, String final_date) {
        Map<String, Object> balance = getAllBalance(account_id, initial_date, final_date);
        String path = null;
        if (!balance.isEmpty()) {
            if (CreateFilePDFBalance.makeFileBalance((List<Balance>) balance.get(1))) {

                path = "";
                return path;
            }

        }
        return path;
    }

    public Map<String, Object> getListBalanceDate(String inicial_date, String final_date) {
        HashMap<String, Object> response = new HashMap<>();
        List<Object> generaBalanceResponses = new ArrayList<>();
        List<CuentaContable> list = cuentaContableRepository.getValuestoBlanaceActivo();
        double total_activoCirculante = 0;
        for (CuentaContable cuentaContable : list) {
            GeneraBalanceResponse generaBalanceResponse = new GeneraBalanceResponse();
            generaBalanceResponse.setCuenta_id(cuentaContable.getCodigo_agrupador());
            generaBalanceResponse.setDescripcion(cuentaContable.getNombre_cuenta());
            generaBalanceResponse.setTotal(0);
            total_activoCirculante += generaBalanceResponse.getTotal();
            generaBalanceResponses.add(generaBalanceResponse);
        }

        response.put("Activo_Circulante", generaBalanceResponses);


        List<CuentaContable> list1 = cuentaContableRepository.getValuestoBlanacePasivo();
        List<Object> pasivo = new ArrayList<>();
        double total_pasivoCirculante = 0;
        for (CuentaContable cuentaContable : list1) {
            GeneraBalanceResponse generaBalanceResponse = new GeneraBalanceResponse();
            generaBalanceResponse.setCuenta_id(cuentaContable.getCodigo_agrupador());
            generaBalanceResponse.setDescripcion(cuentaContable.getNombre_cuenta());
            generaBalanceResponse.setTotal(0);
            total_pasivoCirculante += generaBalanceResponse.getTotal();
            pasivo.add(generaBalanceResponse);
        }
        response.put("Pasivo_Circulante", pasivo);

        List<CuentaContable> list2 = cuentaContableRepository.getValuestoBlanaceActivoFijo();
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


        List<CuentaContable> list3 = cuentaContableRepository.getValuesBalanceActivoDiferido();
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


        List<CuentaContable> list4 = cuentaContableRepository.getValuestoBlanaceCapital();
        List<Object> capital = new ArrayList<>();
        double total_capital = 0;
        for (CuentaContable cuentaContable : list4) {
            GeneraBalanceResponse generaBalanceResponse = new GeneraBalanceResponse();
            generaBalanceResponse.setCuenta_id(cuentaContable.getCodigo_agrupador());
            generaBalanceResponse.setDescripcion(cuentaContable.getNombre_cuenta());
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


    public Map<String, Object> getValuesOfResults(String initial, String final_) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("body", cuentaContableRepository.getValuesOfResults());
        //obj.put("url_pdf", createFileBalance(account_id, initial_date, final_date));
        obj.put("url_pdf", "");
        return obj;
    }
}