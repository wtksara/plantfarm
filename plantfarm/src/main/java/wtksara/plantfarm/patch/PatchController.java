package wtksara.plantfarm.patch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.cultivation.Cultivation;
import wtksara.plantfarm.cultivation.CultivationController;
import wtksara.plantfarm.cultivation.CultivationRepository;
import wtksara.plantfarm.measurement.Measurement;
import wtksara.plantfarm.measurement.MeasurementRepository;
import wtksara.plantfarm.plant.Plant;
import wtksara.plantfarm.plant.PlantRepository;

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

    @GetMapping
    List<Patch> getAllPatches() {
        return patchRepository.getAllPatchesDetails();
    }

    // Creating cultivation on selected patch
    @PutMapping("/patch/{patchId}/plant/{plantId}")
    Patch setPatch(@PathVariable Long patchId, @PathVariable Long plantId)
    {
        Patch patch = patchRepository.findById(patchId).get();

        patch.setCultivation(cultivationController.createCultivation(plantId));
        patch.setAmountOfDays(0.0);

        return patchRepository.save(patch);
    }

    // Ending cultivation on selected patch
    @PutMapping("/patch/{patchId}")
    Patch endPatch(@PathVariable Long patchId){
        Patch patch = patchRepository.findById(patchId).get();

        patch.setCultivation(null);
        patch.setAmountOfDays(0.0);

        return patchRepository.save(patch);
    }


}
