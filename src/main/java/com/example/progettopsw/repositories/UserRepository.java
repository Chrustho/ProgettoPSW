package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Utenti con almeno N recensioni di canzoni.
     */
    @Query("SELECT u FROM User u JOIN u.recensioniCanzoni rc " +
            "GROUP BY u HAVING COUNT(rc) >= :n")
    List<User> findActiveSongReviewers(@Param("n") long minReviews);

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


    /**
     * Ricerca utenti per nome o cognome (case-insensitive).
     */
    List<User> findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(
            String nome, String cognome
    );


    /**
     * Utenti che hanno un dato album tra i preferiti.
     */
    @Query("""
      SELECT u FROM User u
      JOIN u.albumPreferiti p
      WHERE p.id = :albumId
    """)
    List<User> findUsersWhoFavoritedAlbum(@Param("albumId") Long albumId);


    /**
     * Utenti che seguono un dato artista.
     */
    @Query("""
      SELECT u FROM User u
      JOIN u.artistiSeguiti a
      WHERE a.id = :artistId
    """)
    List<User> findUsersFollowingArtist(@Param("artistId") Long artistId);

    /**
     * Utenti che seguono artisti di un certo genere musicale.
     */
    @Query("""
      SELECT DISTINCT u FROM User u
      JOIN u.artistiSeguiti a
      JOIN a.generi g
      WHERE g.nome = :genreName
    """)
    List<User> findUsersFollowingGenre(@Param("genreName") String genreName);


    /**
     * Utenti “top reviewers”: ordina per la media combinata di voti su album e canzoni.
     */
    @Query("""
      SELECT u FROM User u
      LEFT JOIN u.recensioniAlbum ra
      LEFT JOIN u.recensioniCanzoni rc
      GROUP BY u
      HAVING (
        COALESCE(AVG(ra.voto), 0) * COUNT(ra) +
        COALESCE(AVG(rc.voto), 0) * COUNT(rc)
      ) / 
      (CASE WHEN COUNT(ra)+COUNT(rc)=0 THEN 1 ELSE COUNT(ra)+COUNT(rc) END)
      >= :minAvg
    """)
    List<User> findTopReviewersByCombinedAverage(@Param("minAvg") double minAverage);


}
