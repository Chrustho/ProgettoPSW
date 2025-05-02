package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BandRepository extends JpaRepository<Band, Long> {

    /*
    * Trova band con Nome
     */
    Band findByNome(String nome);

    /**
     * Band i cui membri suonano uno specifico strumento.
     */
    @Query("SELECT DISTINCT b FROM Band b JOIN b.membri s " +
            "WHERE LOWER(s.strumento) = LOWER(:strumento)")
    List<Band> findByMemberInstrument(@Param("strumento") String strumento);

    // band con rating album combinato >= soglia
    @Query("""
      SELECT b FROM Band b
      JOIN b.albums al
      JOIN al.recensioniAlbum r
      GROUP BY b
      HAVING AVG(r.voto) >= :minAvg
    """)
    List<Band> findHighRatedBands(@Param("minAvg") double minAverage);

    // band con somma stream album > soglia
    @Query("""
      SELECT b FROM Band b
      JOIN b.albums al
      JOIN al.canzoni c
      GROUP BY b
      HAVING SUM(c.numeroAscolti) > :minStreams
    """)
    List<Band> findTopStreamedBands(@Param("minStreams") long minStreams);

    // ————— Cerca band che hanno almeno un genere tra quelli indicati
    @Query("""
      SELECT DISTINCT b
      FROM Band b
      JOIN b.generi g
      WHERE g.nome IN :generi
    """)
    List<Band> findByAnyGenre(@Param("generi") List<String> generi);

    // ————— Cerca band che hanno TUTTI i generi indicati
    @Query("""
      SELECT b
      FROM Band b
      JOIN b.generi g
      WHERE g.nome IN :generi
      GROUP BY b
      HAVING COUNT(DISTINCT g.nome) = :size
    """)
    List<Band> findByAllGenres(@Param("generi") List<String> generi,
                               @Param("size") long size);


    // ————— Band seguite da uno specifico utente
    @Query("""
      SELECT DISTINCT b
      FROM Band b
      JOIN b.follower u
      WHERE u.id = :userId
    """)
    List<Band> findFollowedByUser(@Param("userId") Long userId);

}
