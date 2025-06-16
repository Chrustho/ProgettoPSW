package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {


    Optional<Album> findById(Long id);

    List<Album> findByArtistaNomeIgnoreCaseAndNomeIgnoreCase(String artistaNome, String nome);

    boolean existsByArtistaNomeIgnoreCaseAndNomeIgnoreCase(String artistaNome, String nome);

    List<Album> findByRecensioniAlbumVotoGreaterThan(Double minVoto);

    List<Album> findByArtista_Id(Long artistaId);


    List<Album> findByGeneriNomeIn(List<String> generi);

    Long countByIdAndGeneriNomeIn(Long id, List<String> generi);

    List<Album> findDistinctByGeneriNomeIn(List<String> generi);


    List<Album> findByGeneriNomeAndRecensioniAlbumVotoGreaterThan(String genere, Double soglia);

    List<Album> findByAnnoRilascioBetweenOrderByAnnoRilascioDesc(Integer startYear, Integer endYear);
}
