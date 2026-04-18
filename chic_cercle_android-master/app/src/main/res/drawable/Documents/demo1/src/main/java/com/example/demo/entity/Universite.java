package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "universite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Universite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_universite")
    private Long idUniversite;

    @Column(name = "nom_universite", nullable = false)
    private String nomUniversite;

    @Column(name = "adresse", nullable = false)
    private String adresse;
    @OneToOne()

    private Foyer foyer;
}