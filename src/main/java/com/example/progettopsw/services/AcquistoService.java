package com.example.progettopsw.services;

import com.example.progettopsw.entities.Acquisto;
import com.example.progettopsw.entities.Album;
import com.example.progettopsw.entities.ProdottoInCarrello;
import com.example.progettopsw.entities.Users;
import com.example.progettopsw.repositories.AcquistoRepository;
import com.example.progettopsw.repositories.ProdottoInCarrelloRepository;
import com.example.progettopsw.repositories.UserRepository;
import com.example.progettopsw.support.exceptions.QuantitaNonDisponibileException;
import com.example.progettopsw.support.exceptions.UtenteNonTrovatoException;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AcquistoService {
    @Autowired
    private AcquistoRepository acquistoRepository;
    @Autowired
    private ProdottoInCarrelloRepository prodottoInCarrelloRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Transactional(readOnly = false)
    public Acquisto aggiungiAcquisto(Acquisto acquisto) throws QuantitaNonDisponibileException {
        Acquisto risultato=acquistoRepository.save(acquisto);
        for (ProdottoInCarrello pic: risultato.getCarrello()){
            pic.setAcquisto(risultato);
            ProdottoInCarrello aggiunta=prodottoInCarrelloRepository.save(pic);
            entityManager.refresh(aggiunta);
            Album album=aggiunta.getAlbum();
            int nuovaQuantita=album.getQuantity()-pic.getQuantity();
            if (nuovaQuantita<0){
                throw new QuantitaNonDisponibileException();
            }
            album.setQuantity(nuovaQuantita);
            entityManager.refresh(pic);
        }
        entityManager.refresh(risultato);
        return risultato;
    }

    @Transactional(readOnly = true)
    public List<Acquisto> prendiAcquistiDaUtente(Users user) throws UtenteNonTrovatoException {
        if (!userRepository.existsById(user.getId())){
            throw new UtenteNonTrovatoException();
        }
        return acquistoRepository.findByBuyer(user);
    }


}
