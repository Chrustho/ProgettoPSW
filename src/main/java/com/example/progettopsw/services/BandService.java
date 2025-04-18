package com.example.progettopsw.services;

import com.example.progettopsw.entities.Band;
import com.example.progettopsw.repositories.BandRepository;
import com.example.progettopsw.support.exceptions.ArtistaGiaPresente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BandService {
    @Autowired
    private BandRepository bandRepository;

    @Transactional(readOnly = true)
    public List<Band> trovaBandConAlmenoNMembri(int min){
        return bandRepository.findByMinMemberCount(min);
    }

    @Transactional(readOnly = true)
    public List<Band> trovaBandConVotoAlbum(double minAvg){
        return bandRepository.findHighRatedBands(minAvg);
    }

    @Transactional(readOnly = true)
    public List<Band> trovaBandPiuAscoltate(long minStreams){
        return bandRepository.findTopStreamedBands(minStreams);
    }


    @Transactional(readOnly = false)
    public Band aggiungiBand(Band band){
        if (bandRepository.findByNome(band.getNome())!=null){
            throw new ArtistaGiaPresente();
        }
        return bandRepository.save(band);
    }
}
