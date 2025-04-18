package com.example.progettopsw.services;

import com.example.progettopsw.entities.Solista;
import com.example.progettopsw.repositories.SolistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolistaService {
    @Autowired
    private SolistaRepository solistaRepository;

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistiCheSuonano(String stumento){
        return solistaRepository.findByStrumentoIgnoreCase(stumento);
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaMusicistiSenzaBand(){
        return solistaRepository.findMusicistiSenzaBand();
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistiConPiuDiNAlbumRegistrati(long n){
        return solistaRepository.findArtistiConPiuDiNAlbum(n);
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistaConPiuDiNStream(long minStreams){
        return solistaRepository.findHighStreamingSolisti(minStreams);
    }

    @Transactional(readOnly = true)
    public List<String> trovaStrumentiUsatiInBandGrandi(int min){
        return solistaRepository.findInstrumentsInLargeBands(min);
    }

}
