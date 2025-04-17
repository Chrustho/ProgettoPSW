package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.RecensioneAlbum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RecensioneAlbumRepository extends CrudRepository<RecensioneAlbum, Long> {

    /**
     * Recensioni di album fatte dopo la data specificata.
     */
    List<RecensioneAlbum> findByDataRecensioneAfter(LocalDate date);

    /**
     * Recensioni con voto tra min e max.
     */
    List<RecensioneAlbum> findByVotoBetween(double min, double max);

    /**
     * Recensioni per un dato album, ordinate per data decrescente.
     */
    List<RecensioneAlbum> findByAlbumIdOrderByDataRecensioneDesc(Long albumId);

    // conteggia parole totali in tutte le recensioni di un album
    @Query("""
      SELECT SUM(LENGTH(r.testo) - LENGTH(REPLACE(r.testo, ' ', '')) + 1)
      FROM RecensioneAlbum r
      WHERE r.album.id = :albumId
    """)
    Long countTotalWordsByAlbum(@Param("albumId") Long albumId);

    // recensioni di album con sentiment positivo (voto >=7)
    List<RecensioneAlbum> findByVotoGreaterThanEqual(double threshold);
}
