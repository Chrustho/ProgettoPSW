package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Band;
import com.example.progettopsw.services.BandService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.ArtistaGiaPresenteException;
import org.antlr.v4.runtime.misc.NotNull;
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
    public ResponseEntity<ResponseMessage> aggiungiBand(@RequestBody @Validated Band band) {
        try {
            Band savedBand = bandService.aggiungiBand(band);
            return ResponseEntity.ok(new ResponseMessage("Band aggiunta correttamente"));
        } catch (ArtistaGiaPresenteException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Band già presente in catalogo"));
        }
    }

    @GetMapping("/search/by_avg_vote")
    public ResponseEntity<?> trovaBandConVotoMedio(
            @RequestParam(defaultValue = "4.0") double minAvg) {
        List<Band> bands = bandService.trovaBandConVotoAlbum(minAvg);
        return bands.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(bands);
    }

    @GetMapping("/most_listened")
    public ResponseEntity<?> trovaBandPiuAscoltate(
            @RequestParam(defaultValue = "0") long minStreams) {
        List<Band> bands = bandService.trovaBandPiuAscoltate(minStreams);
        return bands.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(bands);
    }

    @GetMapping("/search/by_genres")
    public ResponseEntity<?> trovaBandDaGenere(
            @RequestParam List<String> generi,
            @RequestParam boolean tutti) {
        if (generi.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Inserire generi!"));
        }

        List<Band> bands = tutti
                ? bandService.trovaBandConTuttiIGeneri(generi)
                : bandService.trovaBandConAlmenoUnGenere(generi);

        return bands.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(bands);
    }







}
