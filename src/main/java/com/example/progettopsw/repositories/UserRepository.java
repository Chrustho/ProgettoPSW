package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users getUserById(Long id);

    List<Users> findByNomeIgnoreCaseOrCognomeIgnoreCase(String nome, String cognome);

    Boolean existsByEmail(String email);


    /**
     * Cerca utente per email (case-insensitive).
     */
    Users findByEmailIgnoreCase(String email);

    // utenti che seguono almeno M artisti e hanno recensito almeno N album
    @Query("""
      SELECT u FROM Users u
      WHERE SIZE(u.artistiSeguiti) >= :minArtists
        AND SIZE(u.recensioniAlbum) >= :minAlbumReviews
    """)
    List<Users> findPowerUsers(@Param("minArtists") int minArtists,
                               @Param("minAlbumReviews") int minAlbumReviews);


    /**
     * Ricerca utenti per nome o cognome (case-insensitive).
     */
    List<Users> findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(
            String nome, String cognome
    );


    /**
     * Utenti che hanno un dato album tra i preferiti.
     */
    @Query("""
      SELECT u FROM Users u
      JOIN u.albumPreferiti p
      WHERE p.id = :albumId
    """)
    List<Users> findUsersWhoFavoritedAlbum(@Param("albumId") Long albumId);


    /**
     * Utenti che seguono un dato artista.
     */
    @Query("""
      SELECT u FROM Users u
      JOIN u.artistiSeguiti a
      WHERE a.id = :artistId
    """)
    List<Users> findUsersFollowingArtist(@Param("artistId") Long artistId);

    /**
     * Utenti che seguono artisti di un certo genere musicale.
     */
    @Query("""
      SELECT DISTINCT u FROM Users u
      JOIN u.artistiSeguiti a
      JOIN a.generi g
      WHERE g.nome = :genreName
    """)
    List<Users> findUsersFollowingGenre(@Param("genreName") String genreName);


}
