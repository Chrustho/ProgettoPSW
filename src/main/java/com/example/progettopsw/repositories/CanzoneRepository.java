package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Canzone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CanzoneRepository extends JpaRepository<Canzone, Long> {


    Boolean existsByNomeEqualsIgnoreCaseAndAlbum_Nome(String nome, String albumNome);

    /**
     * Le 5 canzoni più lunghe (durata maggiore).
     */
    List<Canzone> findTop5ByOrderByDurataDesc();

    /** 5 canzoni più corte (durata minore). */
    List<Canzone> findTop5ByOrderByDurataAsc();

    /** Top 10 canzoni in assoluto per numero di ascolti. */
    List<Canzone> findTop10ByOrderByNumeroAscoltiDesc();

    /**
     * Canzoni il cui nome contiene la stringa fornita.
     */
    List<Canzone> findByNomeContainingIgnoreCase(String partial);

    // brani con voto medio delle recensioni > soglia
    @Query("""
      SELECT c FROM Canzone c
      JOIN c.recensioni r
      GROUP BY c
      HAVING AVG(r.voto) > :minAvg
    """)
    List<Canzone> findTopRatedSongs(@Param("minAvg") double minAverage);

    /** Canzoni il cui album ha almeno uno dei generi indicati. */
    @Query("""
      SELECT DISTINCT c
      FROM Canzone c
      JOIN c.album.generi g
      WHERE g.nome IN :generi
    """)
    List<Canzone> findByAnyAlbumGenre(@Param("generi") List<String> generi);

    /** Canzoni il cui album copre tutti i generi indicati. */
    @Query("""
      SELECT c
      FROM Canzone c
      JOIN c.album.generi g
      WHERE g.nome IN :generi
      GROUP BY c
      HAVING COUNT(DISTINCT g.nome) = :size
    """)
    List<Canzone> findByAllAlbumGenres(
            @Param("generi") List<String> generi,
            @Param("size") long size);

    /** Canzoni con almeno `minReviews` recensioni. */
    @Query("""
      SELECT c
      FROM Canzone c
      JOIN c.recensioni r
      GROUP BY c
      HAVING COUNT(r) >= :minReviews
    """)
    List<Canzone> findPopularSongsByReviewCount(@Param("minReviews") long minReviews);


    /** Ricerca per parola-chiave su titolo e nome artista. */
    @Query("""
      SELECT c
      FROM Canzone c
      WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :kw, '%'))
         OR LOWER(c.album.artista.nome) LIKE LOWER(CONCAT('%', :kw, '%'))
    """)
    List<Canzone> searchByTitleOrArtist(@Param("kw") String keyword);

    /** Top N canzoni con più ascolti di un dato artista. */
    @Query("""
      SELECT c
      FROM Canzone c
      WHERE c.album.artista.id = :artistId
      ORDER BY c.numeroAscolti DESC
    """)
    Page<Canzone> findTopByArtistOrderByNumeroAscoltiDesc(
            @Param("artistId") Long artistId, Pageable topN);
}