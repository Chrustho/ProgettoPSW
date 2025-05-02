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

    @Transactional(readOnly = false)
    public Genere aggiungiGenere(Genere genere){
        if (genereRepository.findByNomeIgnoreCase(genere.getNome())){
            throw new GenereGiaEsistenteException("Genere già esistente");
        }
        return genereRepository.save(genere);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriCheInizianoPer(String prefisso){
        return genereRepository.findByNomeStartingWithIgnoreCase(prefisso);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriPopolari(long minAlbums){
        return genereRepository.findPopularGenres(minAlbums);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriSeguitiIndirettamente(Long userId){
        return genereRepository.findUserFavoriteGenres(userId);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriPiuAscoltati(long minStreams){
        return genereRepository.findGenresWithTotalStreamsGreaterThan(minStreams);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriMeglioRecensiti(double minAvg){
        return genereRepository.findGenresWithHighRatedAlbums(minAvg);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriPiuSeguiti(long minFollowers){
        return genereRepository.findGenresByArtistFollowersGreaterThan(minFollowers);
    }

    @Transactional(readOnly = true)
    public List<Genere> generiConsigliati(String genreName){
        return genereRepository.findCooccurringGenres(genreName);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriPiuSuonati(long minArtisti){
        return genereRepository.findGenresWithAtLeastArtistCount(minArtisti);
    }
}
