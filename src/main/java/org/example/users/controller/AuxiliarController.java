package org.example.users.controller;


import org.example.users.model.CuentaContable;
import org.example.users.model.ListAuxiliar;
import org.example.users.service.AuxiliarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/")
public class AuxiliarController {
    @Autowired
    AuxiliarService auxiliarService;


    public AuxiliarController(AuxiliarService auxiliarService) {
        this.auxiliarService = auxiliarService;
    }

    /*get all accounts to fill dropdown - Generar Auxiliar  */
    @GetMapping(path = "auxiliar/{account}/{inicial_date}/{final_date}")
    public ResponseEntity<List<ListAuxiliar>> getAuxiliar(@PathVariable(name = "account") String account, @PathVariable(name = "inicial_date") String inicial_date, @PathVariable(name = "final_date") String final_date) {
        List<ListAuxiliar> auxiliars = auxiliarService.getListAccounts(account, inicial_date, final_date);
        if (!auxiliars.isEmpty()) {
            return new ResponseEntity<>(auxiliars, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /* get all the values to fill the table - Generar Auxiliar*/
    @GetMapping("auxiliarTable/{cuenta}")
    public ResponseEntity<Map<String, Object>> getValuesOfAccount(@PathVariable(name = "cuenta") String cuenta) {
        Map<String, Object> auxiliars = auxiliarService.getValuesOfTable(cuenta);
        if (!auxiliars.isEmpty()) {
            return new ResponseEntity<>(auxiliars, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("auxiliarFile/{cuenta}{inicial_date}/{final_date}")
    public ResponseEntity<HttpStatus> createFile(@PathVariable(name = "cuenta") String cuenta, @PathVariable(name = "inicial_date") String inicial_date, @PathVariable(name = "final_date") String final_date) {
        if (auxiliarService.createFileByAccount(cuenta, inicial_date, final_date)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balance/{account_id}")
    public ResponseEntity<List<Object>> getBalance(@PathVariable(name = "account_id") String account_id) {
        List<Object> auxiliarList = auxiliarService.getAllBalance(account_id);
        if (!auxiliarList.isEmpty()) {
            return new ResponseEntity<>(auxiliarList, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balanceFile/{token}")
    public ResponseEntity<HttpStatus> createFileBalance(@PathVariable(name = "token") String token) {
        if (auxiliarService.createFileBalance(token)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balanceGeneral/{inicial_date}/{final_date}")
    public ResponseEntity<HashMap<String, List<CuentaContable>>> getListBalance(@PathVariable(name = "inicial_date") String inicial_date, @PathVariable(name = "final_date") String final_date) {
        HashMap<String, List<CuentaContable>> bodyBalance = auxiliarService.getListBalanceDate(inicial_date, final_date);
        if (!bodyBalance.isEmpty()) {
            return new ResponseEntity<>(bodyBalance, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balanceGeneraEstados/{inicial_date}/{final_date}")
    public ResponseEntity<List<CuentaContable>> getListValues(@PathVariable(name = "inicial_date") String inicial_date, @PathVariable(name = "final_date") String final_date) {
        List<CuentaContable> bodyBalance = auxiliarService.getValuesOfResults(inicial_date, final_date);
        if (!bodyBalance.isEmpty()) {
            return new ResponseEntity<>(bodyBalance, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


}
