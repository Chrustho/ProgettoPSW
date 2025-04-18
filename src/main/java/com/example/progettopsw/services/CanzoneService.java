package com.example.progettopsw.services;

import com.example.progettopsw.entities.Canzone;
import com.example.progettopsw.repositories.CanzoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CanzoneService {
    @Autowired
    private CanzoneRepository canzoneRepository;

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniConAscoltiMaggioriDi(long soglia){
        return canzoneRepository.findByNumeroAscoltiGreaterThan(soglia);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniPiuLunghe(){
        return canzoneRepository.findTop5ByOrderByDurataDesc();
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoneConXNelNome(String nomeParziale){
        return canzoneRepository.findByNomeContainingIgnoreCase(nomeParziale);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniConVotoMaggioreDi(double soglia){
        return canzoneRepository.findTopRatedSongs(soglia);
    }

    @Transactional(readOnly = true)
    public List<Canzone> trovaCanzoniNonInWishlist(Long userId){
        return canzoneRepository.findUnqueuedSongsForUser(userId);
    }
}
