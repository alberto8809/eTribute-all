package org.example.users.controller;


import org.example.users.model.ListAuxiliar;
import org.example.users.service.AuxiliarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("auxiliarTable/{cuenta}/{inicial_date}/{final_date}")
    public ResponseEntity<Map<String, Object>> getValuesOfAccount(@PathVariable(name = "cuenta") String cuenta,@PathVariable(name = "inicial_date") String inicial_date,@PathVariable(name = "final_date") String final_date) {
        Map<String, Object> auxiliars = auxiliarService.getValuesOfTable(cuenta,inicial_date,final_date);
        if (!auxiliars.isEmpty()) {
            return new ResponseEntity<>(auxiliars, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("auxiliarFile/{cuenta}/{inicial_date}/{final_date}")
    public ResponseEntity<HttpStatus> createFile(@PathVariable(name = "cuenta") String cuenta, @PathVariable(name = "inicial_date") String inicial_date, @PathVariable(name = "final_date") String final_date) {
        if (auxiliarService.createFileByAccount(cuenta, inicial_date, final_date)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balance/{account_id}/{inicial_date}/{final_date}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable(name = "account_id") String account_id, @PathVariable(name = "inicial_date") String inicial_date, @PathVariable(name = "final_date") String final_date) {
        Map<String, Object> auxiliarList = auxiliarService.getAllBalance(account_id, inicial_date, final_date);
        if (!auxiliarList.isEmpty()) {
            return new ResponseEntity<>(auxiliarList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balanceGeneral/{inicial_date}/{final_date}")
    public ResponseEntity<Map<String, Object>> getListBalance(@PathVariable(name = "inicial_date") String inicial_date, @PathVariable(name = "final_date") String final_date) {
        Map<String, Object> bodyBalance = auxiliarService.getListBalanceDate(inicial_date, final_date);
        if (!bodyBalance.isEmpty()) {
            return new ResponseEntity<>(bodyBalance, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balanceGeneraEstados/{inicial_date}/{final_date}")
    public ResponseEntity<Map<String, Object>> getListValues(@PathVariable(name = "inicial_date") String inicial_date, @PathVariable(name = "final_date") String final_date) {
        Map<String, Object> bodyBalance = auxiliarService.getValuesOfResults(inicial_date, final_date);
        if (!bodyBalance.isEmpty()) {
            return new ResponseEntity<>(bodyBalance, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


}
