package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Genere;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenereRepository extends CrudRepository<Genere, Long> {

    /**
     * Restituisce generi il cui nome inizia con il prefisso dato.
     */
    List<Genere> findByNomeStartingWithIgnoreCase(String prefix);

    /**
     * Generi associati a più di M album.
     */
    @Query("SELECT g FROM Genere g JOIN g.albums a GROUP BY g HAVING COUNT(a) > :m")
    List<Genere> findPopularGenres(@Param("m") long minAlbums);

    /**
     * Restituisce generi non ancora seguiti da alcun utente.
     */
    @Query("SELECT g FROM Genere g WHERE g.albums IS EMPTY AND g.artisti IS EMPTY")
    List<Genere> findUnreferencedGenres();


    // generi con numero medio di canzoni per album > soglia
    @Query("""
      SELECT g FROM Genere g
      JOIN g.albums al
      JOIN al.canzoni c
      GROUP BY g
      HAVING (COUNT(c) * 1.0) / COUNT(al) > :avgTracks
    """)
    List<Genere> findGenresWithAverageTracksPerAlbumGreaterThan(@Param("avgTracks") double avgTracks);

    // generi seguiti indirettamente (album preferiti di user)
    @Query("""
      SELECT DISTINCT g FROM Genere g
      JOIN g.albums al
      JOIN al.utentiPreferiti u
      WHERE u.id = :userId
    """)
    List<Genere> findUserFavoriteGenres(@Param("userId") Long userId);
}