package wtksara.plantfarm.cultivation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.measurement.Measurement;
import wtksara.plantfarm.measurement.MeasurementRepository;
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

    @Autowired
    private MeasurementRepository measurementRepository;

    // Get all details
    @GetMapping("/cultivation")
    public List<Cultivation> getAllCultivations() {
        List<Cultivation> cultivations = cultivationRepository.findAllByOrderByIdDesc();
        cultivations.forEach((cultivation ->  cultivation.setMeasurements(measurementRepository.findAllByCultivationOrderByIdDesc(cultivation))));
        return cultivations;
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
