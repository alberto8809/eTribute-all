package org.example.users.controller;


import org.example.users.model.Auxiliar;
import org.example.users.model.Balance;
import org.example.users.model.ListAuxiliar;
import org.example.users.service.AuxiliarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @GetMapping("auxiliar/{token}")
    public ResponseEntity<List<ListAuxiliar>> getAuxiliar(@PathVariable(name = "token") String token) {
        List<ListAuxiliar> auxiliars = auxiliarService.getListAccounts(token);
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


    @GetMapping("balance/{token}")
    public ResponseEntity<List<Balance>> getBalance(@PathVariable(name = "token") String token) {
        List<Balance> auxiliarList = auxiliarService.getAllBalance(token);
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

}
