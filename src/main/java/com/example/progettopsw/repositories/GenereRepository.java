package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Genere;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GenereRepository extends JpaRepository<Genere, Long> {

    Optional<Genere> findById(Long id);

    boolean existsByNomeIgnoreCase(String nome);

    List<Genere> findByNomeStartingWithIgnoreCase(String prefix);


    @Query("SELECT g FROM Genere g JOIN g.albums a " +
            "JOIN a.canzoni c GROUP BY g " +
            "HAVING SUM(c.numeroAscolti) > :minStreams")
    List<Genere> findByTotalStreamsGreaterThan(@Param("minStreams") Long minStreams);

    @Query("SELECT g FROM Genere g JOIN g.albums a " +
            "JOIN a.recensioniAlbum r GROUP BY g " +
            "HAVING AVG(r.voto) > :minAvg")
    List<Genere> findByAverageRatingGreaterThan(@Param("minAvg") Double minAvg);

    @Query("SELECT g FROM Genere g JOIN g.artisti a " +
            "JOIN a.follower f GROUP BY g " +
            "HAVING COUNT(f) > :minFollowers")
    List<Genere> findByTotalFollowersGreaterThan(@Param("minFollowers") Long minFollowers);



}