package com.example.demo.Service;

import com.example.demo.entity.Chambre;
import com.example.demo.repository.ChambreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChambreServiceImpl implements ICHambreService {

    private final ChambreRepository ChambreRepository;

    @Override
    public List<Chambre> retrieveAllChambres() {
        return ChambreRepository.findAll();
    }

    @Override
    public Optional<Chambre> retrieveChambre(Long chambreId) {
        return ChambreRepository.findById(chambreId);
    }

    @Override
    public Chambre addChambre(Chambre chambre) {
        return ChambreRepository.save(chambre);
    }

    @Override
    public Chambre modifyChambre(Chambre chambre) {
        return ChambreRepository.save(chambre);    }

    @Override
    public void removeChambre(long chambreId) {
        ChambreRepository.deleteById(chambreId);
    }
}
