package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Album;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    /**
     * Trova gli album di un artista specifico.
     */
    List<Album> findByArtistaNomeIgnoreCaseAndNome(String artistaNome, String nome);

    boolean existsAlbumByArtistaNomeIgnoreCaseAndNome(String artistaNome, String nome);

    /**
     * Album con media voto delle recensioni superiore alla soglia.
     */
    @Query("SELECT al FROM Album al JOIN al.recensioniAlbum r " +
            "GROUP BY al HAVING AVG(r.voto) > :soglia")
    List<Album> findTopRated(@Param("soglia") double minAverage);

    /**
     * Album che nessun utente ha ancora aggiunto alla lista "da ascoltare".
     */
    @Query("SELECT al FROM Album al WHERE al.utentiDaAscoltare IS EMPTY")
    List<Album> findUnqueuedAlbums();

    // album con generi multipli
    @Query("""
      SELECT al FROM Album al
      JOIN al.generi g
      GROUP BY al
      HAVING COUNT(g) > :minGenres
    """)
    List<Album> findAlbumsWithMultipleGenres(@Param("minGenres") long minGenres);

    // album pìù aggiunti più volte nella wishlist (albumPreferiti)
    @Query("""
      SELECT al FROM Album al
      JOIN al.utentiPreferiti u
      GROUP BY al
      ORDER BY COUNT(u) DESC
    """)
    List<Album> findMostWishlistedAlbums(Pageable pageable);
}
