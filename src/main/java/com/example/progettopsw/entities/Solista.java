package com.example.progettopsw.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "solista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Solista extends Artista {

    @Column(name = "strumento", nullable = false)
    private String strumento;

    // band di cui fa parte
    @ManyToMany(mappedBy = "membri")
    private Set<Band> bands = new HashSet<>();
}