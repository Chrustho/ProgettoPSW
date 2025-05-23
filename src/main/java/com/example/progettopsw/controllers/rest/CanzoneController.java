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
    public ResponseEntity<ResponseMessage> aggiungiCanzone(@RequestBody @Validated Canzone canzone) {
        try {
            Canzone saved = canzoneService.aggiungiCanzone(canzone);
            return ResponseEntity.ok(new ResponseMessage("Canzone aggiunta con successo"));
        } catch (CanzoneGiaPresenteException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Canzone già presente"));
        }
    }

    @GetMapping("/sorted/longest")
    public ResponseEntity<?> trovaCanzoniPiuLunghe() {
        List<Canzone> songs = canzoneService.trovaCanzoniPiuLunghe();
        return songs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(songs);
    }

    @GetMapping("/sorted/shortest")
    public ResponseEntity<?> trovaCanzoniPiuCorte() {
        List<Canzone> songs = canzoneService.trovaCanzoniPiuCorte();
        return songs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(songs);
    }

    @GetMapping("/search/by_name")
    public ResponseEntity<?> cercaPerNome(@RequestParam String nome) {
        if (nome.isBlank()) return ResponseEntity.badRequest().build();
        List<Canzone> songs = canzoneService.trovaCanzonePerNome(nome);
        return songs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(songs);
    }

    @GetMapping("/search/top_rated")
    public ResponseEntity<?> trovaCanzoniConVotoMaggioreDi(
            @RequestParam(defaultValue = "4.2") double minVoto) {
        List<Canzone> songs = canzoneService.trovaCanzoniConVotoMaggioreDi(minVoto);
        return songs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(songs);
    }

    @GetMapping("/search/by_genres")
    public ResponseEntity<?> cercaPerGeneri(
            @RequestParam List<String> generi,
            @RequestParam boolean tutti) {
        if (generi.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Specificare almeno un genere"));
        }
        List<Canzone> songs = canzoneService.trovaCanzoniPerGeneri(generi, tutti);
        return songs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(songs);
    }

    @GetMapping("/most_reviewed")
    public ResponseEntity<?> trovaCanzoniPiuRecensite(
            @RequestParam(defaultValue = "1") long minRecensioni) {
        List<Canzone> songs = canzoneService.trovaCanzoniPiuRecensite(minRecensioni);
        return songs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(songs);
    }

    @GetMapping("/most_listened")
    public ResponseEntity<?> top10CanzoniPiuAscoltate() {
        List<Canzone> songs = canzoneService.top10CanzoniPiuAscoltate();
        return songs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(songs);
    }

    @GetMapping("/search/by_artist")
    public ResponseEntity<?> trovaCanzoniDiArtista(
            @RequestParam(required = true) Long artistaId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "numeroAscolti") String sortBy) {
        List<Canzone> songs = canzoneService.trovaCanzoniDiArtista(artistaId, pageNumber, pageSize, sortBy);
        return songs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(songs);
    }








}
