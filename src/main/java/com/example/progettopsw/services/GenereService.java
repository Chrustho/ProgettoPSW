package com.example.progettopsw.services;

import com.example.progettopsw.entities.Genere;
import com.example.progettopsw.repositories.GenereRepository;
import com.example.progettopsw.support.exceptions.GenereGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenereService {
    @Autowired
    private GenereRepository genereRepository;

    public Genere aggiungiGenere(Genere genere) {
        if (genere.getNome() == null) {
            throw new IllegalArgumentException("Nome genere obbligatorio");
        }
        if (genereRepository.existsByNomeIgnoreCase(genere.getNome())) {
            throw new GenereGiaEsistenteException("Genere già presente!");
        }
        return genereRepository.save(genere);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriCheInizianoPer(String prefisso) {
        return genereRepository.findByNomeStartingWithIgnoreCase(prefisso);
    }


    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriPiuAscoltati(long minStreams) {
        return genereRepository.findByTotalStreamsGreaterThan(minStreams);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriMeglioRecensiti(double minAvg) {
        return genereRepository.findByAverageRatingGreaterThan(minAvg);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriPiuSeguiti(long minFollowers) {
        return genereRepository.findByTotalFollowersGreaterThan(minFollowers);
    }


}
