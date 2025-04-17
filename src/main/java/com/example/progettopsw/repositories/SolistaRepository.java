package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Solista;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolistaRepository extends CrudRepository<Solista, Long> {

    /**
     * Ricerca solisti che suonano lo strumento indicato.
     */
    List<Solista> findByStrumentoIgnoreCase(String strumento);

    /**
     * Trova solisti che non fanno parte di alcuna band.
     */
    @Query("SELECT s FROM Solista s WHERE s.bands IS EMPTY")
    List<Solista> findMusicistiSenzaBand();

    /**
     * Solisti che hanno registrato più di N album come artisti principali.
     * Usa la relazione indiretta albums di Artista.
     */
    @Query("SELECT s FROM Solista s JOIN s.albums al GROUP BY s HAVING COUNT(al) > :n")
    List<Solista> findArtistiConPiuDiNAlbum(@Param("n") long n);


    // solisti con totale stream > soglia
    @Query("""
      SELECT s FROM Solista s
      JOIN s.albums al
      JOIN al.canzoni c
      GROUP BY s
      HAVING SUM(c.numeroAscolti) > :minStreams
    """)
    List<Solista> findHighStreamingSolisti(@Param("minStreams") long minStreams);

    // strumenti usati in band più grande di dimensione N
    @Query("""
      SELECT DISTINCT s.strumento FROM Solista s
      JOIN s.bands b
      WHERE SIZE(b.membri) >= :minMembers
    """)
    List<String> findInstrumentsInLargeBands(@Param("minMembers") int minMembers);
}

