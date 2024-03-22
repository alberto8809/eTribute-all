package org.example.users.service;

import org.example.users.model.Auxiliar;
import org.example.users.model.Balance;
import org.example.users.model.ListAuxiliar;
import org.example.users.repository.AuxiliarRepository;
import org.example.users.util.CreateFilePDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AuxiliarService {

    @Autowired
    AuxiliarRepository auxiliarRepository;

    public List<ListAuxiliar> getListAccounts(String token) {
        return auxiliarRepository.getAuxiliar(token);
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


    public List<Balance> getAllBalance(String token) {
        List<ListAuxiliar> listAuxiliars = auxiliarRepository.getAuxiliar(token);
        List<Balance> values = new ArrayList<>();

        /*falta añadir inicial: deudor, acredor final: deudor, acredor*/
        for (ListAuxiliar listAuxiliar : listAuxiliars) {
            Balance balance = new Balance();
            balance.setCuenta(listAuxiliar.getCuenta());
            balance.setConcepto(listAuxiliar.getConcepto());
            balance.setCargo(auxiliarRepository.getSumCargo(listAuxiliar.getCuenta()));
            balance.setAbono(auxiliarRepository.getSumAbono(listAuxiliar.getCuenta()));

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


    public List<String> getListBalanceByDate(String token) {
        return auxiliarRepository.getDatesByUser(token);
    }

    public List<Auxiliar> getListBalanceDate(String inicial_date, String final_date) {
        return auxiliarRepository.getValuestoBlanace(inicial_date, final_date);
    }

}
