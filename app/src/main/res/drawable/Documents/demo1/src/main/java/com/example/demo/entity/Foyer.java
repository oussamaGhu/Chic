package com.example.demo.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "foyer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Foyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFoyer;

    @Column
    private String nomFoyer;

    @Column
    private long capaciteFoyer;

    @OneToMany(mappedBy = "foyer")
    @ToString.Exclude
    @JsonSerialize
    private Set<Bloc> blocs;
}
