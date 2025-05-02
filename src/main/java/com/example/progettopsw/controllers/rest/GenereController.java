package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Genere;
import com.example.progettopsw.services.GenereService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.GenereGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenereController {
    @Autowired
    private GenereService genereService;

    @PostMapping
    public ResponseEntity createGenere(@RequestBody @Validated Genere genere){
        try {
            genereService.aggiungiGenere(genere);
        }catch (GenereGiaEsistenteException e){
            return new ResponseEntity(new ResponseMessage("Genere già presente!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ResponseMessage("Genere aggiunto correttamente"), HttpStatus.OK);
    }

    @GetMapping("/search/by_prefix")
    public ResponseEntity trovaGeneriCheInizianoPer(String prefisso){
        if (prefisso.isBlank()){
            return new ResponseEntity(new ResponseMessage("Inserire prefisso"),HttpStatus.BAD_REQUEST);
        }
        List<Genere> genres=genereService.trovaGeneriCheInizianoPer(prefisso);
        if (genres.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(genres,HttpStatus.OK);
    }

    @GetMapping("/most_popular")
    public ResponseEntity trovaGeneriPiuPopolari(){
        List<Genere> genres=genereService.trovaGeneriPopolari(15);
        if (genres.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(genres,HttpStatus.OK);
    }

    @GetMapping("/most_listened")
    public ResponseEntity trovaGeneriPiuAscoltati(){
        List<Genere> genres=genereService.trovaGeneriPiuAscoltati(10000);
        if (genres.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(genres,HttpStatus.OK);
    }

    @GetMapping("/search/by_avg_vote")
    public ResponseEntity trovaGeneriMeglioRecensiti(double minAvg){
        List<Genere> genres;
        if (minAvg>0){
            genres=genereService.trovaGeneriMeglioRecensiti(minAvg);
        }else {
            genres=genereService.trovaGeneriMeglioRecensiti(4.2);
        }
        if (genres.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(genres,HttpStatus.OK);
    }

    @GetMapping("/most_followed")
    public ResponseEntity trovaGeneriPiuSeguiti(){
        List<Genere> genres=genereService.trovaGeneriPiuSeguiti(10000);
        if (genres.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(genres,HttpStatus.OK);
    }

    @GetMapping("/most_played")
    public ResponseEntity trovaGeneriPiuSuonati(){
        List<Genere> genres=genereService.trovaGeneriPiuSuonati(15);
        if (genres.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(genres,HttpStatus.OK);
    }




}
