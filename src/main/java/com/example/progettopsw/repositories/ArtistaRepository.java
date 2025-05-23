package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Album;
import com.example.progettopsw.entities.Artista;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArtistaRepository extends JpaRepository<Artista,Long> {

    Optional<Artista> findById(Long id);

    List<Artista> findByNomeContainingIgnoreCase(String nome);

    List<Artista> findByAlbumsAnnoRilascioBetween(Integer fromYear, Integer toYear);

    @Query("SELECT a FROM Artista a WHERE SIZE(a.follower) > :minFollowers ORDER BY SIZE(a.follower) DESC")
    List<Artista> findByFollowerCountGreaterThanOrderByFollowerCountDesc(@Param("minFollowers") Long minFollowers);

    @Query("SELECT a FROM Artista a JOIN a.generi g WHERE g.nome = :genere AND SIZE(a.follower) > :minFollowers")
    List<Artista> findByGenreAndFollowerCountGreaterThan(
            @Param("genere") String genere,
            @Param("minFollowers") Long minFollowers
    );

    List<Artista> findByAlbumsCanzoniNumeroAscoltiGreaterThanOrderByAlbumsCanzoniNumeroAscoltiDesc(
            Long minStreams, Pageable pageable);

    List<Artista> findByGeneriNomeIn(List<String> generi);

    Long countByIdAndGeneriNomeIn(Long id, List<String> generi);

    List<Artista> findDistinctByGeneriNomeIn(List<String> generi);

    boolean existsByNomeIgnoreCase(String nome);

    List<Artista> findByAlbumsAnnoRilascioGreaterThanEqual(Integer yearsAgo);



}

