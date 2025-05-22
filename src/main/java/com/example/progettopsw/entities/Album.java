package com.example.progettopsw.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "album", schema = "PSWDB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @Basic
    @Column(name = "prezzo")
    private Float prezzo;

    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    @ManyToMany(mappedBy = "albumDaAscoltare")
    @JsonIgnore
    private Set<Users> utentiDaAscoltare = new HashSet<>();

    @ManyToMany(mappedBy = "albumPreferiti")
    @JsonIgnore
    private Set<Users> utentiPreferiti = new HashSet<>();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<RecensioneAlbum> recensioniAlbum = new HashSet<>();
}
