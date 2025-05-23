package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Solista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SolistaRepository extends JpaRepository<Solista, Long> {
    Optional<Solista> findById(Long id);

    List<Solista> findByStrumentoIgnoreCase(String strumento);

    List<Solista> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndStrumentoIgnoreCase(String nome, String strumento);

    @Query("SELECT s FROM Solista s JOIN s.albums a " +
            "JOIN a.canzoni c GROUP BY s " +
            "HAVING SUM(c.numeroAscolti) > :minStreams " +
            "ORDER BY SUM(c.numeroAscolti) DESC")
    List<Solista> findByTotalStreamsGreaterThan(@Param("minStreams") Long minStreams);

    @Query("SELECT s FROM Solista s JOIN s.generi g " +
            "WHERE g.nome IN :generi")
    List<Solista> findByGenres(@Param("generi") List<String> generi);

    @Query("SELECT s FROM Solista s JOIN s.generi g " +
            "WHERE g.nome IN :generi GROUP BY s " +
            "HAVING COUNT(DISTINCT g.nome) = :generiCount")
    List<Solista> findByAllGenres(
            @Param("generi") List<String> generi,
            @Param("generiCount") Long generiCount);

    @Query("SELECT s FROM Solista s JOIN s.albums a " +
            "JOIN a.recensioniAlbum r GROUP BY s " +
            "HAVING AVG(r.voto) > :minAvg")
    List<Solista> findByAverageRatingGreaterThan(@Param("minAvg") Double minAvg);

    @Query("SELECT s FROM Solista s JOIN s.follower f " +
            "GROUP BY s ORDER BY COUNT(f) DESC")
    Page<Solista> findAllOrderByFollowerCount(Pageable pageable);

}

