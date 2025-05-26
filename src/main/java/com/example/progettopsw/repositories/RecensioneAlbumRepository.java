package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Album;
import com.example.progettopsw.entities.RecensioneAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecensioneAlbumRepository extends JpaRepository<RecensioneAlbum, Long> {

    Optional<RecensioneAlbum> findById(Long id);

    List<RecensioneAlbum> findByDataRecensioneAfter(LocalDate date);

    List<RecensioneAlbum> findByVotoBetween(double min, double max);

    List<RecensioneAlbum> findByVotoGreaterThanEqual(double minVoto);

    List<RecensioneAlbum> findByAlbumIdOrderByDataRecensioneDesc(Long albumId);

    // Nuove query utili
    @Query("SELECT r FROM RecensioneAlbum r WHERE r.album.id = :albumId AND " +
            "LENGTH(r.testo) >= :minLength")
    List<RecensioneAlbum> findDetailedReviewsByAlbum(
            @Param("albumId") Long albumId,
            @Param("minLength") Integer minLength);

    @Query("SELECT r FROM RecensioneAlbum r WHERE r.user.id = :userId " +
            "ORDER BY r.dataRecensione DESC")
    List<RecensioneAlbum> findUserReviewsOrderByDate(@Param("userId") Long userId);

    @Query("SELECT AVG(r.voto) FROM RecensioneAlbum r WHERE r.album.id = :albumId")
    Optional<Double> calculateAlbumAverageRating(@Param("albumId") Long albumId);

    @Query("SELECT COUNT(r) FROM RecensioneAlbum r WHERE r.album.id = :albumId")
    Long countReviewsByAlbum(@Param("albumId") Long albumId);

    @Query("SELECT r FROM RecensioneAlbum r WHERE r.album.artista.id = :artistaId " +
            "ORDER BY r.dataRecensione DESC")
    List<RecensioneAlbum> findByArtistaOrderByDate(@Param("artistaId") Long artistaId);

    @Query("SELECT r FROM RecensioneAlbum r WHERE " +
            "LOWER(r.testo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<RecensioneAlbum> findByKeywordInText(@Param("keyword") String keyword);



}
