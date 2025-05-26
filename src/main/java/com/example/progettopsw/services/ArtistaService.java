package com.example.progettopsw.services;

import com.example.progettopsw.entities.Album;
import com.example.progettopsw.entities.Artista;
import com.example.progettopsw.entities.RecensioneAlbum;
import com.example.progettopsw.repositories.AlbumRepository;
import com.example.progettopsw.repositories.ArtistaRepository;
import com.example.progettopsw.repositories.RecensioneAlbumRepository;
import com.example.progettopsw.support.exceptions.ArtistaGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArtistaService {
    @Autowired
    private ArtistaRepository artistaRepository;

    @Transactional(readOnly = true)
    public List<Artista> trovaArtistiConNome(String nome) {
        return artistaRepository.findByNomeContainingIgnoreCase(nome);
    }



    @Transactional(readOnly = true)
    public List<Artista> artistiPiuPopolari(Long minFollowers) {
        return artistaRepository.findByFollowerCountGreaterThanOrderByFollowerCountDesc(minFollowers);
    }

    @Transactional(readOnly = true)
    public List<Artista> artistiPopolariDiGenere(String genere, Long minFollowers) {
        return artistaRepository.findByGenreAndFollowerCountGreaterThan(genere, minFollowers);
    }



    @Transactional(readOnly = true)
    public List<Artista> trovaArtistiConAlmenoUnGenere(List<String> generi) {
        return artistaRepository.findByGeneriNomeIn(generi);
    }

    @Transactional(readOnly = true)
    public List<Artista> trovaArtistiConTuttiIGeneri(List<String> generi) {
        List<Artista> artistiWithAnyGenre = artistaRepository.findDistinctByGeneriNomeIn(generi);
        return artistiWithAnyGenre.stream()
                .filter(artista -> artistaRepository.countByIdAndGeneriNomeIn(artista.getId(), generi)
                        == generi.size())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public Artista aggiungiArtista(Artista artista) {
        if (artista.getNome() == null) {
            throw new IllegalArgumentException("Nome artista obbligatorio");
        }
        if (artistaRepository.existsByNomeIgnoreCase(artista.getNome())) {
            throw new ArtistaGiaPresenteException();
        }
        return artistaRepository.save(artista);
    }

    @Transactional(readOnly = true)
    public List<Artista> artistiConReleaseRecenti(int years) {
        int yearsAgo = LocalDate.now().getYear() - years;
        return artistaRepository.findByAlbumsAnnoRilascioGreaterThanEqual(yearsAgo);
    }


}
