package wtksara.plantfarm.patch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.cultivation.CultivationController;
import wtksara.plantfarm.exception.ResourceNotFoundException;
import wtksara.plantfarm.response.PatchResponse;

import java.util.List;

// Pozwolenie na wysyłanie żądań cross-origin dla adresu http://localhost:3000/
@CrossOrigin(origins = "http://localhost:3000/")

// Komponent pełni role kontrolera
@RestController

// Mapowanie na poziomie klasy
@RequestMapping("/api/")
public class PatchController {

    // Wstrzykiwanie zależności, a dokładniej repozytorium patchRepository
    @Autowired
    private PatchRepository patchRepository;

    @Autowired
    private CultivationController cultivationController;

    @GetMapping("/patches")
    // Metoda zwracająca wszystkie plantacje
    public List<PatchResponse> getAllPatches() {
        return patchRepository.getAllPatches();
    }

    @GetMapping("/patches/{id}")
    // Metoda zwracająca daną plantację znaleziona po id, jeśli istnieje
    public ResponseEntity<Patch> getPatchById(@PathVariable Long id) {
        Patch patch = patchRepository.findById(id).
            orElseThrow(() -> new ResourceNotFoundException("Patch not exist with id" + id) );
        return ResponseEntity.ok(patch);
    }

    @PutMapping("/patches/{patchId}/plants/{plantId}")
    // Metoda odpowiedzialana za utworzenie uprawy, a następnie ustawienie jej na danej plantacji
    Patch setPatch(@PathVariable Long patchId, @PathVariable Long plantId)
    {
        Patch patch = patchRepository.findById(patchId).get();
        patch.setCultivation(cultivationController.createCultivation(plantId, patchId));
        patch.setAmountOfDays(0.0);
        return patchRepository.save(patch);
    }

    @PutMapping("/patches/{patchId}")
    // Metoda odpowiedzialana za zakończenie uprawy na danej plantacji
    Patch endPatch(@PathVariable Long patchId){
        Patch patch = patchRepository.findById(patchId).get();
        patch.getCultivation().setFinished(true);
        patch.setCultivation(null);
        patch.setAmountOfDays(0.0);
        return patchRepository.save(patch);
    }
}
