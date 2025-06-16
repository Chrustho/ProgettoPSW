package com.example.progettopsw.services;

import com.example.progettopsw.entities.Album;
import com.example.progettopsw.entities.Canzone;
import com.example.progettopsw.entities.RecensioneAlbum;
import com.example.progettopsw.repositories.AlbumRepository;
import com.example.progettopsw.repositories.RecensioneAlbumRepository;
import com.example.progettopsw.support.exceptions.AlbumGiaPresenteException;
import com.example.progettopsw.support.exceptions.ElementoNonTrovatoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Transactional(readOnly = true)
    public List<Album> mostraTuttiGliAlbum(int pageNumber, int pageSize, String sortBy) {
        return albumRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy)))
                .getContent();
    }

    @Transactional(readOnly = true)
    public Optional<Album> getById(Long id){
        return albumRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Album> trovaAlbumPerArtista(Long idArtista) {
        return albumRepository.findByArtista_Id(idArtista);
    }

    @Transactional(readOnly = true)
    public List<Album> trovaAlbumTramiteArtistaENome(String nomeArtista, String nome) {
        return albumRepository.findByArtistaNomeIgnoreCaseAndNomeIgnoreCase(nomeArtista, nome);
    }

    @Transactional(readOnly = true)
    public List<Album> albumConVotoMedioPiuAltoDi(double minAverage) {
        return albumRepository.findByRecensioniAlbumVotoGreaterThan(minAverage);
    }



    @Transactional(readOnly = true)
    public List<Album> trovaAlbumConAlmenoUnGenereSpecificato(List<String> generi) {
        return albumRepository.findByGeneriNomeIn(generi);
    }

    @Transactional(readOnly = true)
    public List<Album> trovaMiglioriAlbumDiUnDatoGenere(String genere, double soglia) {
        return albumRepository.findByGeneriNomeAndRecensioniAlbumVotoGreaterThan(genere, soglia);
    }

    @Transactional(readOnly = true)
    public List<Album> trovaAlbumConTuttiIGeneriSpecificati(List<String> generi) {
        List<Album> albumsWithAnyGenre = albumRepository.findDistinctByGeneriNomeIn(generi);
        return albumsWithAnyGenre.stream()
                .filter(album -> albumRepository.countByIdAndGeneriNomeIn(album.getId(), generi) == generi.size())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public Album aggiungiAlbum(Album album) {
        if (album.getNome() == null || album.getArtista() == null) {
            throw new IllegalArgumentException("Nome album e artista sono obbligatori");
        }
        if (albumRepository.existsByArtistaNomeIgnoreCaseAndNomeIgnoreCase(
                album.getArtista().getNome(), album.getNome())) {
            throw new AlbumGiaPresenteException("Errore");
        }
        return albumRepository.save(album);
    }

    @Transactional(readOnly = false)
    public Album aggiungiCanzone(Long albumId, Canzone canzone) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(ElementoNonTrovatoException::new);
        canzone.setAlbum(album);
        album.getCanzoni().add(canzone);
        return albumRepository.save(album);
    }
}
