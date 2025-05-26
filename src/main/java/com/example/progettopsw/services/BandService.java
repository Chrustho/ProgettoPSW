package com.example.progettopsw.services;

import com.example.progettopsw.entities.Band;
import com.example.progettopsw.repositories.BandRepository;
import com.example.progettopsw.support.exceptions.ArtistaGiaPresenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BandService {
    @Autowired
    private BandRepository bandRepository;

    @Transactional(readOnly = true)
    public List<Band> trovaBandConVotoAlbum(double minAvg) {
        return bandRepository.findByAverageAlbumRatingGreaterThan(minAvg);
    }

    @Transactional(readOnly = true)
    public List<Band> trovaBandPiuAscoltate(long minStreams) {
        return bandRepository.findByTotalStreamsGreaterThan(minStreams);
    }

    @Transactional(readOnly = true)
    public List<Band> trovaBandConAlmenoUnGenere(List<String> generi) {
        return bandRepository.findByGeneriNomeIn(generi);
    }

    @Transactional(readOnly = true)
    public List<Band> trovaBandConTuttiIGeneri(List<String> generi) {
        return bandRepository.findByAllGenres(generi, (long) generi.size());
    }


    @Transactional
    public Band aggiungiBand(Band band) {
        if (band.getNome() == null) {
            throw new IllegalArgumentException("Nome band obbligatorio");
        }
        if (bandRepository.existsByNomeIgnoreCase(band.getNome())) {
            throw new ArtistaGiaPresenteException();
        }
        return bandRepository.save(band);
    }

}
