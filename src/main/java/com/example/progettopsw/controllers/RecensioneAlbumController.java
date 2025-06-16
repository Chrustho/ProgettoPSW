package com.example.progettopsw.controllers;

import com.example.progettopsw.entities.RecensioneAlbum;
import com.example.progettopsw.services.RecensioneAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/album_reviews")
public class RecensioneAlbumController {
    @Autowired
    private RecensioneAlbumService recensioneAlbumService;

    @PostMapping
    public ResponseEntity<RecensioneAlbum> aggiungiRecensione(
            @RequestBody @Validated RecensioneAlbum recensione) {
        RecensioneAlbum saved = recensioneAlbumService.aggiungiRecensione(recensione);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/search/by_date")
    public ResponseEntity<?> trovaRecensioniDopo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<RecensioneAlbum> recensioni = recensioneAlbumService.trovaRecensioniDopo(data);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/search/by_rating")
    public ResponseEntity<?> trovaRecensioniPerVoto(
            @RequestParam double minVoto,
            @RequestParam double maxVoto) {
        List<RecensioneAlbum> recensioni =
                recensioneAlbumService.trovaRecensioniPerVoto(minVoto, maxVoto);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/search/detailed/{albumId}")
    public ResponseEntity<?> trovaRecensioniDettagliate(
            @PathVariable Long albumId,
            @RequestParam(defaultValue = "100") int minLunghezza) {
        List<RecensioneAlbum> recensioni =
                recensioneAlbumService.trovaRecensioniDettagliateAlbum(albumId, minLunghezza);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }


    @GetMapping("/search/by_keyword")
    public ResponseEntity<?> cercaPerParolaChiave(
            @RequestParam String keyword) {
        if (keyword.isBlank()) return ResponseEntity.badRequest().build();
        List<RecensioneAlbum> recensioni = recensioneAlbumService.cercaPerParolaChiave(keyword);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> trovaRecensioniUtente(@PathVariable Long userId) {
        List<RecensioneAlbum> recensioni = recensioneAlbumService.trovaRecensioniUtente(userId);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/artist/{artistaId}")
    public ResponseEntity<?> trovaRecensioniArtista(@PathVariable Long artistaId) {
        List<RecensioneAlbum> recensioni = recensioneAlbumService.trovaRecensioniArtista(artistaId);
        return recensioni.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(recensioni);
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<?> trovaRecensioniAlbum(@PathVariable Long albumId) {
        List<RecensioneAlbum> lista= recensioneAlbumService.trovaRecensioniDaAlbumId(albumId);
        return lista.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(lista);
    }
    
    @PostMapping("/review")
    public ResponseEntity<RecensioneAlbum> submitReview(@RequestBody @Validated RecensioneAlbum review) {
        RecensioneAlbum savedReview = recensioneAlbumService.aggiungiRecensione(review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/average_rating/{albumId}")
    public ResponseEntity<Double> calcolaVotoMedioAlbum(@PathVariable Long albumId) {
        Double averageRating = recensioneAlbumService.calcolaVotoMedioAlbum(albumId);
        return averageRating != null
                ? ResponseEntity.ok(averageRating)
                : ResponseEntity.noContent().build();
    }
}
