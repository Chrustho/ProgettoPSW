package com.example.progettopsw.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "artista", schema = "PSWDB")
@Inheritance(strategy = InheritanceType.JOINED)
@ToString
public abstract class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    // generi musicali associati
    @ManyToMany
    @JoinTable(
            name = "artista_genere",
            joinColumns = @JoinColumn(name = "artista_id"),
            inverseJoinColumns = @JoinColumn(name = "genere_id")
    )
    private Set<Genere> generi = new HashSet<>();

    // album realizzati da questo artista
    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Album> albums = new HashSet<>();

    // utenti che seguono questo artista
    @ManyToMany(mappedBy = "artistiSeguiti")
    private Set<User> follower = new HashSet<>();
}
