package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Canzone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CanzoneRepository extends CrudRepository<Canzone, Long> {

    /**
     * Trova canzoni con numero di ascolti superiore a X.
     */
    List<Canzone> findByNumeroAscoltiGreaterThan(long soglia);

    /**
     * Le 5 canzoni più lunghe (durata maggiore).
     */
    List<Canzone> findTop5ByOrderByDurataDesc();

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

    // brani che non hanno mai fatto parte di playlist "da ascoltare"
    @Query("""
      SELECT c FROM Canzone c
      WHERE c.album NOT IN (
        SELECT al FROM Album al JOIN al.utentiDaAscoltare u WHERE u.id = :userId
      )
    """)
    List<Canzone> findUnqueuedSongsForUser(@Param("userId") Long userId);
}