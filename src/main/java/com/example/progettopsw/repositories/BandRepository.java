package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BandRepository extends JpaRepository<Band, Long> {

    /**
     * Trova band con almeno N membri.
     */
    @Query("SELECT b FROM Band b WHERE SIZE(b.membri) >= :minMembers")
    List<Band> findByMinMemberCount(@Param("minMembers") int minMembers);

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


}
