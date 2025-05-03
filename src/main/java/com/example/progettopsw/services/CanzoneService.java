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

    @Transactional(readOnly = false)
    public void aggiungiCanzone(Canzone canzone) {
        if (canzone.getNome() != null && canzone.getAlbum() != null) {
            if (canzoneRepository.existsByNomeEqualsIgnoreCaseAndAlbum_Nome(canzone.getNome(), canzone.getAlbum().getNome())) {
                throw new CanzoneGiaPresenteException("Canzone già presente!");
            } else {
                canzoneRepository.save(canzone);
            }
        }
    }


    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniPiuLunghe() {
        return canzoneRepository.findTop5ByOrderByDurataDesc();
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoneConXNelNome(String nomeParziale) {
        return canzoneRepository.findByNomeContainingIgnoreCase(nomeParziale);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniConVotoMaggioreDi(double soglia) {
        return canzoneRepository.findTopRatedSongs(soglia);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaAlbumConAlmenoUnGenereSpecificato(List<String> generi){
        return canzoneRepository.findByAnyAlbumGenre(generi);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaAlbumConTuttiIGeneriSpecificati(List<String> generi){
        return canzoneRepository.findByAllAlbumGenres(generi,generi.size());
    }

    @Transactional(readOnly = true)
    public List<Canzone> canzoniPiuRecensite(long minReviews){
        return canzoneRepository.findPopularSongsByReviewCount(minReviews);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaPerTitoloOArtista(String keyword){
        return canzoneRepository.searchByTitleOrArtist(keyword);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniPiuCorte(){
        return canzoneRepository.findTop5ByOrderByDurataAsc();
    }

    @Transactional(readOnly = true)
    public List<Canzone> top10CanzoniPiuAscoltate(){
        return canzoneRepository.findTop10ByOrderByNumeroAscoltiDesc();
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaNCanzoniPiuAscoltatiDiArtista(Long artistaId, int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Canzone> pagedResult = canzoneRepository.findTopByArtistOrderByNumeroAscoltiDesc(artistaId, paging);
        if (pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }


}
