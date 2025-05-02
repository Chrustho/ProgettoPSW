package com.example.progettopsw.services;

import com.example.progettopsw.entities.Canzone;
import com.example.progettopsw.entities.Solista;
import com.example.progettopsw.repositories.SolistaRepository;
import com.example.progettopsw.support.exceptions.SolistaGiaPresenteException;
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
    public List<Solista> cercaPerNome(String nome){
        return solistaRepository.findByNomeIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistaConAlmenoUnGenereSpecificato(List<String> generi){
        return solistaRepository.findByAnyGenre(generi);
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistaConTuttiIGeneri(List<String> generi){
        return solistaRepository.findByAllGenres(generi, generi.size());
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistaConVotiMAggioriDi(double minAvg){
        return solistaRepository.findByAverageAlbumRatingGreaterThan(minAvg);
    }

    @Transactional(readOnly = false)
    public void aggiungiSolista(Solista solista){
        if (solistaRepository.existsByNomeEqualsIgnoreCaseAndStrumentoEqualsIgnoreCase(solista.getNome(),solista.getStrumento())){
            throw new SolistaGiaPresenteException("Solista già presente");
        }
        solistaRepository.save(solista);
    }

    @Transactional(readOnly = true)
    public List<Solista> topSolistiPerNumeroDiFollower( int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Solista> pagedResult = solistaRepository.findTopByFollowerCount(paging);
        if (pagedResult.hasContent()){
            return pagedResult.getContent();
        }else {
            return new ArrayList<>();
        }
    }


}
