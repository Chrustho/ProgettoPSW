package com.example.progettopsw.services;

import com.example.progettopsw.entities.RecensioneAlbum;
import com.example.progettopsw.repositories.AlbumRepository;
import com.example.progettopsw.repositories.RecensioneAlbumRepository;
import com.example.progettopsw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class RecensioneAlbumService {
    @Autowired
    private RecensioneAlbumRepository recensioneAlbumRepository;


    public RecensioneAlbum aggiungiRecensione(RecensioneAlbum recensione) {
        if (recensione.getAlbum() == null || recensione.getUser() == null ||
                recensione.getVoto() == null) {
            throw new IllegalArgumentException("Album, utente e voto sono obbligatori");
        }
        recensione.setDataRecensione(new Date());
        return recensioneAlbumRepository.save(recensione);
    }

    @Transactional(readOnly = true)
    public List<RecensioneAlbum> trovaRecensioniDaAlbumId(Long id){
        return recensioneAlbumRepository.findByAlbumId(id);
    }

    @Transactional(readOnly = true)
    public Double calcolaVotoMedioAlbum(Long albumId) {
        return recensioneAlbumRepository.findAverageRatingByAlbumId(albumId)
                .orElse(0.0);
    }

    @Transactional(readOnly = true)
    public List<RecensioneAlbum> trovaRecensioniDopo(LocalDate data) {
        return recensioneAlbumRepository.findByDataRecensioneAfter(data);
    }

    @Transactional(readOnly = true)
    public List<RecensioneAlbum> trovaRecensioniPerVoto(double minVoto, double maxVoto) {
        return recensioneAlbumRepository.findByVotoBetween(minVoto, maxVoto);
    }

    @Transactional(readOnly = true)
    public List<RecensioneAlbum> trovaRecensioniDettagliateAlbum(Long albumId, int minLunghezza) {
        return recensioneAlbumRepository.findDetailedReviewsByAlbum(albumId, minLunghezza);
    }

    @Transactional(readOnly = true)
    public List<RecensioneAlbum> trovaRecensioniUtente(Long userId) {
        return recensioneAlbumRepository.findUserReviewsOrderByDate(userId);
    }

    @Transactional(readOnly = true)
    public Double calcolaMediaVotiAlbum(Long albumId) {
        return recensioneAlbumRepository.calculateAlbumAverageRating(albumId)
                .orElse(0.0);
    }


    @Transactional(readOnly = true)
    public List<RecensioneAlbum> cercaPerParolaChiave(String keyword) {
        return recensioneAlbumRepository.findByKeywordInText(keyword);
    }


    @Transactional(readOnly = true)
    public List<RecensioneAlbum> trovaRecensioniArtista(Long artistaId) {
        return recensioneAlbumRepository.findByArtistaOrderByDate(artistaId);
    }

}
