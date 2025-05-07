package com.example.progettopsw.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "recensione_canzone", schema = "PSWDB")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode(of = "id")
public class RecensioneCanzone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "canzone_id", nullable = false)
    private Canzone canzone;

    @Column(name = "data_recensione")
    private Date dataRecensione;

    @Column(name = "voto", nullable = false)
    private Double voto;

    @Column(name = "testo")
    private String testo;
}
