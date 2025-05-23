package com.example.progettopsw.services;

import com.example.progettopsw.entities.Album;
import com.example.progettopsw.entities.Canzone;
import com.example.progettopsw.repositories.CanzoneRepository;
import com.example.progettopsw.support.exceptions.CanzoneGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CanzoneService {
    @Autowired
    private CanzoneRepository canzoneRepository;

    public Canzone aggiungiCanzone(Canzone canzone) {
        if (canzone.getNome() == null || canzone.getAlbum() == null) {
            throw new IllegalArgumentException("Nome canzone e album sono obbligatori");
        }
        if (canzoneRepository.existsByNomeIgnoreCaseAndAlbumNomeIgnoreCase(
                canzone.getNome(), canzone.getAlbum().getNome())) {
            throw new CanzoneGiaPresenteException("Canzone già presente!");
        }
        return canzoneRepository.save(canzone);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniPiuLunghe() {
        return canzoneRepository.findTop5ByOrderByDurataDesc();
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniPiuCorte() {
        return canzoneRepository.findTop5ByOrderByDurataAsc();
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzonePerNome(String nomeParziale) {
        return canzoneRepository.findByNomeContainingIgnoreCase(nomeParziale);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniConVotoMaggioreDi(double soglia) {
        return canzoneRepository.findByAverageRatingGreaterThan(soglia);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniPerGeneri(List<String> generi, boolean tutti) {
        return tutti
                ? canzoneRepository.findByAllAlbumGenres(generi, (long) generi.size())
                : canzoneRepository.findByAlbumGenres(generi);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniPiuRecensite(long minRecensioni) {
        return canzoneRepository.findByMinReviewCount(minRecensioni);
    }

    @Transactional(readOnly = true)
    public List<Canzone> top10CanzoniPiuAscoltate() {
        return canzoneRepository.findTop10ByOrderByNumeroAscoltiDesc();
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniDiArtista(Long artistaId, int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Canzone> result = canzoneRepository.findByArtistIdOrderByStreamsDesc(artistaId, pageable);
        return result.getContent();
    }


}
