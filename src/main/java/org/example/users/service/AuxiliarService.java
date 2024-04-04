package org.example.users.service;

import org.example.users.model.Auxiliar;
import org.example.users.model.Balance;
import org.example.users.model.ListAuxiliar;
import org.example.users.repository.AuxiliarRepository;
import org.example.users.repository.CuentaContableRepository;
import org.example.users.util.CreateFilePDF;
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

    public List<Auxiliar> getValuesofTable(String cuenta) {
        return auxiliarRepository.valuesOfTable(cuenta);
    }

    public boolean createFileByAccount(String cuenta) {
        List<Auxiliar> auxiliars = auxiliarRepository.valuesOfTable(cuenta);
        if (!auxiliars.isEmpty()) {
            CreateFilePDF.makeFileAuxiliar(auxiliars);
            return true;
        }
        return false;
    }


    public List<Balance> getAllBalance(String account_id) {
        List<Auxiliar> listAuxiliars = auxiliarRepository.getAuxiliarById(account_id);
        List<Balance> values = new ArrayList<>();

        for (Auxiliar listAuxiliar : listAuxiliars) {
            Balance balance = new Balance();
            balance.setCuenta(listAuxiliar.getCuenta());
            balance.setNombre(listAuxiliar.getDescripcion());
            //balance.setDeudor_inicial();
            //balance.setAcredor_inicial();
            balance.setDebe(auxiliarRepository.getSumCargo(listAuxiliar.getCuenta()));
            balance.setHaber(auxiliarRepository.getSumAbono(listAuxiliar.getCuenta()));
            //balance.setDeudorInicial_total();
            //balance.setAcredorinical_total();
            //balance.setDeudorInicial_total();
            //balance.setAcredorinical_total();
            balance.setDebe_total(auxiliarRepository.getSumDebeByAccountId(account_id));
            balance.setHaber_total(auxiliarRepository.getSumHaberByAccountId(account_id));
            //balance.setDeudorFinal_total();
            //balance.setAcredorFinal_total();

            values.add(balance);
        }

        return values;
    }


    public boolean createFileBalance(String token) {
        List<Balance> balance = getAllBalance(token);
        if (!balance.isEmpty()) {
            CreateFilePDF.makeFileBalance(balance);
            return true;
        }
        return false;
    }

    public HashMap<String, Object> getListBalanceDate(String inicial_date, String final_date) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("Activo Circulante", cuentaContableRepository.getValuestoBlanaceActivo());
        response.put("Pasivo Circulante", cuentaContableRepository.getValuestoBlanacePasivo());
        response.put("Activo Fijo", cuentaContableRepository.getValuestoBlanaceActivoFijo());
        response.put("Pasivo a Largo PLazo", null);
        response.put("Activo Diferido", cuentaContableRepository.getValuesBalanceActivoDiferido());
        response.put("Capital Contable", cuentaContableRepository.getValuestoBlanaceCapital());
        return response;
    }
}