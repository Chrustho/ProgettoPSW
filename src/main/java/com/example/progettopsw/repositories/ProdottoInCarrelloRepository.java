package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Album;
import com.example.progettopsw.entities.ProdottoInCarrello;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoInCarrelloRepository extends JpaRepository<ProdottoInCarrello, Long> {
}
