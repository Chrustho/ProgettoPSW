package com.example.progettopsw.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "recensione_album", schema = "orders")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode(of = "id")
public class RecensioneAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @Column(name = "data_recensione")
    private LocalDate dataRecensione;

    @Column(name = "voto", nullable = false)
    private Double voto;

    @Column(name = "testo")
    private String testo;
}