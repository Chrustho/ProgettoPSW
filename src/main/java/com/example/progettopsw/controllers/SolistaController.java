package com.example.progettopsw.controllers;

import com.example.progettopsw.entities.Solista;
import com.example.progettopsw.services.SolistaService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.SolistaGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ResponseMessage> aggiungiSolista(@RequestBody @Validated Solista solista) {
        try {
            Solista saved = solistaService.aggiungiSolista(solista);
            return ResponseEntity.ok(new ResponseMessage("Solista aggiunto correttamente"));
        } catch (SolistaGiaPresenteException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Solista già presente in catalogo"));
        }
    }

    @GetMapping("/search/by_instrument")
    public ResponseEntity<?> trovaSolistiPerStrumento(
            @RequestParam String strumento) {
        if (strumento.isBlank()) return ResponseEntity.badRequest().build();
        List<Solista> solisti = solistaService.trovaSolistiPerStrumento(strumento);
        return solisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(solisti);
    }

    @GetMapping("/most_listened")
    public ResponseEntity<?> trovaSolistiPiuAscoltati(
            @RequestParam(defaultValue = "10") long minStreams) {
        List<Solista> solisti = solistaService.trovaSolistiPiuAscoltati(minStreams);
        return solisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(solisti);
    }

    @GetMapping("/search/by_name")
    public ResponseEntity<?> cercaPerNome(
            @RequestParam String nome) {
        if (nome.isBlank()) return ResponseEntity.badRequest().build();
        List<Solista> solisti = solistaService.cercaPerNome(nome);
        return solisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(solisti);
    }

    @GetMapping("/search/by_genres")
    public ResponseEntity<?> cercaPerGeneri(
            @RequestParam List<String> generi,
            @RequestParam(defaultValue = "false") boolean tutti) {
        if (generi.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Specificare almeno un genere"));
        }
        List<Solista> solisti = solistaService.trovaSolistiPerGeneri(generi, tutti);
        return solisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(solisti);
    }

    @GetMapping("/search/by_avg_vote")
    public ResponseEntity<?> trovaSolistiPerMediaVoti(
            @RequestParam(defaultValue = "4.2") double minAvg) {
        List<Solista> solisti = solistaService.trovaSolistiPerMediaVoti(minAvg);
        return solisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(solisti);
    }

    @GetMapping("/most_followed")
    public ResponseEntity<?> trovaSolistiPiuSeguiti(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Solista> solisti = solistaService.trovaSolistiPiuSeguiti(pageNumber, pageSize, sortBy);
        return solisti.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(solisti);
    }


}
