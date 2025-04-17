package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Artista;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistaRepository extends JpaRepository<Artista,Long> {

    /**
     * Trova tutti gli artisti il cui nome contiene la stringa passata (case-insensitive).
     */
    List<Artista> findByNomeContainingIgnoreCase(String nome);

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


}

