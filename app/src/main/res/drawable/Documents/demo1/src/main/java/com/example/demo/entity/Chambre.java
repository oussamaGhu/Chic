package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "chambre")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idChambre;
    @Enumerated(EnumType.STRING)
    TypeChambre typeC;
    @Column
    long numeroChambre;

    @OneToMany
    private Set<Reservation>Reservations;
    @ManyToOne
        private Bloc bloc;



}
