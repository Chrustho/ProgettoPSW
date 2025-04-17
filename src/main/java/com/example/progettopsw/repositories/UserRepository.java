package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Utenti con almeno N recensioni di canzoni.
     */
    @Query("SELECT u FROM User u JOIN u.recensioniCanzoni rc " +
            "GROUP BY u HAVING COUNT(rc) >= :n")
    List<User> findActiveSongReviewers(@Param("n") long minReviews);

    /**
     * Utenti che non hanno recensito né album né canzoni.
     */
    @Query("SELECT u FROM User u WHERE u.recensioniAlbum IS EMPTY " +
            "AND u.recensioniCanzoni IS EMPTY")
    List<User> findCompletelyInactive();

    /**
     * Cerca utente per email (case-insensitive).
     */
    User findByEmailIgnoreCase(String email);

    // utenti che seguono almeno M artisti e hanno recensito almeno N album
    @Query("""
      SELECT u FROM User u
      WHERE SIZE(u.artistiSeguiti) >= :minArtists
        AND SIZE(u.recensioniAlbum) >= :minAlbumReviews
    """)
    List<User> findPowerUsers(@Param("minArtists") int minArtists,
                              @Param("minAlbumReviews") int minAlbumReviews);

    // utenti che hanno recensito sia album che canzoni dello stesso artista
    @Query("""
      SELECT DISTINCT u FROM User u
      JOIN u.recensioniAlbum ra
      JOIN u.recensioniCanzoni rc
      WHERE ra.album.artista = rc.canzone.album.artista
    """)
    List<User> findUsersReviewingSameArtistContent();
}
