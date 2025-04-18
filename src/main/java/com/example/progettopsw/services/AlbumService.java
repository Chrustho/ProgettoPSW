package com.example.progettopsw.services;

import com.example.progettopsw.entities.Album;
import com.example.progettopsw.repositories.AlbumRepository;
import com.example.progettopsw.support.exceptions.AlbumGiaPresenteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;


    @Transactional(readOnly = true)
    public List<Album> mostraTuttiGliAlbum(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Album> pagedResult = albumRepository.findAll(paging);
        if (pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Album> trovaAlbumTramiteArtistaENome(String nomeArtista, String nome){
        return albumRepository.findByArtistaNomeIgnoreCaseAndNome(nomeArtista,nome);
    }

    @Transactional(readOnly = true)
    public List<Album> albumConVotoMedioPiuAltoDi(double minAverage){
        return albumRepository.findTopRated(minAverage);
    }

    @Transactional(readOnly = true)
    public List<Album> albumNonAncoraMessiInDaAscoltare(){
        return albumRepository.findUnqueuedAlbums();
    }

    @Transactional(readOnly = true)
    public List<Album> trovaAlbumConAlmenoNGeneri(long minGenres){
        return albumRepository.findAlbumsWithMultipleGenres(minGenres);
    }

    @Transactional(readOnly = true)
    public List<Album> mostraAlbumPiuDesiderati(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Album> pagedResult = (Page<Album>) albumRepository.findMostWishlistedAlbums(paging);
        if (pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = false)
    public void aggiungiAlbum(Album album){
        if (album.getNome() != null && album.getArtista() != null){
            if (albumRepository.existsAlbumByArtistaNomeIgnoreCaseAndNome(album.getArtista().getNome(),album.getNome())) {
                throw new AlbumGiaPresenteException("Album già presente in memoria");
            }else {
                albumRepository.save(album);
            }
        }
    }







}
