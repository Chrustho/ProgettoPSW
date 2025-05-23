package com.example.progettopsw.services;

import com.example.progettopsw.entities.RecensioneCanzone;
import com.example.progettopsw.entities.Users;
import com.example.progettopsw.repositories.CanzoneRepository;
import com.example.progettopsw.repositories.RecensioneCanzoneRepository;
import com.example.progettopsw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class RecensioneCanzoneService {
    @Autowired
    private RecensioneCanzoneRepository recensioneCanzoneRepository;

    @Autowired
    private CanzoneRepository canzoneRepository;

    @Autowired
    private UserRepository userRepository;

    public RecensioneCanzone aggiungiRecensione(RecensioneCanzone recensione) {
        if (recensione.getCanzone() == null || recensione.getUser() == null ||
                recensione.getVoto() == null) {
            throw new IllegalArgumentException("Canzone, utente e voto sono obbligatori");
        }
        recensione.setDataRecensione(new Date());
        return recensioneCanzoneRepository.save(recensione);
    }

    @Transactional(readOnly = true)
    public List<RecensioneCanzone> trovaRecensioniDopo(LocalDate data) {
        return recensioneCanzoneRepository.findByDataRecensioneAfter(data);
    }

    @Transactional(readOnly = true)
    public List<RecensioneCanzone> trovaRecensioniPerVoto(double minVoto, double maxVoto) {
        return recensioneCanzoneRepository.findByVotoBetween(minVoto, maxVoto);
    }

    @Transactional(readOnly = true)
    public List<RecensioneCanzone> trovaRecensioniDettagliateCanzone(Long canzoneId, int minLunghezza) {
        return recensioneCanzoneRepository.findDetailedReviewsBySong(canzoneId, minLunghezza);
    }

    @Transactional(readOnly = true)
    public List<RecensioneCanzone> trovaRecensioniUtente(Long userId) {
        return recensioneCanzoneRepository.findUserReviewsOrderByDate(userId);
    }

    @Transactional(readOnly = true)
    public Double calcolaMediaVotiCanzone(Long canzoneId) {
        return recensioneCanzoneRepository.calculateSongAverageRating(canzoneId)
                .orElse(0.0);
    }



    @Transactional(readOnly = true)
    public List<RecensioneCanzone> cercaPerParolaChiave(String keyword) {
        return recensioneCanzoneRepository.findByKeywordInText(keyword);
    }

    @Transactional(readOnly = true)
    public List<RecensioneCanzone> trovaRecensioniArtista(Long artistaId) {
        return recensioneCanzoneRepository.findByArtistaOrderByDate(artistaId);
    }

    @Transactional(readOnly = true)
    public List<RecensioneCanzone> trovaRecensioniAlbum(Long albumId) {
        return recensioneCanzoneRepository.findByAlbumOrderByDate(albumId);
    }


}
