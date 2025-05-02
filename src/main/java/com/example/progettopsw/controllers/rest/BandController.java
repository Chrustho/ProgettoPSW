package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Band;
import com.example.progettopsw.services.BandService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.ArtistaGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/search/by_avg_vote")
    public ResponseEntity trovaBandConVotoMedio(@RequestParam double minAvg){
        List<Band> bands;
        if (minAvg>0){
            bands=bandService.trovaBandConVotoAlbum(minAvg);
        }else{
            bands=bandService.trovaBandConVotoAlbum(4.2);
        }
        if (bands.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(bands,HttpStatus.OK);
    }

    @GetMapping("/most_listened")
    public ResponseEntity trovaBandPiuAscoltate(){
        List<Band> bands=bandService.trovaBandPiuAscoltate(100000);
        if (bands.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun Risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(bands,HttpStatus.OK);
    }

    @GetMapping("/search/by_generi")
    public ResponseEntity trovaBandDaGenere(@RequestParam List<String> generi, @RequestParam boolean tutti){
        if (generi.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Inserire generi!"), HttpStatus.BAD_REQUEST);
        }
        List<Band> bands;
        if (tutti){
            bands=bandService.trovaBandConTuttiIGeneri(generi,generi.size());
        }else {
            bands=bandService.trovaBandConAlmenoUnGenere(generi);
        }
        if (bands.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(bands, HttpStatus.OK);
    }

    @GetMapping("/search/followed_by")
    public ResponseEntity trovaBandSeguiteDaUser(@RequestParam Long userId){
        List<Band> bands=bandService.trovaBandSeguiteDaUser(userId);
        if (bands.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(bands,HttpStatus.OK);
    }




}
