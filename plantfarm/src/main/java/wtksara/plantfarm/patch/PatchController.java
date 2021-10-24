package wtksara.plantfarm.patch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.cultivation.Cultivation;
import wtksara.plantfarm.cultivation.CultivationController;
import wtksara.plantfarm.cultivation.CultivationRepository;
import wtksara.plantfarm.exception.ResourceNotFoundException;
import wtksara.plantfarm.measurement.Measurement;
import wtksara.plantfarm.measurement.MeasurementRepository;
import wtksara.plantfarm.plant.Plant;
import wtksara.plantfarm.plant.PlantRepository;
import wtksara.plantfarm.response.PatchDetailsResponse;
import wtksara.plantfarm.response.PatchResponse;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/")
public class PatchController {

    @Autowired
    private PatchRepository patchRepository;

    @Autowired
    private CultivationRepository cultivationRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private CultivationController cultivationController;

    // Get all paches
    @GetMapping("/patches")
    List<PatchResponse> getAllPatches() {
        return patchRepository.getAllPatches();
    }

    // Get all details about patches
    @GetMapping("/patches/details")
    List<PatchDetailsResponse> getAllPatchesDetails() {
        return patchRepository.getAllPatchesDetails();
    }




    // Get patch by id
    @GetMapping("/patches/{id}")
    public ResponseEntity<Patch> getPatchById(@PathVariable Long id) {
        Patch patch = patchRepository.findById(id).
            orElseThrow(() -> new ResourceNotFoundException("Patch not exist with id" + id) );
        return ResponseEntity.ok(patch);
    }

    // Creating cultivation on selected patch
    @PutMapping("/patches/{patchId}/plant/{plantId}")
    Patch setPatch(@PathVariable Long patchId, @PathVariable Long plantId)
    {
        Patch patch = patchRepository.findById(patchId).get();

        patch.setCultivation(cultivationController.createCultivation(plantId, patchId));
        patch.setAmountOfDays(0.0);

        return patchRepository.save(patch);
    }

    // Ending cultivation on selected patch
    @PutMapping("/patches/{patchId}")
    Patch endPatch(@PathVariable Long patchId){
        Patch patch = patchRepository.findById(patchId).get();

        patch.getCultivation().setFinished(true);
        patch.setCultivation(null);
        patch.setAmountOfDays(0.0);

        return patchRepository.save(patch);
    }


}
