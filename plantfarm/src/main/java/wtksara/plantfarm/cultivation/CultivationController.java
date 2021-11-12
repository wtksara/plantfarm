package wtksara.plantfarm.cultivation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.measurement.Measurement;
import wtksara.plantfarm.plant.Plant;
import wtksara.plantfarm.plant.PlantRepository;


import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/")
public class CultivationController {

    @Autowired
    private CultivationRepository cultivationRepository;

    @Autowired
    private PlantRepository plantRepository;

    // Get all details
    @GetMapping("/cultivation")
    public List<Cultivation> getAllCultivations() {
        return cultivationRepository.findAllByOrderByIdDesc();
    }


    public Cultivation createCultivation(Long plantId, Long patchId) {
        Cultivation cultivation = new Cultivation();
        Plant plant = plantRepository.findById(plantId).get();
        cultivation.setPlant(plant);
        cultivation.setGrowPatch(patchId);
        cultivation.setFinished(false);
        return cultivationRepository.save(cultivation);
    }

}
