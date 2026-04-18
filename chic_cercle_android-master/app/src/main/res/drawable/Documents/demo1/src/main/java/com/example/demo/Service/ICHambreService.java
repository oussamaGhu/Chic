package com.example.demo.Service;

import com.example.demo.entity.Chambre;

import java.util.List;
import java.util.Optional;

public interface ICHambreService {

    List<Chambre> retrieveAllChambres();

    Optional<Chambre> retrieveChambre(Long chambreId);

    Chambre addChambre(Chambre chambre);

    Chambre modifyChambre(Chambre chambre);

    void removeChambre(long chambreId);
}
