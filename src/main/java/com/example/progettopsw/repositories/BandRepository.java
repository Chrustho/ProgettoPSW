package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BandRepository extends JpaRepository<Band, Long> {

    Optional<Band> findById(Long id);

    Optional<Band> findByNomeIgnoreCase(String nome);

    @Query("SELECT DISTINCT b FROM Band b JOIN b.membri m WHERE LOWER(m.strumento) = LOWER(:strumento)")
    List<Band> findByMembersInstrument(@Param("strumento") String strumento);

    @Query("SELECT b FROM Band b JOIN b.albums a JOIN a.recensioniAlbum r " +
            "GROUP BY b HAVING AVG(r.voto) >= :minAvg")
    List<Band> findByAverageAlbumRatingGreaterThan(@Param("minAvg") Double minAvg);

    @Query("SELECT b FROM Band b JOIN b.albums a JOIN a.canzoni c " +
            "GROUP BY b HAVING SUM(c.numeroAscolti) > :minStreams")
    List<Band> findByTotalStreamsGreaterThan(@Param("minStreams") Long minStreams);

    @Query("SELECT b FROM Band b JOIN b.generi g " +
            "WHERE g.nome IN :generi GROUP BY b " +
            "HAVING COUNT(DISTINCT g.nome) = :generiCount")
    List<Band> findByAllGenres(@Param("generi") List<String> generi,
                               @Param("generiCount") Long generiCount);

    List<Band> findByGeneriNomeIn(List<String> generi);

    @Query("SELECT DISTINCT b FROM Band b JOIN b.follower f WHERE f.id = :userId")
    List<Band> findByFollowerId(@Param("userId") Long userId);

    boolean existsByNomeIgnoreCase(String nome);


}
