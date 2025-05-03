package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Solista;
import com.example.progettopsw.services.SolistaService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.SolistaGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solo_artists")
public class SolistaController {
    @Autowired
    private SolistaService solistaService;

    @PostMapping
    public ResponseEntity aggiungiSolista(@RequestBody @Validated Solista solista){
        if (solista.getNome()!=null && solista.getStrumento()!=null){
            return new ResponseEntity<>(new ResponseMessage("Paramentri di input incorretti!"), HttpStatus.BAD_REQUEST);
        }
        try {
            solistaService.aggiungiSolista(solista);
        }catch (SolistaGiaPresenteException e){
            return new ResponseEntity(new ResponseMessage("Solista già presente in catalogo"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ResponseMessage("Solista aggiunto correttamente"), HttpStatus.OK);
    }

    @GetMapping("/search/by_instrument")
    public ResponseEntity trovaSolistaCheSuonano(@RequestParam String strumento){
        if (strumento.isBlank()){
            return new ResponseEntity(new ResponseMessage("Inserisci strumento"), HttpStatus.BAD_REQUEST);
        }
        List<Solista> solisti=solistaService.trovaSolistiCheSuonano(strumento);
        if (solisti.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(solisti,HttpStatus.OK);
    }

    @GetMapping("/most_listened")
    public ResponseEntity trovaSolistiPiuAscoltati(){
        List<Solista> solisti=solistaService.trovaSolistaConPiuDiNStream(100000);
        if (solisti.isEmpty()){
            return new ResponseEntity<>(new ResponseMessage("Nessun risultato"), HttpStatus.OK);
        }
        return new ResponseEntity(solisti,HttpStatus.OK);
    }

    @GetMapping("/search/by_name")
    public ResponseEntity cercaPerNome(@RequestParam String nome){
        if (nome.isBlank()){
            return new ResponseEntity<>(new ResponseMessage("Inserisci un nome!"), HttpStatus.BAD_REQUEST);
        }
        List<Solista> solisti=solistaService.cercaPerNome(nome);
        if (solisti.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(solisti,HttpStatus.OK);
    }

    @GetMapping("/search/by_genre")
    public ResponseEntity cercaPerStrumento(@RequestParam List<String> generi, @RequestParam boolean tutti){
        if (generi.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Inserisci generi!"),HttpStatus.BAD_REQUEST);
        }
        List<Solista> solisti;
        if (tutti){
            solisti=solistaService.trovaSolistaConTuttiIGeneri(generi);
        }else {
            solisti=solistaService.trovaSolistaConAlmenoUnGenereSpecificato(generi);
        }
        if (solisti.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(solisti,HttpStatus.OK);
    }

    @GetMapping("/search/by_avg_vote")
    public ResponseEntity trovaSolistaConVotiMaggioriDi(@RequestParam double minAvg){
        List<Solista> solisti;
        if (minAvg>0){
            solisti=solistaService.trovaSolistaConVotiMAggioriDi(minAvg);
        }else{
            solisti=solistaService.trovaSolistaConVotiMAggioriDi(4.2);
        }
        if (solisti.isEmpty()){
            return new ResponseEntity<>(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(solisti,HttpStatus.OK);
    }

    @GetMapping("/most_followed")
    public ResponseEntity topSolistiPerNumeroDiFollower(@RequestParam int pageNumber,@RequestParam int pageSize, @RequestParam String sortBy){
        List<Solista> solisti=solistaService.topSolistiPerNumeroDiFollower(pageNumber, pageSize, sortBy);
        if (solisti.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(solisti,HttpStatus.OK);
    }



}
