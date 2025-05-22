package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.RecensioneCanzone;
import com.example.progettopsw.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecensioneCanzoneRepository extends JpaRepository<RecensioneCanzone, Long> {

    RecensioneCanzone getRecensioneCanzoneById(Long id);

    /**
     * Media voti per una canzone.
     */
    @Query("SELECT AVG(rc.voto) FROM RecensioneCanzone rc WHERE rc.canzone.id = :cid")
    Double averageRatingBySong(@Param("cid") Long canzoneId);

    /**
     * Recensioni contenenti una parola chiave.
     */
    List<RecensioneCanzone> findByTestoContainingIgnoreCase(String keyword);

    /**
     * Ultime N recensioni di canzoni, ordinate per id decrescente.
     */
    List<RecensioneCanzone> findTop10ByOrderByIdDesc();

    // utenti che hanno recensito una specifica canzone e la seguono
    @Query("""
      SELECT rc.user FROM RecensioneCanzone rc
      WHERE rc.canzone.id = :cid
        AND rc.user MEMBER OF rc.canzone.album.artista.follower
    """)
    List<Users> findReviewersAlsoFollowingArtist(@Param("cid") Long canzoneId);

    // feedback rapido: percentuale di recensioni senza testo
    @Query("""
      SELECT (SUM(CASE WHEN r.testo IS NULL OR r.testo = '' THEN 1 ELSE 0 END) * 100.0) / COUNT(r)
      FROM RecensioneCanzone r
    """)
    Double percentageEmptyFeedback();
}
