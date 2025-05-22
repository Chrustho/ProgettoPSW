package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Acquisto;
import com.example.progettopsw.entities.Users;
import com.example.progettopsw.services.AcquistoService;
import com.example.progettopsw.services.UserService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.QuantitaNonDisponibileException;
import com.example.progettopsw.support.exceptions.UtenteNonTrovatoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class AcquistoController {
    @Autowired
    private AcquistoService acquistoService;
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity createAcquisto(@RequestBody @Validated Acquisto acquisto){
        try {
            acquistoService.aggiungiAcquisto(acquisto);
        } catch (QuantitaNonDisponibileException e) {
            return new ResponseEntity(new ResponseMessage("Impossibile aggiungere Acquisto!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ResponseMessage("Acquisto validato!"), HttpStatus.OK);
    }

    @GetMapping("/{user}")
    public ResponseEntity getPurchases(@RequestBody @Validated Users user) throws UtenteNonTrovatoException {
        List<Acquisto> acquisti=acquistoService.prendiAcquistiDaUtente(user);
        return new ResponseEntity<>(acquisti,HttpStatus.OK);
    }
}
