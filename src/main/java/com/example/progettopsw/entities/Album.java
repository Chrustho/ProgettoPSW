package com.example.progettopsw.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "album", schema = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "anno_rilascio")
    private Integer annoRilascio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "artista_id", nullable = false)
    private Artista artista;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Canzone> canzoni = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "album_genere",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genere_id")
    )
    private Set<Genere> generi = new HashSet<>();

    @ManyToMany(mappedBy = "albumDaAscoltare")
    private Set<User> utentiDaAscoltare = new HashSet<>();

    @ManyToMany(mappedBy = "albumPreferiti")
    private Set<User> utentiPreferiti = new HashSet<>();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecensioneAlbum> recensioniAlbum = new HashSet<>();
}
