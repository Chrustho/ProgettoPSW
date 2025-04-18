package com.example.progettopsw.services;

import com.example.progettopsw.entities.RecensioneCanzone;
import com.example.progettopsw.entities.User;
import com.example.progettopsw.repositories.RecensioneCanzoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecensioneCanzoneService {
    @Autowired
    private RecensioneCanzoneRepository recensioneCanzoneRepository;

    @Transactional(readOnly = true)
    public Double mediaVotiCanzone(Long canzoneId) {
        return recensioneCanzoneRepository.averageRatingBySong(canzoneId);
    }

    @Transactional(readOnly = true)
    public List<RecensioneCanzone> trovaRecensioniConParolaChiave(String keyword) {
        return recensioneCanzoneRepository.findByTestoContainingIgnoreCase(keyword);
    }

    @Transactional(readOnly = true)
    public List<RecensioneCanzone> trovaUltimeRecensioni() {
        return recensioneCanzoneRepository.findTop10ByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<User> trovaRecensoriCheSeguonoArtista(Long canzoneId){
        return recensioneCanzoneRepository.findReviewersAlsoFollowingArtist(canzoneId);
    }

    @Transactional(readOnly = true)
    public Double recensioniSenzaTesto(){
        return recensioneCanzoneRepository.percentageEmptyFeedback();
    }
}
