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
    public ResponseEntity<ResponseMessage> createGenere(@RequestBody @Validated Genere genere) {
        try {
            Genere saved = genereService.aggiungiGenere(genere);
            return ResponseEntity.ok(new ResponseMessage("Genere aggiunto correttamente"));
        } catch (GenereGiaEsistenteException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Genere già presente!"));
        }
    }

    @GetMapping("/search/by_prefix")
    public ResponseEntity<?> trovaGeneriCheInizianoPer(
            @RequestParam  String prefisso) {
        if (prefisso.isBlank()) return ResponseEntity.badRequest().build();
        List<Genere> genres = genereService.trovaGeneriCheInizianoPer(prefisso);
        return genres.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(genres);
    }

    @GetMapping("/most_popular")
    public ResponseEntity<?> trovaGeneriPiuPopolari(
            @RequestParam(defaultValue = "15") long minAlbums) {
        List<Genere> genres = genereService.trovaGeneriPopolari(minAlbums);
        return genres.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(genres);
    }

    @GetMapping("/most_listened")
    public ResponseEntity<?> trovaGeneriPiuAscoltati(
            @RequestParam(defaultValue = "1") long minStreams) {
        List<Genere> genres = genereService.trovaGeneriPiuAscoltati(minStreams);
        return genres.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(genres);
    }

    @GetMapping("/search/by_avg_vote")
    public ResponseEntity<?> trovaGeneriMeglioRecensiti(
            @RequestParam(defaultValue = "4.2") double minAvg) {
        List<Genere> genres = genereService.trovaGeneriMeglioRecensiti(minAvg);
        return genres.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(genres);
    }

    @GetMapping("/most_followed")
    public ResponseEntity<?> trovaGeneriPiuSeguiti(
            @RequestParam(defaultValue = "10000") long minFollowers) {
        List<Genere> genres = genereService.trovaGeneriPiuSeguiti(minFollowers);
        return genres.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(genres);
    }

    @GetMapping("/most_played")
    public ResponseEntity<?> trovaGeneriPiuSuonati(
            @RequestParam(defaultValue = "15") long minArtists) {
        List<Genere> genres = genereService.trovaGeneriPiuSuonati(minArtists);
        return genres.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(genres);
    }





}
