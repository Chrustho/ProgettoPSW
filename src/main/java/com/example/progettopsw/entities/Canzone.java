package com.example.progettopsw.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "canzone", schema = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Canzone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "durata", nullable = false)
    private Integer durata;

    @Column(name = "numero_ascolti", nullable = false)
    private Long numeroAscolti;

    @ManyToOne(optional = false)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @OneToMany(mappedBy = "canzone", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecensioneCanzone> recensioni = new HashSet<>();
}
