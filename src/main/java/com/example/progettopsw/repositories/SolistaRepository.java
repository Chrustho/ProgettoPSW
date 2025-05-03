package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Solista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolistaRepository extends JpaRepository<Solista, Long> {

    Boolean existsByNomeEqualsIgnoreCaseAndStrumentoEqualsIgnoreCase(String nome, String strumento);
    
    List<Solista> findByNomeIgnoreCase(String nome);
    /**
     * Ricerca solisti che suonano lo strumento indicato.
     */
    List<Solista> findByStrumentoIgnoreCase(String strumento);


    // solisti con totale stream > soglia
    @Query("""
      SELECT s FROM Solista s
      JOIN s.albums al
      JOIN al.canzoni c
      GROUP BY s
      HAVING SUM(c.numeroAscolti) > :minStreams
    """)
    List<Solista> findHighStreamingSolisti(@Param("minStreams") long minStreams);

    /**
     * 1. Trova solisti che appartengono ad almeno uno dei generi indicati.
     */
    @Query("""
      SELECT DISTINCT s
      FROM Solista s
      JOIN s.generi g
      WHERE g.nome IN :generi
    """)
    List<Solista> findByAnyGenre(@Param("generi") List<String> generi);

    /**
     * 2. Trova solisti i cui generi includono tutti quelli indicati.
     */
    @Query("""
      SELECT s
      FROM Solista s
      JOIN s.generi g
      WHERE g.nome IN :generi
      GROUP BY s
      HAVING COUNT(DISTINCT g.nome) = :size
    """)
    List<Solista> findByAllGenres(@Param("generi") List<String> generi,
                                  @Param("size") long size);

    /**
     * 3. Trova solisti con media voto degli album >= soglia.
     */
    @Query("""
      SELECT s
      FROM Solista s
      JOIN s.albums al
      JOIN al.recensioniAlbum r
      GROUP BY s
      HAVING AVG(r.voto) >= :minAvg
    """)
    List<Solista> findByAverageAlbumRatingGreaterThan(@Param("minAvg") double minAverage);

    /**
     * 5. Trova top N solisti per numero di follower.
     */
    @Query("""
      SELECT s
      FROM Solista s
      LEFT JOIN s.follower u
      GROUP BY s
      ORDER BY COUNT(u) DESC
    """)
    Page<Solista> findTopByFollowerCount(Pageable topN);


}

