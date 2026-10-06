package com.example.demo.entity;




import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Ensure your DB supports auto-increment
    @Column(name = "id_reservation")
    private Long idReservation;

    @Column(name = "annee_universitaire", nullable = false)
    private String anneeUniversitaire;  // Better as String if it's just the academic year

    @Column(name = "est_valide", nullable = false)
    private boolean estValide;
    @ManyToMany()
    private Set<Etudiant> etudiants;
}
