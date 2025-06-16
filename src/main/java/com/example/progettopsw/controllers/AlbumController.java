package com.example.progettopsw.controllers;

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
import java.util.Optional;

@RestController
@RequestMapping("/albums")
@Validated
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createAlbum(@RequestBody @Validated Album album) {
        try {
            Album savedAlbum = albumService.aggiungiAlbum(album);
            return ResponseEntity.ok(new ResponseMessage("Album aggiunto correttamente!"));
        } catch (AlbumGiaPresenteException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Album già presente in catalogo"));
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {

        List<Album> albums = albumService.mostraTuttiGliAlbum(pageNumber, pageSize, sortBy);
        return albums.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(albums);
    }

    @GetMapping("search/byId")
    public ResponseEntity<?> getById(@RequestParam(required = true) Long id) {
        Optional<Album> album= albumService.getById(id);
        return album.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(album.get());
    }

    @GetMapping("/search/by_artist")
    public ResponseEntity<?> getByArtista(@RequestParam(required = true) Long idArtista) {
        if (idArtista == null || idArtista <= 0) {
            return ResponseEntity.badRequest().build();
        }
        List<Album> albums = albumService.trovaAlbumPerArtista(idArtista);
        return albums.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(albums);
    }

    @GetMapping("/search/by_artist_and_name")
    public ResponseEntity<?> getByNomeAndArtista(
            @RequestParam(required = true) String artista,
            @RequestParam(required = true) String nome) {
        if (artista.isBlank() || nome.isBlank()){
            return ResponseEntity.badRequest().build();
        }
        List<Album> albums = albumService.trovaAlbumTramiteArtistaENome(artista, nome);
        return albums.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(albums);
    }

    @GetMapping("/most_voted/by_avg")
    public ResponseEntity<?> getMostVotedAlbums(
            @RequestParam Double votomin) {

        double threshold = (votomin != null && votomin > 0) ? votomin : 4.0;
        List<Album> albums = albumService.albumConVotoMedioPiuAltoDi(threshold);
        return albums.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(albums);
    }




    @GetMapping("/search/by_generi")
    public ResponseEntity<?> getByGenere(
            @RequestParam List<String> generi,
            @RequestParam boolean tutti) {

        if (generi.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Parametri in input incorretti!"));
        }

        List<Album> albums = tutti
                ? albumService.trovaAlbumConTuttiIGeneriSpecificati(generi)
                : albumService.trovaAlbumConAlmenoUnGenereSpecificato(generi);

        return albums.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(albums);
    }

    @GetMapping("/most_voted/by_genre")
    public ResponseEntity<?> getMiglioriAlbumDiUnGenere(
            @RequestParam String genere,
            @RequestParam(required = false) Double soglia) {

        if (genere.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Genere non specificato!"));
        }

        double threshold = (soglia != null && soglia > 0) ? soglia : 4.2;
        List<Album> albums = albumService.trovaMiglioriAlbumDiUnDatoGenere(genere, threshold);

        return albums.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(albums);
    }
}
