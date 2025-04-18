package com.example.progettopsw.services;

import com.example.progettopsw.entities.RecensioneAlbum;
import com.example.progettopsw.repositories.RecensioneAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecensioneAlbumService {
    @Autowired
    private RecensioneAlbumRepository recensioneAlbumRepository;

    @Transactional(readOnly = true)
    public List<RecensioneAlbum> trovaRecensioniAlbumDopo(LocalDate data){
        return recensioneAlbumRepository.findByDataRecensioneAfter(data);
    }

    @Transactional(readOnly = true)
    public List<RecensioneAlbum> trovaRecensioniConVotoTra(double min, double max){
        return recensioneAlbumRepository.findByVotoBetween(min,max);
    }

    @Transactional(readOnly = true)
    public Long conteggioParoleRecensione(Long albumId){
        return recensioneAlbumRepository.countTotalWordsByAlbum(albumId);
    }

    @Transactional(readOnly = true)
    public List<RecensioneAlbum> trovaRecensioniConVotoSuperioreA(double threshold){
        return recensioneAlbumRepository.findByVotoGreaterThanEqual(threshold);
    }
}
