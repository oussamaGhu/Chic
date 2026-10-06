package com.example.demo.repository;


import com.example.demo.entity.Bloc;
import com.example.demo.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Bloc, Long> {


}
