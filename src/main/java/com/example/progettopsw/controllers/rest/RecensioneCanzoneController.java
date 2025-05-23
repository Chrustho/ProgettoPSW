package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.RecensioneCanzone;
import com.example.progettopsw.services.RecensioneCanzoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("song_reviews")
public class RecensioneCanzoneController {
    @Autowired
    private RecensioneCanzoneService recensioneCanzoneService;

    @PostMapping
    public ResponseEntity<RecensioneCanzone> aggiungiRecensione(
            @RequestBody @Validated RecensioneCanzone recensione) {
        RecensioneCanzone saved = recensioneCanzoneService.aggiungiRecensione(recensione);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/search/by_date")
    public ResponseEntity<?> trovaRecensioniDopo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<RecensioneCanzone> recensioni = recensioneCanzoneService.trovaRecensioniDopo(data);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/search/by_rating")
    public ResponseEntity<?> trovaRecensioniPerVoto(
            @RequestParam double minVoto,
            @RequestParam double maxVoto) {
        List<RecensioneCanzone> recensioni =
                recensioneCanzoneService.trovaRecensioniPerVoto(minVoto, maxVoto);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/search/detailed/{canzoneId}")
    public ResponseEntity<?> trovaRecensioniDettagliate(
            @PathVariable Long canzoneId,
            @RequestParam(defaultValue = "100") int minLunghezza) {
        List<RecensioneCanzone> recensioni =
                recensioneCanzoneService.trovaRecensioniDettagliateCanzone(canzoneId, minLunghezza);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }



    @GetMapping("/search/by_keyword")
    public ResponseEntity<?> cercaPerParolaChiave(
            @RequestParam  String keyword) {
        if (keyword.isBlank()) return ResponseEntity.badRequest().build();
        List<RecensioneCanzone> recensioni =
                recensioneCanzoneService.cercaPerParolaChiave(keyword);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> trovaRecensioniUtente(@PathVariable Long userId) {
        List<RecensioneCanzone> recensioni =
                recensioneCanzoneService.trovaRecensioniUtente(userId);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/artist/{artistaId}")
    public ResponseEntity<?> trovaRecensioniArtista(@PathVariable Long artistaId) {
        List<RecensioneCanzone> recensioni =
                recensioneCanzoneService.trovaRecensioniArtista(artistaId);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<?> trovaRecensioniAlbum(@PathVariable Long albumId) {
        List<RecensioneCanzone> recensioni =
                recensioneCanzoneService.trovaRecensioniAlbum(albumId);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }



}
