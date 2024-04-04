package org.example.users.controller;


import org.example.users.model.Auxiliar;
import org.example.users.model.Balance;
import org.example.users.model.ListAuxiliar;
import org.example.users.service.AuxiliarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


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
    @GetMapping(path = "auxiliar/{account}")
    public ResponseEntity<List<ListAuxiliar>> getAuxiliar(@PathVariable(name = "account") String account) {
        List<ListAuxiliar> auxiliars = auxiliarService.getListAccounts(account);
        if (!auxiliars.isEmpty()) {
            return new ResponseEntity<>(auxiliars, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /* get all the values to fill the table - Generar Auxiliar*/
    @GetMapping("auxiliarTable/{cuenta}")
    public ResponseEntity<List<Auxiliar>> getValuesoAccount(@PathVariable(name = "cuenta") String cuenta) {
        List<Auxiliar> auxiliars = auxiliarService.getValuesofTable(cuenta);
        if (!auxiliars.isEmpty()) {
            return new ResponseEntity<>(auxiliars, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("auxiliarFile/{cuenta}")
    public ResponseEntity<HttpStatus> createFile(@PathVariable(name = "cuenta") String cuenta) {
        if (auxiliarService.createFileByAccount(cuenta)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balance/{cuenta}")
    public ResponseEntity<List<Balance>> getBalance(@PathVariable(name = "cuenta") String cuenta) {
        List<Balance> auxiliarList = auxiliarService.getAllBalance(cuenta);
        if (!auxiliarList.isEmpty()) {
            return new ResponseEntity<>(auxiliarList, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balancekkkFile/{token}")
    public ResponseEntity<HttpStatus> createFileBalance(@PathVariable(name = "token") String token) {
        if (auxiliarService.createFileBalance(token)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("balanceGeneralByDate/{token}")
    public ResponseEntity<List<String>> getListBalanceByDate(@PathVariable(name = "token") String token) {
        List<String> auxiliarList = auxiliarService.getListBalanceByDate(token);
        if (!auxiliarList.isEmpty()) {
            return new ResponseEntity<>(auxiliarList, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("balanceGeneral/{inicial_date}/{final_date}")
    public ResponseEntity<List<Auxiliar>> getListBalance(@PathVariable(name = "inicial_date") String inicial_date, @PathVariable(name = "final_date") String final_date) {
        List<Auxiliar> auxiliarList = auxiliarService.getListBalanceDate(inicial_date, final_date);
        if (!auxiliarList.isEmpty()) {
            return new ResponseEntity<>(auxiliarList, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


}
