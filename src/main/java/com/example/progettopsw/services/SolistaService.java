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

    public Solista aggiungiSolista(Solista solista) {
        if (solista.getNome() == null || solista.getStrumento() == null) {
            throw new IllegalArgumentException("Nome e strumento sono obbligatori");
        }
        if (solistaRepository.existsByNomeIgnoreCaseAndStrumentoIgnoreCase(
                solista.getNome(), solista.getStrumento())) {
            throw new SolistaGiaPresenteException("Solista già presente");
        }
        return solistaRepository.save(solista);
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistiPerStrumento(String strumento) {
        return solistaRepository.findByStrumentoIgnoreCase(strumento);
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistiPiuAscoltati(long minStreams) {
        return solistaRepository.findByTotalStreamsGreaterThan(minStreams);
    }

    @Transactional(readOnly = true)
    public List<Solista> cercaPerNome(String nome) {
        return solistaRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistiPerGeneri(List<String> generi, boolean tutti) {
        return tutti
                ? solistaRepository.findByAllGenres(generi, (long) generi.size())
                : solistaRepository.findByGenres(generi);
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistiPerMediaVoti(double minAvg) {
        return solistaRepository.findByAverageRatingGreaterThan(minAvg);
    }

    @Transactional(readOnly = true)
    public List<Solista> trovaSolistiPiuSeguiti(int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Solista> result = solistaRepository.findAllOrderByFollowerCount(pageable);
        return result.getContent();
    }



}
