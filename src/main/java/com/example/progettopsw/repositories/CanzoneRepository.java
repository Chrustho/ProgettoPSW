package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Canzone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CanzoneRepository extends JpaRepository<Canzone, Long> {

    Optional<Canzone> findById(Long id);

    boolean existsByNomeIgnoreCaseAndAlbumNomeIgnoreCase(String nome, String albumNome);

    List<Canzone> findTop5ByOrderByDurataDesc();

    List<Canzone> findTop5ByOrderByDurataAsc();

    List<Canzone> findTop10ByOrderByNumeroAscoltiDesc();

    List<Canzone> findByNomeContainingIgnoreCase(String partial);



    @Query("SELECT DISTINCT c FROM Canzone c JOIN c.album a " +
            "JOIN a.generi g WHERE g.nome IN :generi")
    List<Canzone> findByAlbumGenres(@Param("generi") List<String> generi);

    @Query("SELECT c FROM Canzone c JOIN c.album a " +
            "JOIN a.generi g WHERE g.nome IN :generi " +
            "GROUP BY c HAVING COUNT(DISTINCT g.nome) = :generiCount")
    List<Canzone> findByAllAlbumGenres(
            @Param("generi") List<String> generi,
            @Param("generiCount") Long generiCount);



    @Query("SELECT c FROM Canzone c WHERE " +
            "LOWER(c.nome) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.album.artista.nome) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Canzone> findByTitleOrArtistContaining(@Param("keyword") String keyword);

    @Query("SELECT c FROM Canzone c WHERE c.album.artista.id = :artistId " +
            "ORDER BY c.numeroAscolti DESC")
    Page<Canzone> findByArtistIdOrderByStreamsDesc(
            @Param("artistId") Long artistId,
            Pageable pageable);

}