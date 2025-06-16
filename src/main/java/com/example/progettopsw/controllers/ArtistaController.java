package com.example.progettopsw.controllers;

import com.example.progettopsw.entities.Artista;
import com.example.progettopsw.services.ArtistaService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.ArtistaGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistaController {
    @Autowired
    private ArtistaService artistaService;

    @PostMapping
    public ResponseEntity<ResponseMessage> creaArtista(@RequestBody @Validated Artista artista) {
        try {
            Artista savedArtista = artistaService.aggiungiArtista(artista);
            return ResponseEntity.ok(new ResponseMessage("Artista aggiunto correttamente"));
        } catch (ArtistaGiaPresenteException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Artista già presente in catalogo"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> trovaArtistaPerId(@PathVariable Long id) {
        return artistaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/by_name")
    public ResponseEntity<?> cercaPerNome(@RequestParam String nome) {
        if (nome.isBlank()) return ResponseEntity.badRequest().build();
        List<Artista> artisti = artistaService.trovaArtistiConNome(nome);
        return artisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(artisti);
    }


    @GetMapping("/most_popular")
    public ResponseEntity<?> artistiPiuPopolari(
            @RequestParam(defaultValue = "0") Long minFollowers) {
        List<Artista> artisti = artistaService.artistiPiuPopolari(minFollowers);
        return artisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(artisti);
    }

    @GetMapping("/popular/by_genre")
    public ResponseEntity<?> artistiPopolariPerGenere(
            @RequestParam String genere,
            @RequestParam(defaultValue = "0") Long minFollowers) {
        if (genere.isBlank()) return ResponseEntity.badRequest().build();
        List<Artista> artisti = artistaService.artistiPopolariDiGenere(genere, minFollowers);
        return artisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(artisti);
    }



    @GetMapping("/search/by_genres")
    public ResponseEntity<?> cercaPerGeneri(
            @RequestParam List<String> generi,
            @RequestParam boolean tutti) {
        if (generi.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Specificare almeno un genere"));
        }

        List<Artista> artisti = tutti
                ? artistaService.trovaArtistiConTuttiIGeneri(generi)
                : artistaService.trovaArtistiConAlmenoUnGenere(generi);

        return artisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(artisti);
    }

    @GetMapping("/recent_releases")
    public ResponseEntity<?> artistiConReleaseRecenti(
            @RequestParam(defaultValue = "2") int years) {
        List<Artista> artisti = artistaService.artistiConReleaseRecenti(years);
        return artisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(artisti);
    }

    @GetMapping("/by_avg_vote")
    public ResponseEntity<?> trovaArtistiPerMediaVoti(
            @RequestParam(defaultValue = "0.0") double votoMinimo) {
        List<Artista> artisti = artistaService.trovaArtistiPerMediaVotiAlta(votoMinimo);
        return artisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(artisti);
    }



}
