package com.example.progettopsw.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    @JsonIgnore
    private Users user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "canzone_id", nullable = false)
    @JsonIgnore
    private Canzone canzone;

    @Column(name = "data_recensione")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRecensione;

    @Column(name = "voto", nullable = false)
    private Double voto;

    @Column(name = "testo")
    private String testo;
}
