package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Genere;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenereRepository extends JpaRepository<Genere, Long> {

    Genere getGenereById(Long id);


    Boolean findByNomeIgnoreCase(String nome);
    /**
     * Restituisce generi il cui nome inizia con il prefisso dato.
     */
    List<Genere> findByNomeStartingWithIgnoreCase(String prefix);

    /**
     * Generi associati a più di M album.
     */
    @Query("SELECT g FROM Genere g JOIN g.albums a GROUP BY g HAVING COUNT(a) > :m")
    List<Genere> findPopularGenres(@Param("m") long minAlbums);


    // generi seguiti indirettamente (album preferiti di user)
    @Query("""
      SELECT DISTINCT g FROM Genere g
      JOIN g.albums al
      JOIN al.utentiPreferiti u
      WHERE u.id = :userId
    """)
    List<Genere> findUserFavoriteGenres(@Param("userId") Long userId);

    /**
     * Trova generi il cui totale di ascolti su tutte le canzoni degli album
     * supera una certa soglia.
     */
    @Query("""
      SELECT g
      FROM Genere g
      JOIN g.albums al
      JOIN al.canzoni c
      GROUP BY g
      HAVING SUM(c.numeroAscolti) > :minStreams
    """)
    List<Genere> findGenresWithTotalStreamsGreaterThan(@Param("minStreams") long minStreams);

    /**
     * Trova generi con album mediamente ben recensiti (voto medio > soglia).
     */
    @Query("""
      SELECT g
      FROM Genere g
      JOIN g.albums al
      JOIN al.recensioniAlbum r
      GROUP BY g
      HAVING AVG(r.voto) > :minAvg
    """)
    List<Genere> findGenresWithHighRatedAlbums(@Param("minAvg") double minAverage);

    /**
     * Trova generi i cui artisti associati hanno complessivamente
     * più di N follower.
     */
    @Query("""
      SELECT g
      FROM Genere g
      JOIN g.artisti a
      JOIN a.follower u
      GROUP BY g
      HAVING COUNT(u) > :minFollowers
    """)
    List<Genere> findGenresByArtistFollowersGreaterThan(@Param("minFollowers") long minFollowers);

    /**
     * Trova generi che co-appaiono insieme a un dato genere
     * negli stessi album (utile per suggerimenti “simili”).
     */
    @Query("""
      SELECT DISTINCT g2
      FROM Genere g1
      JOIN g1.albums al
      JOIN al.generi g2
      WHERE g1.nome = :genreName
        AND g2.nome <> :genreName
    """)
    List<Genere> findCooccurringGenres(@Param("genreName") String genreName);

    /**
     * Top N generi ordinati per somma di stream, paginati.
     */
    @Query("""
      SELECT g
      FROM Genere g
      JOIN g.albums al
      JOIN al.canzoni c
      GROUP BY g
      ORDER BY SUM(c.numeroAscolti) DESC
    """)
    List<Genere> findTopGenresByStreams(Pageable topN);

    /**
            * Generi con almeno M artisti associati.
            */
    @Query("""
      SELECT g
      FROM Genere g
      JOIN g.artisti a
      GROUP BY g
      HAVING COUNT(a) >= :minArtists
    """)
    List<Genere> findGenresWithAtLeastArtistCount(@Param("minArtists") long minArtists);

}