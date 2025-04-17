package com.example.progettopsw.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "band")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class Band extends Artista {
    // solisti membri della band
    @ManyToMany
    @JoinTable(
            name = "band_solisti",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "solista_id")
    )
    private Set<Solista> membri = new HashSet<>();
}
