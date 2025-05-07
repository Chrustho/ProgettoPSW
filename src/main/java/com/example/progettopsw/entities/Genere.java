package com.example.progettopsw.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genere", schema = "PSWDB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Genere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "generi")
    private Set<Artista> artisti = new HashSet<>();

    @ManyToMany(mappedBy = "generi")
    private Set<Album> albums = new HashSet<>();
}