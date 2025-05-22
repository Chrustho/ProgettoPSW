package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Album;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    Album getAlbumById(Long id);

    /**
     * Trova gli album di un artista specifico.
     */
    List<Album> findByArtistaNomeIgnoreCaseAndNome(String artistaNome, String nome);

    boolean existsAlbumByArtistaNomeIgnoreCaseAndNome(String artistaNome, String nome);

    /**
     * Album con media voto delle recensioni superiore alla soglia.
     */
    @Query("""
  SELECT al
    FROM Album al
    LEFT JOIN al.recensioniAlbum r
  GROUP BY al
  HAVING COALESCE(AVG(r.voto), 0) >= :soglia
""")
    List<Album> findTopRated(@Param("soglia") double minAverage);

    /**
     * Album che nessun utente ha ancora aggiunto alla lista "da ascoltare".
     */
    @Query("SELECT al FROM Album al WHERE al.utentiDaAscoltare IS EMPTY")
    List<Album> findUnqueuedAlbums();


    // album pìù aggiunti più volte nella wishlist (albumPreferiti)
    @Query("""
      SELECT al FROM Album al
      LEFT JOIN al.utentiPreferiti u
      GROUP BY al
      ORDER BY COUNT(u) DESC
    """)
    List<Album> findMostWishlistedAlbums(Pageable pageable);


    // album con almeno un genere tra quelli posti in input
    @Query("""
    SELECT DISTINCT al FROM Album al
    JOIN al.generi g
    WHERE g.nome IN :generi
    """)
    List<Album> findAlbumsByAnyGenre(@Param("generi") List<String> generi);

    // album con TUTTI i generi passati in input

    @Query("""
    SELECT al FROM Album al
    JOIN al.generi g
    WHERE g.nome IN :generi
    GROUP BY al
    HAVING COUNT(DISTINCT g.nome) = :size
    """)
    List<Album> findAlbumsByAllGenres(@Param("generi") List<String> generi, @Param("size") long size);


    // migliori album di un dato genere

    @Query("""
    SELECT al FROM Album al
    JOIN al.generi g
    LEFT JOIN al.recensioniAlbum r
    WHERE g.nome = :genere
    GROUP BY al
    HAVING COALESCE(AVG(r.voto), 0) >= :soglia
    """)
    List<Album> findTopRatedAlbumsByGenres(@Param("genere") String genere, @Param("soglia") double soglia);


}
