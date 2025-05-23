package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.RecensioneCanzone;
import com.example.progettopsw.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecensioneCanzoneRepository extends JpaRepository<RecensioneCanzone, Long> {

    Optional<RecensioneCanzone> findById(Long id);

    List<RecensioneCanzone> findByDataRecensioneAfter(LocalDate date);

    List<RecensioneCanzone> findByVotoBetween(double min, double max);

    List<RecensioneCanzone> findByCanzoneIdOrderByDataRecensioneDesc(Long canzoneId);

    // Nuove query utili
    @Query("SELECT r FROM RecensioneCanzone r WHERE r.canzone.id = :canzoneId AND " +
            "LENGTH(r.testo) >= :minLength")
    List<RecensioneCanzone> findDetailedReviewsBySong(
            @Param("canzoneId") Long canzoneId,
            @Param("minLength") Integer minLength);

    @Query("SELECT r FROM RecensioneCanzone r WHERE r.user.id = :userId " +
            "ORDER BY r.dataRecensione DESC")
    List<RecensioneCanzone> findUserReviewsOrderByDate(@Param("userId") Long userId);

    @Query("SELECT AVG(r.voto) FROM RecensioneCanzone r WHERE r.canzone.id = :canzoneId")
    Optional<Double> calculateSongAverageRating(@Param("canzoneId") Long canzoneId);

    @Query("SELECT r FROM RecensioneCanzone r WHERE r.canzone.album.artista.id = :artistaId " +
            "ORDER BY r.dataRecensione DESC")
    List<RecensioneCanzone> findByArtistaOrderByDate(@Param("artistaId") Long artistaId);

    @Query("SELECT r FROM RecensioneCanzone r WHERE " +
            "LOWER(r.testo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<RecensioneCanzone> findByKeywordInText(@Param("keyword") String keyword);


    @Query("SELECT r FROM RecensioneCanzone r WHERE r.canzone.album.id = :albumId " +
            "ORDER BY r.dataRecensione DESC")
    List<RecensioneCanzone> findByAlbumOrderByDate(@Param("albumId") Long albumId);

    @Query("SELECT COUNT(DISTINCT r.user.id) FROM RecensioneCanzone r " +
            "WHERE r.canzone.id = :canzoneId")
    Long countUniqueReviewersBySong(@Param("canzoneId") Long canzoneId);

}
