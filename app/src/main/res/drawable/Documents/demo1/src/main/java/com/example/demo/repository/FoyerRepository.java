package com.example.demo.repository;

import com.example.demo.entity.Bloc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoyerRepository  extends JpaRepository<Bloc, Long> {
}
