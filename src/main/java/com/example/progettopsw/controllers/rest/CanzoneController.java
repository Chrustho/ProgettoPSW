package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Canzone;
import com.example.progettopsw.services.CanzoneService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.CanzoneGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class CanzoneController {
    @Autowired
    private CanzoneService canzoneService;

    @PostMapping
    public ResponseEntity aggiungiCanzone(@RequestBody @Validated Canzone canzone){
        try {
            canzoneService.aggiungiCanzone(canzone);
        }catch (CanzoneGiaPresenteException e){
            return new ResponseEntity(new ResponseMessage("Canzone già presente"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(canzone,HttpStatus.OK);
    }

    @GetMapping("/sorted/longest")
    public ResponseEntity trovaCanzoniPiuLunghe(){
        List<Canzone> songs=canzoneService.trovaCanzoniPiuLunghe();
        if (songs.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(songs,HttpStatus.OK);
    }

    @GetMapping("/sorted/shortest")
    public ResponseEntity trovaCanzoniPiuCorte(){
        List<Canzone> songs=canzoneService.trovaCanzoniPiuCorte();
        if (songs.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(songs,HttpStatus.OK);
    }

    @GetMapping("/search/partial_title")
    public ResponseEntity trovaCanzoniConXNelNome(String nomeParziale){
        if (nomeParziale.isBlank()){
            return new ResponseEntity<>(new ResponseMessage("Nessuna keyword passata in input!"), HttpStatus.BAD_REQUEST);
        }
        List<Canzone> songs=canzoneService.trovaCanzoneConXNelNome(nomeParziale);
        if (songs.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(songs,HttpStatus.OK);
    }

    @GetMapping("/search/top_rated")
    public ResponseEntity trovaCanzoniConVotoMaggioreDi(double soglia){
        List<Canzone> songs;
        if (soglia>0){
            songs=canzoneService.trovaCanzoniConVotoMaggioreDi(soglia);
        }else {
            songs=canzoneService.trovaCanzoniConVotoMaggioreDi(4.2);
        }
        if (songs.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(songs,HttpStatus.OK);
    }

    @GetMapping("/search/by_genres")
    public ResponseEntity trovaCanzoniPerGeneri(List<String> generi, boolean tutti){
        List<Canzone> songs;
        if (tutti){
            songs=canzoneService.trovaAlbumConTuttiIGeneriSpecificati(generi);
        }else {
            songs=canzoneService.trovaAlbumConAlmenoUnGenereSpecificato(generi);
        }
        if (songs.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(songs,HttpStatus.OK);
    }

    @GetMapping("/top_reviewed")
    public ResponseEntity trovaCanzoniPiuRecensite(){
        List<Canzone> songs=canzoneService.canzoniPiuRecensite(10);
        if (songs.isEmpty()){
            return new ResponseEntity<>(new ResponseMessage("Nessun risultato"),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(songs,HttpStatus.OK);
    }

    @GetMapping("/top_listened")
    public ResponseEntity top10CanzoniPiuAscoltate(){
        List<Canzone> songs=canzoneService.top10CanzoniPiuAscoltate();
        if (songs.isEmpty()){
            return new ResponseEntity<>(new ResponseMessage("Nessun risultato"),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(songs,HttpStatus.OK);
    }

    @GetMapping("/search/top_songs_by_artist")
    public ResponseEntity trovaNCanzoniPiuAscoltatiDiArtista(Long artistaId,int pageNumber, int pageSize, String sortBy){
        List<Canzone> songs=canzoneService.trovaNCanzoniPiuAscoltatiDiArtista(artistaId, pageNumber, pageSize, sortBy);
        if (songs.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(songs,HttpStatus.OK);
    }







}
