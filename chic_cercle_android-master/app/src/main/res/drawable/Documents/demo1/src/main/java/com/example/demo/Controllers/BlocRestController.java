package com.example.demo.Controllers;

import com.example.demo.Service.IBlocService;
import com.example.demo.entity.Bloc;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/bloc")
public class BlocRestController {

    private final IBlocService blocService;

    @PostMapping("/addBloc")
    public Bloc addBloc(@RequestBody Bloc bloc) {
        return blocService.addBloc(bloc);
    }

    @DeleteMapping("/remove-bloc/{blocId}")
    public void removeBloc(@PathVariable("blocId") long blocId) {
        blocService.removeBloc(blocId);
    }

    @GetMapping("/retrieveAll")
    public List<Bloc> getBlocs() {
        return blocService.retrieveAllBlocs();
    }

    @GetMapping("/retrieve-bloc/{blocId}")
    public Optional<Bloc> retrieveBloc(@PathVariable long blocId) {
        return blocService.retrieveBloc(blocId);
    }
    @PutMapping("/update/{blocId}")
    public Bloc updateBloc(@RequestBody Bloc updatedBloc, @PathVariable long blocId) {
        // Retrieve the existing bloc by ID
        Bloc existingBloc = blocService.retrieveBloc(blocId)
                .orElseThrow(() -> new EntityNotFoundException("Bloc with ID " + blocId + " not found."));

        // Update the fields of the existing bloc with the new values
        existingBloc.setNomBloc(updatedBloc.getNomBloc());
        existingBloc.setFoyer(updatedBloc.getFoyer());
        existingBloc.setChambres(updatedBloc.getChambres());
        // Add any other fields that need updating

        // Save the updated bloc
        return blocService.modifyBloc(existingBloc);
    }


}
