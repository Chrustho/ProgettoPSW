package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Band;
import com.example.progettopsw.services.BandService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.ArtistaGiaPresente;
import com.example.progettopsw.support.exceptions.ArtistaGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bands")
public class BandController {
    @Autowired
    private BandService bandService;

    @PostMapping
    public ResponseEntity aggiungiBand(@RequestBody @Validated Band band){
        try {
            bandService.aggiungiBand(band);
        }catch (ArtistaGiaPresenteException e){
            return new ResponseEntity<>(new ResponseMessage("Band già presente in catalogo"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ResponseMessage("Band aggiunta correttamente"), HttpStatus.OK);
    }


}
