package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Artista;
import com.example.progettopsw.services.ArtistaService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.ArtistaGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistaController {
    @Autowired
    private ArtistaService artistaService;

    @GetMapping("/search/by_nome")
    public ResponseEntity trovaArtistaByNome(@RequestParam String nome){
        if (nome.isBlank()){
            return new ResponseEntity(new ResponseMessage("Nome incorretto!"), HttpStatus.BAD_REQUEST);
        }
        List<Artista> artista;
        artista=artistaService.trovaArtisticonNome(nome);
        if (artista.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun Risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(artista, HttpStatus.OK);
    }

    @GetMapping("/most_popular")
    public ResponseEntity artistiPiuPopolari(){
        List<Object[]> artisti=artistaService.artistiPiuPopolari();
        if (artisti.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(artisti,HttpStatus.OK);
    }

    @GetMapping("/top_artists/by_vote")
    public ResponseEntity artistiPiuVotati(@RequestParam double minAvg){
        List<Artista> artists;
        if (minAvg>0){
            artists=artistaService.artistiConVotoMedioSuperioreA(minAvg);
        }else {
            artists=artistaService.artistiConVotoMedioSuperioreA(4.2);
        }
        if (artists.isEmpty()){
            return new ResponseEntity<>(new ResponseMessage("Nessun risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(artists,HttpStatus.OK);
    }

    @GetMapping("/top_artist/by_genre")
    public ResponseEntity artistiPiuPopolariDiUnDatoGenere(@RequestParam String genere){
        if (genere.isBlank()){
            return new ResponseEntity(new ResponseMessage("Inserire genere valido"), HttpStatus.BAD_REQUEST);
        }
        List<Artista> artists= artistaService.artistiPopolariDiGenere(genere, 0);
        if (artists.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(artists,HttpStatus.OK);
    }

    @GetMapping("/search/by_generi")
    public ResponseEntity trovaArtistaByGeneri(@RequestParam List<String> generi, boolean tutti){
        if (generi.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Inserire generi!"), HttpStatus.BAD_REQUEST);
        }
        List<Artista> artists;
        if (tutti){
            artists=artistaService.trovaArtistiConTuttiIGeneri(generi,generi.size());
        }else {
            artists=artistaService.trovaArtistiConAlmenoUnGenere(generi);
        }
        if (artists.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(artists,HttpStatus.OK);
    }

    @GetMapping("/most_recent_releases")
    public ResponseEntity artistiConReleaseRecenti(@RequestParam int years){
        List<Artista> artists=artistaService.artistiConReleaseRecenti(years);
        if (artists.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun Risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(artists,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity creaArtista(@RequestBody @Validated Artista artista){
        try {
            artistaService.aggiungiArtista(artista);
        }catch (ArtistaGiaPresenteException e){
            return new ResponseEntity<>(new ResponseMessage("Artista già presente in catalogo"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ResponseMessage("Artista aggiunto correttamente"), HttpStatus.OK);
    }


}
