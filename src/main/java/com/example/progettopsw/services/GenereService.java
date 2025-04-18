package com.example.progettopsw.services;

import com.example.progettopsw.entities.Genere;
import com.example.progettopsw.repositories.GenereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenereService {
    @Autowired
    private GenereRepository genereRepository;

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriCheInizianoPer(String prefisso){
        return genereRepository.findByNomeStartingWithIgnoreCase(prefisso);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriPopolari(long minAlbums){
        return genereRepository.findPopularGenres(minAlbums);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriConPiuDiXCanzoniPerAlbum(double avg){
        return genereRepository.findGenresWithAverageTracksPerAlbumGreaterThan(avg);
    }

    @Transactional(readOnly = true)
    public List<Genere> trovaGeneriSeguitiIndirettamente(Long userId){
        return genereRepository.findUserFavoriteGenres(userId);
    }
}
