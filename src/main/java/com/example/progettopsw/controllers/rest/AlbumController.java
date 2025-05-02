package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Album;
import com.example.progettopsw.services.AlbumService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.AlbumGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @PostMapping
    public ResponseEntity createAlbum(@RequestBody @Validated Album album){
        try {
            albumService.aggiungiAlbum(album);
        }catch (AlbumGiaPresenteException e){
            return new ResponseEntity(new ResponseMessage("Album già presente in catalogo"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ResponseMessage("Album aggiiunto correttamente!"), HttpStatus.OK);
    }

    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Album> albums = albumService.mostraTuttiGliAlbum(pageNumber, pageSize, sortBy);
        if (albums.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("Nessun Risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/search/by_artist_and_name")
    public ResponseEntity getByNomeAndArtista(@RequestParam(required = true) String artista, @RequestParam(required = true) String nome) {
        List<Album> albums = albumService.trovaAlbumTramiteArtistaENome(artista, nome);
        if (albums.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("Nessun risultato trovato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(albums, HttpStatus.OK);
    }

    @GetMapping("/most_voted")
    public ResponseEntity getMostVotedAlbums(@RequestParam(required = false) double votoMin) {
        List<Album> albums;
        if (votoMin > 0) {
            albums = albumService.albumConVotoMedioPiuAltoDi(votoMin);
        } else {
            albums = albumService.albumConVotoMedioPiuAltoDi(4.0);
        }
        if (albums.isEmpty()) {
            return new ResponseEntity(new ResponseMessage("Nessun Risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/most_wishlisted")
    public ResponseEntity getMostWishlisted(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Album> albums= albumService.mostraAlbumPiuDesiderati(pageNumber,pageSize,sortBy);
        if (albums.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/search/by_generi")
    public ResponseEntity getByGenere(@RequestParam List<String> generi, @RequestParam boolean tutti){
        if (generi.isEmpty()){
            return new ResponseEntity<>(new ResponseMessage("Parametri in input incorretti!"), HttpStatus.BAD_REQUEST);
        }
        List<Album> albums;
        if (tutti){
            albums=albumService.trovaAlbumConTuttiIGeneriSpecificati(generi);
        }else {
            albums=albumService.trovaAlbumConAlmenoUnGenereSpecificato(generi);
        }
        if (albums.isEmpty()){
            return new ResponseEntity<>(new ResponseMessage("Nessun Risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/most_voted_by_genre")
    public ResponseEntity getMiglioriAlbumDiUnGenere(@RequestParam String genere, @RequestParam(required = false) double soglia){
        if (genere.isBlank()){
            new ResponseEntity<>(new ResponseMessage("Genere non specificato!"), HttpStatus.BAD_REQUEST);
        }
        List<Album> albums;
        if (soglia>0){
            albums=albumService.trovaMiglioriAlbumDiUnDatoGenere(genere,soglia);
        }else{
            albums=albumService.trovaMiglioriAlbumDiUnDatoGenere(genere,4.2);
        }
        if (albums.isEmpty()){
            new ResponseEntity<>(new ResponseMessage("Nessun Risultato"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(albums,HttpStatus.OK);
    }
}
