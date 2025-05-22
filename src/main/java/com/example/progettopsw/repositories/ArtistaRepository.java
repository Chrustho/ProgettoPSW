package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Artista;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistaRepository extends JpaRepository<Artista,Long> {

    Artista getArtistaById(Long id);

    /**
     * Trova tutti gli artisti il cui nome contiene la stringa passata (case-insensitive).
     */
    List<Artista> findByNomeContainingIgnoreCase(String nome);

    /**
     * Trova il singolo artista con quell'id e nome
     */
    Artista findByIdAndNome(Long id, String nome);

    /**
     * Trova artisti che hanno pubblicato almeno un album nell'intervallo di anni indicato.
     */
    @Query("SELECT DISTINCT a FROM Artista a JOIN a.albums al " +
            "WHERE al.annoRilascio BETWEEN :from AND :to")
    List<Artista> findByAlbumReleaseYearRange(@Param("from") int fromYear,
                                              @Param("to")   int toYear);

    /**
     * Conta quanti follower ha ciascun artista, ordinando dal più popolare.
     * Restituisce lista di array [Artista, Long followerCount]
     */
    @Query("SELECT a, COUNT(u) FROM Artista a LEFT JOIN a.follower u " +
            "GROUP BY a ORDER BY COUNT(u) DESC")
    List<Object[]> findMostFollowedArtists();


    // trova artisti con media voto album >= soglia
    @Query("""
      SELECT a FROM Artista a
      JOIN a.albums al
      JOIN al.recensioniAlbum r
      GROUP BY a
      HAVING AVG(r.voto) >= :minAvg
    """)
    List<Artista> findArtistsWithAverageAlbumRating(@Param("minAvg") double minAverage);



    // trova artisti di un genere con almeno N follower
    @Query("""
      SELECT a FROM Artista a
      JOIN a.generi g
      JOIN a.follower f
      WHERE g.nome = :genreName
      GROUP BY a
      HAVING COUNT(f) >= :minFollowers
    """)
    List<Artista> findPopularByGenre(@Param("genreName") String genre,
                                     @Param("minFollowers") long minFollowers);

    // trova artisti con più stream totali (somma ascolti canzoni)
    @Query("""
      SELECT a FROM Artista a
      JOIN a.albums al
      JOIN al.canzoni c
      GROUP BY a
      ORDER BY SUM(c.numeroAscolti) DESC
    """)
    List<Artista> findTopStreamingArtists(Pageable pageable);

    // ————— Cerca artisti che abbiano almeno un genere tra quelli indicati
    @Query("""
      SELECT DISTINCT a
      FROM Artista a
      JOIN a.generi g
      WHERE g.nome IN :generi
    """)
    List<Artista> findByAnyGenre(@Param("generi") List<String> generi);
    /** Artisti con almeno un genere in `generi`. */


    // ————— Cerca artisti che abbiano TUTTI i generi indicati
    @Query("""
      SELECT a
      FROM Artista a
      JOIN a.generi g
      WHERE g.nome IN :generi
      GROUP BY a
      HAVING COUNT(DISTINCT g.nome) = :size
    """)
    List<Artista> findByAllGenres(@Param("generi") List<String> generi,
                                  @Param("size") long size);
    /** Artisti i cui generi includono tutti i nomi passati. */


    // ————— Artisti attivi negli ultimi N anni
    @Query("""
      SELECT DISTINCT a
      FROM Artista a
      JOIN a.albums al
      WHERE al.annoRilascio >= YEAR(CURRENT_DATE) - :years
    """)
    List<Artista> findActiveInLastYears(@Param("years") int years);
    /** Artisti che hanno pubblicato almeno un album negli ultimi `years` anni. */


}

