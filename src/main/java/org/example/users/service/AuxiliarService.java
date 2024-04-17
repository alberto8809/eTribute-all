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

    public List<Object> getValuesOfTable(String cuenta) {
        List<Object> obj = new ArrayList<>();
        List<Auxiliar> auxList = auxiliarRepository.valuesOfTable(cuenta);
        HeaderAuxiliar headerAux = new HeaderAuxiliar();

        headerAux.setCuenta(auxList.get(0).getCuenta());
        headerAux.setNombre(auxList.get(0).getDescripcion());

        obj.add(headerAux);
        obj.add(auxList);
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


    public List<Object> getAllBalance(String account_id) {
        List<Auxiliar> listAuxiliars = auxiliarRepository.getAuxiliarById(account_id);
        List<Balance> values = new ArrayList<>();
        List<Object> obj = new ArrayList<>();

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

        obj.add(values);
        obj.add(balanceTotal);
        return obj;
    }


    public boolean createFileBalance(String token) {
        List<Object> balance = getAllBalance(token);
        if (!balance.isEmpty()) {
            CreateFilePDFBalance.makeFileBalance((List<Balance>) balance.get(1));
            return true;
        }
        return false;
    }

    public HashMap<String, List<CuentaContable>> getListBalanceDate(String inicial_date, String final_date) {
        HashMap<String, List<CuentaContable>> response = new HashMap<>();
        response.put("Activo Circulante", cuentaContableRepository.getValuestoBlanaceActivo());
        response.put("Total  Activo Circulante", new ArrayList<>());
        response.put("Pasivo Circulante", cuentaContableRepository.getValuestoBlanacePasivo());
        response.put("Total  Pasivo Circulante", new ArrayList<>());
        response.put("Activo Fijo", cuentaContableRepository.getValuestoBlanaceActivoFijo());
        response.put("Total  de Activo Fijo", new ArrayList<>());
        response.put("Pasivo a Largo PLazo", new ArrayList<>());
        response.put("Total  Pasivo a Largo PLazo", new ArrayList<>());
        response.put("Activo Diferido", cuentaContableRepository.getValuesBalanceActivoDiferido());
        response.put("Total Activo Diferido", new ArrayList<>());
        response.put("Capital Contable", cuentaContableRepository.getValuestoBlanaceCapital());
        response.put("Total Capital Contable", new ArrayList<>());
        response.put("TOTAL DE ACTIVO", new ArrayList<>());
        response.put("TOTAL PASIVO MÁS CAPITAL", new ArrayList<>());


        return response;
    }


    public List<CuentaContable> getValuesOfResults(String initial, String final_) {
        return  cuentaContableRepository.getValuesOfResults();
    }
}