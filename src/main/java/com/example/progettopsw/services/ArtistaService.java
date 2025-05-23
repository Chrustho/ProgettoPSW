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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ArtistaService {
    @Autowired
    private ArtistaRepository artistaRepository;
    @Autowired
    private RecensioneAlbumRepository recensioneAlbumRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @Transactional(readOnly = true)
    public List<Artista> trovaArtisticonNome(String nome){
        return artistaRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Artista> artistiCheHannoRilasciatoTra(int fromYear, int toYear){
        return artistaRepository.findByAlbumReleaseYearRange(fromYear,toYear);
    }

    @Transactional(readOnly = true)
    public List<Object[]> artistiPiuPopolari(){
        return artistaRepository.findMostFollowedArtists();
    }

    /*
    @Transactional(readOnly = true)
    public List<Artista> artistiConVotoMedioSuperioreA(double minAvg){
        List< RecensioneAlbum> rec=recensioneAlbumRepository.findByVotoGreaterThan(minAvg);
        Set<RecensioneAlbum> sRec= new HashSet<>(rec);
        Set<Album> album=new HashSet<>(albumRepository.findByRecensioniAlbum(sRec));
        return artistaRepository.findByAlbums(album);
    }

     */

    @Transactional(readOnly = true)
    public List<Artista> artistiPopolariDiGenere(String genere, long minFollowers){
        return artistaRepository.findPopularByGenre(genere,minFollowers);
    }

    @Transactional(readOnly = true)
    public List<Artista> artistiPiuAscoltati(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Artista> pagedResult = (Page<Artista>) artistaRepository.findTopStreamingArtists(paging);
        if (pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = false)
    public Artista aggiungiArtista(Artista artista){
        if (artistaRepository.findByIdAndNome(artista.getId(),artista.getNome())!=null){
            throw new ArtistaGiaPresenteException();
        }
        return artistaRepository.save(artista);
    }

    @Transactional(readOnly = true)
    public List<Artista> trovaArtistiConAlmenoUnGenere(List<String> generi){
        return artistaRepository.findByAnyGenre(generi);
    }

    @Transactional(readOnly = true)
    public List<Artista> trovaArtistiConTuttiIGeneri(List<String> generi, long size){
        return artistaRepository.findByAllGenres(generi, size);
    }

    @Transactional(readOnly = true)
    public List<Artista> artistiConReleaseRecenti(int years){
        return artistaRepository.findActiveInLastYears(years);
    }



}
