package com.example.demo.Controllers;

import com.example.demo.Service.ICHambreService;
import com.example.demo.entity.Chambre;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/chambres")
public class ChambreRestController {

    private final ICHambreService chambreService;

    @PostMapping("/addChambre")
    public Chambre addChambre(@RequestBody Chambre chambre) {
        return chambreService.addChambre(chambre);
    }

    @DeleteMapping("/removechambre/{chambreId}")
    public void removeChambre(@PathVariable("chambreId") long chambreId) {
        chambreService.removeChambre(chambreId);
    }

    @GetMapping("/retrieveAll")
    public List<Chambre> getChambres() {
        return chambreService.retrieveAllChambres();
    }

    @GetMapping("/retrievechambre/{chambreId}")
    public Optional<Chambre> retrieveChambre(@PathVariable long chambreId) {
        return chambreService.retrieveChambre(chambreId);
    }

    @PutMapping("/update/{chambreId}")
    public Chambre updateChambre(@RequestBody Chambre updatedChambre, @PathVariable long chambreId) {
        // Récupérer la chambre existante par ID
        Chambre existingChambre = chambreService.retrieveChambre(chambreId)
                .orElseThrow(() -> new EntityNotFoundException("Chambre with ID " + chambreId + " not found."));

        // Mettre à jour les champs de la chambre existante avec les nouvelles valeurs
        existingChambre.setIdChambre(updatedChambre.getIdChambre());
        existingChambre.setTypeC(updatedChambre.getTypeC());
        existingChambre.setBloc(updatedChambre.getBloc());
        // Ajoutez d'autres champs à mettre à jour si nécessaire

        // Enregistrer la chambre mise à jour
        return chambreService.modifyChambre(existingChambre);
    }

}
